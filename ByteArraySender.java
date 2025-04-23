import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import collections.PerformanceComparison;

public class ByteArraySender {

    public static void main(String[] args) {
        byte[] blob = new byte[10_000_000_00]; // An extremely large byte array
        // byte[] blob = new byte[1000]; // A small byte array

        for (int i = 0; i < blob.length; i++) {
            blob[i] = (byte) (i % 128);
        }

        // Send the byte array
        PerformanceComparison.measurePerformance(
            "sendBlob", 
            () -> sendBlob(blob)
        );
        // sendBlobBuffered(blob);
    }

    static void sendBlob(byte[] blob) {
        // Send the byte array
        Consumer<String> printer = System.out::print;
        
        System.out.println("Sending blob of size: " + blob.length);
        var out = new NullOutputStream(); // or System.out to print to a console
        try (var writer = new BufferedWriter(new OutputStreamWriter(out))) {
            // The commented code will throw an OutOfMemoryError
            // writer.write(Arrays.toString(blob));
            // writer.flush();

            BiFunction<String, Integer, String> stringMultiplier = (content, times) -> {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < times; i++) {
                    sb.append(content);
                }   
                return sb.toString();
            };

            // This code will work. 
            byte[] buffer = new byte[8192]; // 8KB buffer
            long written = 0, total = blob.length;
            for (int i = 0; i < blob.length; i += buffer.length) {
                int length = Math.min(buffer.length, blob.length - i);
                writer.write(Arrays.toString(Arrays.copyOfRange(blob, i, i + length)));
                written += length;
                
                // print the percentage of the data sent
                float progress = (float) written / total * 100;
                final String progressMsg = "Sending: [" + 
                    stringMultiplier.apply("#", (int) progress / 10) + 
                    stringMultiplier.apply(".", (int) (10 - (progress / 10))) + 
                    "]\r";
                printer.accept(progressMsg);
            }
            writer.flush();
            printer.accept("\nData sent.\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void sendBlobBuffered(byte[] blob) {
        byte[] buffer = new byte[1024];

        try (var out = new ByteArrayOutputStream()) {
            for (int i = 0; i < blob.length; i += buffer.length) {
                int length = Math.min(buffer.length, blob.length - i);
                out.write(blob, i, length);
            }
            out.flush();

            System.out.println("Sending blob of size: " + blob.length);
            System.out.println(out.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class NullOutputStream extends OutputStream {
    @Override
    public void write(int b) {
        // Do nothing
    }
}
