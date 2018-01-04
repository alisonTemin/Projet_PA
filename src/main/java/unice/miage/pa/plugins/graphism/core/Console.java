package unice.miage.pa.plugins.graphism.core;

import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

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
