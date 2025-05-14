package io.sonnet;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.zip.GZIPInputStream;

public class SonnetExample {

    static final String sonnetURI = "https://www.gutenberg.org/cache/epub/1041/pg1041.txt";
    static final String sonnetFileName = "sonnet.txt";

    public static void main(String[] args) {
        DownloadCallback callback = new DownloadCallback() {
            @Override
            public void onDownloadComplete(Path filePath) {
                createCompressedSonnetsFile(filePath);
                readSonnet(75);
            }

            @Override
            public void onDownloadError(String errorMessage) {
                System.err.println("Download error: " + errorMessage);
            }
        };

        new DownloadManager().downloadIfNotExists(
                "https://www.gutenberg.org/cache/epub/1041/pg1041.txt",
                "sonnet.txt",
                callback
        );

        printGzContents();
    }
    
    static void readSonnet(int sonnetNum) {
        Path path = Path.of("./downloads", "sonnets.gz");

        try (var file = Files.newInputStream(path, StandardOpenOption.READ);
             var bis = new BufferedInputStream(file);
             var dos = new DataInputStream(file)) {
            
            int numSonnets = dos.readInt();
            System.out.println("numSonnets = " + numSonnets);

            List<Integer> offsets = new ArrayList<>();
            List<Integer> lengths = new ArrayList<>();
            for (int i = 0; i < numSonnets; i++) {
                offsets.add(dos.readInt());
                lengths.add(dos.readInt());
            }

            int offest = offsets.get(sonnetNum - 1);
            int length = lengths.get(sonnetNum - 1);

            skip(bis, offest);
            byte[] bytes = readBytes(bis, length);

            try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
                 GZIPInputStream gzis = new GZIPInputStream(bais);
                 InputStreamReader isr = new InputStreamReader(gzis);
                 BufferedReader reader = new BufferedReader(isr);) {
               
                List<String> lines = reader.lines().toList();
                lines.forEach(System.out::println);
            }
            
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    static void createCompressedSonnetsFile(Path filePath) {
        int start = 33;

        List<Sonnet> sonnets = new ArrayList<>();
        try (var reader = new SonnetReader(Files.newInputStream(filePath, StandardOpenOption.READ))) {
            reader.skipLines(start);
            Sonnet sonnet;
            while ((sonnet = reader.readNextSonnet()) != null) {
                sonnets.add(sonnet);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("# sonnets = " + sonnets.size());

        int numSonnets = sonnets.size();
        Path sonnetsFile = Path.of("./downloads", "sonnets.gz");
        if (Files.exists(sonnetsFile)) {
            try {
                Files.delete(sonnetsFile);
                Files.createFile(sonnetsFile);
            } catch (IOException e) {
                System.err.println("Error deleting file: " + e.getMessage());
                e.printStackTrace();
            }
        }

        try (var outputStream = Files.newOutputStream(sonnetsFile, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
             var dos = new DataOutputStream(outputStream)) {
            
            List<Integer> offsets = new ArrayList<>();
            List<Integer> lengths = new ArrayList<>();
            byte[] encodeSonnetsBytesArray = null;

            try (ByteArrayOutputStream encodedSonnets = new ByteArrayOutputStream()) {
                for (Sonnet sonnet : sonnets) {
                    byte[] compressedBytes = sonnet.getCommpressedBytes();
                    offsets.add(encodedSonnets.size());
                    lengths.add(compressedBytes.length);
                    encodedSonnets.write(compressedBytes);
                }

                dos.writeInt(numSonnets);
                for (int i = 0; i < numSonnets; i++) {
                    dos.writeInt(offsets.get(i));
                    dos.writeInt(lengths.get(i));
                }
                encodeSonnetsBytesArray = encodedSonnets.toByteArray();
            }
            outputStream.write(encodeSonnetsBytesArray);
        } catch (IOException e) {
            System.err.println("Error writing file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    static byte[] readBytes(BufferedInputStream bis, int length) throws IOException {
        byte[] bytes = new byte[length];
        byte[] buffer = new byte[length];
        int read = bis.read(buffer);
        int copied = 0;
        while (copied < length) {
            System.arraycopy(buffer, 0, bytes, copied, read);
            copied += read;
            read = bis.read(buffer);
        }
        return bytes;
    }

    static long skip(BufferedInputStream bis, int offset) throws IOException {
        long skipped = 0;
        while (skipped < offset) {
            long n = bis.skip(offset - skipped);
            if (n <= 0) {
                break;
            }
            skipped += n;
        }
        return skipped;
    }

    static void printGzContents() {
        Path path = Path.of("./downloads", "sonnets.gz");

        // Read the compressed file
        try (var file = Files.newInputStream(path, StandardOpenOption.READ);
             var bis = new BufferedInputStream(file);
             var dos = new DataInputStream(file)) {
            
            int numSonnets = dos.readInt();
            System.out.println("numSonnets = " + numSonnets);

            List<Integer> offsets = new ArrayList<>();
            List<Integer> lengths = new ArrayList<>();
            for (int i = 0; i < numSonnets; i++) {
                offsets.add(dos.readInt());
                lengths.add(dos.readInt());
            }

            for (int sonnetNum = 1; sonnetNum < numSonnets; sonnetNum++) {
                int offset = offsets.get(sonnetNum);
                int length = lengths.get(sonnetNum);

                skip(bis, offset);
                byte[] bytes = readBytes(bis, length);

                try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
                     GZIPInputStream gzis = new GZIPInputStream(bais);
                     InputStreamReader isr = new InputStreamReader(gzis);
                     BufferedReader reader = new BufferedReader(isr)) {
                    
                    List<String> lines = reader.lines().toList();
                    System.out.println("Sonnet " + sonnetNum + ":");
                    lines.forEach(System.out::println);
                    System.out.println();
                }
            }
            
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
