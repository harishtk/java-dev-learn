package io.sonnet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public class SonnetReader extends BufferedReader {

    public SonnetReader(Reader in) {
        super(in);
    }

    public SonnetReader(InputStream in) {
        super(new java.io.InputStreamReader(in));
    }
    
    public void skipLines(int n) throws IOException {
        for (int i = 0; i < n; i++) {
            readLine();
        }
    }

    private String skipSonnetHeader() throws IOException {
        String line = readLine();
        while (line.isBlank()) {
            line = readLine();
        }

        if (line.startsWith("*** END OF THE PROJECT GUTENBERG EBOOK")) {
            return null;
        }
        line = readLine();
        while (line.isBlank()) {
            line = readLine();
        }
        return line;
    }

    public Sonnet readNextSonnet() throws IOException {
        String line = skipSonnetHeader();
        if (line == null) {
            return null;
        }

        Sonnet sonnet = new Sonnet();
        while (!line.isBlank()) {
            sonnet.addLine(line);
            line = readLine();
        }
        return sonnet;
    }
}
