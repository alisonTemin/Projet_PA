package fr.unice.miage.pa.plugins.core.graphism;

import javax.swing.*;
import java.io.FileWriter;
import java.io.FilterOutputStream;
import java.io.OutputStream;

public class FilteredStream extends FilterOutputStream{
    private final JTextArea aTextArea;
    private final String logFile;

    public FilteredStream(OutputStream aStream, JTextArea aTextArea, String logFile){
        super(aStream);
        this.aTextArea = aTextArea;
        this.logFile = logFile;
    }

    @Override
    public void write(byte b[]) {
        String aString = new String(b);
        this.aTextArea.append(aString);
    }

    @Override
    public void write(byte b[], int off, int len) {
        String debugText = new String(b, off, len);
        this.aTextArea.append(debugText);

        try(FileWriter writer = new FileWriter(this.logFile, true)) {
            writer.write(debugText);
        } catch (Exception e) {
            System.out.println("Write error");
        }
    }
}
