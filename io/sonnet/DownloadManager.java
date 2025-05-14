package io.sonnet;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;

public class DownloadManager {

    final Path downloadPath = Path.of("./downloads");

    DownloadManager() {
        // Constructor
        if (Files.exists(downloadPath, LinkOption.NOFOLLOW_LINKS)) {
            System.out.println("Download path already exists: " + downloadPath);
        } else {
            try {
                Files.createDirectories(downloadPath);
                System.out.println("Download path created: " + downloadPath);
            } catch (Exception e) {
                System.err.println("Error creating download path: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    void downloadIfNotExists(String url, String fileName, DownloadCallback callback) {
        // Check if the file already exists
        if (fileExists(fileName)) {
            System.out.println("File already exists: " + fileName);
            callback.onDownloadComplete(downloadPath.resolve(fileName));
            return;
        }
        
        // Download the file
        downloadFile(url, fileName, callback);
    }

    private boolean fileExists(String fileName) {
        // Check if the file exists in the local directory
        return Files.exists(downloadPath.resolve(fileName));
    }

    void downloadFile(String url, String fileName, DownloadCallback callback) {
        URI uri = URI.create(url);
        
        HttpRequest request = HttpRequest.newBuilder(uri)
                .header("User-Agent", "Mozilla/5.0")
                .GET()
                .build();
        HttpClient client = HttpClient.newHttpClient();
        Path filePath = downloadPath.resolve(fileName);
        if (!Files.exists(filePath)) {
            try {
                Files.createFile(filePath);
                System.out.println("File created: " + filePath);
            } catch (Exception e) {
                System.err.println("Error creating file: " + e.getMessage());
                e.printStackTrace();
            }
        }
        var future = client.sendAsync(request, java.net.http.HttpResponse.BodyHandlers.ofFile(filePath))
                .thenAccept(response -> {
                    if (response.statusCode() == 200) {
                        System.out.println("Downloaded: " + fileName);
                        callback.onDownloadComplete(downloadPath.resolve(fileName));
                    } else {
                        System.err.println("Failed to download: " + fileName + " - Status code: " + response.statusCode());
                        callback.onDownloadError("Failed to download: " + fileName + " - Status code: " + response.statusCode());
                    }
                })
                .exceptionally(e -> {
                    System.err.println("Error downloading file: " + e.getMessage());
                    callback.onDownloadError("Error downloading file: " + e.getMessage());
                    return null;
                });
        future.join();
    }

}
