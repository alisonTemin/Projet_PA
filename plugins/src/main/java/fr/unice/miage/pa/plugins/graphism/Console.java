package fr.unice.miage.pa.plugins.graphism;

import javax.swing.*;
import java.io.*;

public class Console extends JFrame {
    JTextArea aTextArea = new JTextArea();
    PrintStream aPrintStream = new PrintStream(new FilteredStream(new ByteArrayOutputStream(), aTextArea));

    public Console() {
        setSize(300, 300);
        add("Center", new JScrollPane(aTextArea));
        setVisible(true);

        System.setOut(aPrintStream); // catches System.out messages
        System.setErr(aPrintStream); // catches error messages
    }
}
