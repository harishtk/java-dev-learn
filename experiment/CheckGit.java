package experiment;

import java.io.PrintStream;
import java.nio.file.Paths;

/**
 * When you run this code, it will print the dirty branches and uncommitted changes
 * present in the given directory tree and it's decendants.
 */
public class CheckGit {
    public static void main(String[] args) {
        String path = "D:\\Workspace\\StudioProjects";
        String command = "git status --porcelain --untracked-files=all " + path;
        try {
            Process process = Runtime.getRuntime().exec(command);
            // Wait for the process to complete
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.err.println("Error executing git status command. Exit code: " + exitCode);
                return;
            }

            // Prin the error stream of the process
            try (var errorStream = process.getErrorStream();
                 var reader = new java.io.BufferedReader(new java.io.InputStreamReader(errorStream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.err.println(line);
                }
            }
            
            // Print the output stream of the process
            try (var inputStream = process.getInputStream();
                 var reader = new java.io.BufferedReader(new java.io.InputStreamReader(inputStream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }
            System.out.println("Git status command executed successfully.");
        } catch (Exception e) {
            System.err.println("Error executing git status command: " + e.getMessage());
        }
    }
    
}
