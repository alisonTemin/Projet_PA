package unice.miage.pa;

import unice.miage.pa.Engine.Robot;
import unice.miage.pa.Plugins.Graphism.Graphism;

import javax.swing.*;

/**
 * Main App
 *
 */
public class App 
{
    public static void main( String[] args ) {
        Package.getPackages();

        JFrame frame = new JFrame();

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        // panel who contains our bots
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));

        // Create two stupids bots
        Robot chappy = new Robot("Chappy", 100, 25, 25);
        Robot poirot = new Robot("Poirot", 100, 90, 90);

        // Draw
        Graphism pg = new Graphism();
        pg.drawRobot(mainPanel, chappy);
        pg.drawRobot(mainPanel, poirot);

        frame.add(mainPanel);
        frame.setVisible(true);
    }
}
