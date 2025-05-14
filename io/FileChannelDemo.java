package io;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

public class FileChannelDemo {
    public static void main(String[] args) {
        final String content = "I was here!\n";

        byte[] data = content.getBytes();
        ByteBuffer out = ByteBuffer.wrap(data);

        ByteBuffer copy = ByteBuffer.allocate(12);

        Path file = Paths.get("test.txt");
        try {
            if (!file.toFile().exists()) {
                file.toFile().createNewFile();
            }
            // try (FileOutputStream ous = new FileOutputStream(file.toFile(), false);
            //     FileChannel outChan = ous.getChannel()) {
            //     outChan.truncate(0);
            
            // } catch (IOException e) {
            //     System.out.println("Unable to truncate file");
            //     e.printStackTrace();
            // }
            System.out.println("File created: " + file.toAbsolutePath());
        } catch (IOException e) {
            System.out.println("Unable to create file");
            e.printStackTrace();
        }

        try (FileChannel fc = FileChannel.open(file, StandardOpenOption.WRITE,
                StandardOpenOption.READ)) {
            int nread;
            do {
                nread = fc.read(copy);
            } while (nread != -1 && copy.hasRemaining());

            fc.position(0);
            // Write "I was here!" at the beginning of the file.
            while (out.hasRemaining()) {
                fc.write(out);
            }
            out.rewind();

            // Move to the end of the file. Copy the first 12 bytes to
            // the end of the file. Then write "I was here!" again.
            long length = fc.size();
            fc.position(length - 1);
            copy.flip();

            while (copy.hasRemaining()) {
                fc.write(copy);
            }
            while (out.hasRemaining()) {
                fc.write(out);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
