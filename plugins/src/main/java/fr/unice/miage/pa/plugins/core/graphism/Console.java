package fr.unice.miage.pa.plugins.core.graphism;

import fr.unice.miage.pa.plugins.core.annotations.Plugin;

import javax.swing.*;
import java.io.*;

@Plugin(name = "Console", required = 1, type = "core")
public class Console extends JFrame {

    public Console() {
        setSize(400, 300);
        JTextArea aTextArea = new JTextArea();
        add("Center", new JScrollPane(aTextArea));
        setVisible(true);
        setLocation(400, 0);
        setTitle("Console");

        FilteredStream filteredStream = new FilteredStream(new ByteArrayOutputStream(), aTextArea, "a.log");
        PrintStream aPrintStream = new PrintStream(filteredStream);

        System.setOut(aPrintStream); // catches System.out messages
        System.setErr(aPrintStream); // catches error messages
    }
}
