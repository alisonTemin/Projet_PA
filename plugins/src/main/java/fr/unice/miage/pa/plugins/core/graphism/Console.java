package fr.unice.miage.pa.plugins.core.graphism;

import fr.unice.miage.pa.plugins.core.annotations.Plugin;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.io.*;

@Plugin(name = "Console", required = 1, type = "core")
public class Console extends JFrame {

    public Console() {
        setSize(400, 300);
        JTextArea textarea = new JTextArea();
        DefaultCaret caret = (DefaultCaret)textarea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        add("Center", new JScrollPane(textarea));
        setVisible(true);
        setLocation(400, 0);
        setTitle("Console");

        FilteredStream filteredStream = new FilteredStream(new ByteArrayOutputStream(), textarea, "a.log");
        PrintStream aPrintStream = new PrintStream(filteredStream);

        System.setOut(aPrintStream); // catches System.out messages
        System.setErr(aPrintStream); // catches error messages
    }
}
