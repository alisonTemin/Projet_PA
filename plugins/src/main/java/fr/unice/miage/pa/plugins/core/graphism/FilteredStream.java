package fr.unice.miage.pa.plugins.core.graphism;

import javax.swing.*;
import java.io.FileWriter;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FilteredStream extends FilterOutputStream{
    private final JTextArea aTextArea;
    private final String logFile;

    public FilteredStream(OutputStream aStream, JTextArea aTextArea, String logFile){
        super(aStream);
        this.aTextArea = aTextArea;
        this.logFile = logFile;
    }

    public void write(byte b[]) {
        String aString = new String(b);
        this.aTextArea.append(aString);
    }

    public void write(byte b[], int off, int len) throws IOException {
        String debugText = new String(b, off, len);
        this.aTextArea.append(debugText);

        FileWriter aWriter = new FileWriter(this.logFile, true);
        aWriter.write(debugText);
        aWriter.close();
    }
}
