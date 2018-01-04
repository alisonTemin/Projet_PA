package fr.unice.miage.pa.plugins.graphism;

import fr.unice.miage.pa.plugins.Plugin;
import fr.unice.miage.pa.plugins.PluginTrait;

import javax.swing.*;
import java.io.*;

@Plugin(name = "Console", required = 1, type = "core")
public class Console extends JFrame {
    JTextArea aTextArea = new JTextArea();
    PrintStream aPrintStream = new PrintStream(new FilteredStream(new ByteArrayOutputStream(), aTextArea));

    public Console() {
        setSize(450, 300);
        add("Center", new JScrollPane(aTextArea));
        setVisible(true);
        setLocation(600, 0);

        System.setOut(aPrintStream); // catches System.out messages
        System.setErr(aPrintStream); // catches error messages
    }
}
