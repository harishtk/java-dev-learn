package io.sonnet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPOutputStream;

public class Sonnet {
    private List<String> lines = new ArrayList<>();

    void addLine(String line) {
        lines.add(line);
    }

    byte[] getCommpressedBytes() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (GZIPOutputStream gzip = new GZIPOutputStream(bos);
             PrintWriter writer = new PrintWriter(gzip)) {
            
            for (String line : lines) {
                writer.println(line);
            }
        }

        return bos.toByteArray();
    }
}
