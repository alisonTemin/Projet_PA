package unice.miage.pa;

import unice.miage.pa.elements.Robot;
import unice.miage.pa.engine.Board;
import unice.miage.pa.engine.ClassLoader;

import javax.swing.*;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Main App
 *
 */

public class App 
{
    public static void main( String[] args ) throws IllegalAccessException, InstantiationException {
        JFrame frame = new JFrame();

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        // panel who contains our bots
        JPanel mainPanel = new JPanel(null);

        String pluginsPath = computePluginsPath();

        ArrayList<File> path = new ArrayList<File>();
        path.add(new File(pluginsPath));

        ClassLoader classLoader = new ClassLoader(path);
        Class<?> strategy = null;
        try {
            strategy = classLoader.loadPlugin("fr.unice.miage.pa.plugins.strategies.Strategy");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Invoke a method (printAnything)
        Method chosenMethod = null;
        try {
            chosenMethod = strategy.getMethod("printAnything");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        try {
            String test = (String) chosenMethod.invoke(strategy.newInstance());
            System.out.println(test);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        // Create two stupids bots
        Robot chappy = new Robot("Chappy", 100, 25, 25);
        Robot poirot = new Robot("Poirot", 100, 200, 25);


        Board game = new Board();
        game.addBot(chappy);
        game.addBot(poirot);

        /*
        // TODO WIP : Re-do from Plugins
        // Draw
        Graphism pg = new Graphism(mainPanel);
        pg.drawRobot(chappy);
        pg.drawRobot(poirot);
        pg.drawWeapon(chappy, Weapons.Sword);
        pg.drawWeapon(poirot, Weapons.Sword);
        pg.moveRobot(chappy,50,50);
        pg.drawStats(chappy);
        pg.drawStats(poirot);



        frame.add(mainPanel);
        frame.setVisible(true);

        Console c1 = new Console();
        System.out.println("test de la console");*/
    }

    private static String computePluginsPath(){

        return System.getProperty("user.dir") +
                "/plugins/target/classes/";
    }
}
