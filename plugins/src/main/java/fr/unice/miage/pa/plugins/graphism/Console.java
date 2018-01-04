package fr.unice.miage.pa.plugins.graphism;

import javax.swing.*;
import java.io.*;

public class Console extends JFrame {
    JTextArea aTextArea = new JTextArea();
    PrintStream aPrintStream = new PrintStream(new FilteredStream(new ByteArrayOutputStream()));

    public Console() {
        setSize(300, 300);
        add("Center", new JScrollPane(aTextArea));
        setVisible(true);

        System.setOut(aPrintStream); // catches System.out messages
        System.setErr(aPrintStream); // catches error messages
    }

    class FilteredStream extends FilterOutputStream {
        public FilteredStream(OutputStream aStream) {
            super(aStream);
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
}
