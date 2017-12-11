package unice.miage.pa;

import unice.miage.pa.Engine.Robot;
import unice.miage.pa.Plugins.Graphism;

import javax.swing.*;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setSize(600, 400);

        Robot chappy = new Robot("Chappy", 100);

        Graphism pg = new Graphism();
        pg.drawRobot(frame, chappy);
        frame.validate();
        frame.repaint();
    }
}
