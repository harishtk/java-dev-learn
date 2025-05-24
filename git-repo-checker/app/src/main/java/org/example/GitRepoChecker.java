package org.example;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import javax.annotation.Nonnull;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

public class GitRepoChecker {

    public static void main(String[] args) {
        String directoryPath = args.length > 0 ? args[0] : null;
        if (directoryPath == null) {
            System.err.println("Error: Please provide a directory using -Ddirectory=\"path/to/your/directory\"");
            return;
        }

        int maxDepth = 3;
        if (args.length > 1) {
            try {
                maxDepth = Integer.parseInt(args[1]);
                System.out.println("maxDepth=" + maxDepth);
            } catch (Exception ignore) {}
        }

        final GitRepoChecker app = new GitRepoChecker();
        long startTime = System.currentTimeMillis();
        app.run(directoryPath, maxDepth);

        long elapsed = System.currentTimeMillis() - startTime;
        System.out.println("Completed in " + elapsed + "ms");
    }

    private void run(String directoryPath, int maxDepth) {
        Path startPath = Paths.get(directoryPath);
        System.out.println("Walking.. " + startPath);
        
        final ExecutorService executor = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors() / 2
        );

        try (Stream<Path> stream = Files.walk(startPath, maxDepth)) {
            stream.filter(Files::isDirectory)
                    .forEach(path -> {
                        Path gitDir = path.resolve(".git");
                        if (Files.exists(gitDir) && Files.isDirectory(gitDir)) {
                            executor.submit(() -> checkGitRepository(path));
                        }
                    });
            executor.shutdown();
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (Exception e) {
            System.err.println("Error walking the directory tree: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void checkGitRepository(@Nonnull Path directory) {
        File gitDir = directory.resolve(".git").toFile();
        if (!gitDir.exists() || !gitDir.isDirectory()) {
            return; // Not a Git repository
        }

        try {
            FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
            Repository repository = repositoryBuilder.setGitDir(gitDir)
                    .readEnvironment()
                    .findGitDir()
                    .build();

            try (Git git = new Git(repository)) {
                Status status = git.status().call();
                Set<String> uncommittedChanges = status.getUncommittedChanges();
                Set<String> untracked = status.getUntracked();

                if (!uncommittedChanges.isEmpty() || !untracked.isEmpty()) {
                    System.out.println(directory.getName(directory.getNameCount() - 1) + " is dirty on " + repository.getBranch());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error checking Git repository at " + directory + ": " + e.getMessage());
        }
    }
}