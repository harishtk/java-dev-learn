package io.sonnet;

import java.nio.file.Path;

public interface DownloadCallback {
    void onDownloadComplete(Path filePath);
    void onDownloadError(String errorMessage);    
}