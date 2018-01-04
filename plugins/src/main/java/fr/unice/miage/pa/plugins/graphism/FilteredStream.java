package fr.unice.miage.pa.plugins.graphism;

import javax.swing.*;
import java.io.FileWriter;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FilteredStream extends FilterOutputStream{
    private final JTextArea aTextArea;

    public FilteredStream(OutputStream aStream, JTextArea aTextArea){
        super(aStream);
        this.aTextArea = aTextArea;
    }

    public void write(byte b[]) throws IOException {
        String aString = new String(b);
        aTextArea.append(aString);
    }

    public void write(byte b[], int off, int len) throws IOException {
        String aString = new String(b, off, len);
        aTextArea.append(aString);
        FileWriter aWriter = new FileWriter("a.log", true);
        aWriter.write(aString);
        aWriter.close();
    }
}
