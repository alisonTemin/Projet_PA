package unice.miage.pa;

import unice.miage.pa.elements.Robot;
import unice.miage.pa.engine.Board;
import unice.miage.pa.plugins.graphism.core.Graphism;

import javax.swing.*;

/**
 * Main App
 *
 */
public class App 
{
    public static void main( String[] args ) {
        JFrame frame = new JFrame();

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        // panel who contains our bots
        JPanel mainPanel = new JPanel(null);

        // Create two stupids bots
        Robot chappy = new Robot("Chappy", 100, 25, 25);
        Robot poirot = new Robot("Poirot", 100, 200, 25);

        Board game = new Board();
        game.addBot(chappy);
        game.addBot(poirot);


        // Draw
        Graphism pg = new Graphism(mainPanel);
        pg.drawRobot(chappy);
        pg.drawRobot(poirot);
        pg.drawWeapon(chappy);
        pg.drawWeapon(poirot);
        pg.moveRobot(chappy,50,50);

        frame.add(mainPanel);
        frame.setVisible(true);
    }
}
