package com.example;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Stream;

public class GitRepoChecker {

    public static void main(String[] args) {
        String directoryPath = System.getProperty("directory");
        if (directoryPath == null) {
            System.err.println("Error: Please provide a directory using -Ddirectory=\"path/to/your/directory\"");
            return;
        }

        Path startPath = Paths.get(directoryPath);

        try (Stream<Path> stream = Files.walk(startPath)) {
            stream.filter(Files::isDirectory)
                  .forEach(GitRepoChecker::checkGitRepository);
        } catch (Exception e) {
            System.err.println("Error walking the directory tree: " + e.getMessage());
        }
    }

    private static void checkGitRepository(Path directory) {
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
                    System.out.println("Repository with uncommitted changes: " + directory);
                    System.out.println("  Branch: " + repository.getBranch());
                    System.out.println("  Uncommitted changes: " + uncommittedChanges);
                    System.out.println("  Untracked files: " + untracked);
                }
            }
        } catch (Exception e) {
            System.err.println("Error checking Git repository at " + directory + ": " + e.getMessage());
        }
    }
}