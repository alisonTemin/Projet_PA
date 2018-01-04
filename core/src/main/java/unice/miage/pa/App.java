package unice.miage.pa;

import unice.miage.pa.elements.Robot;
import unice.miage.pa.engine.Board;
import unice.miage.pa.engine.ClassLoader;

import javax.swing.*;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Main App
 *
 */

public class App 
{
    public static void main( String[] args ) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        JFrame frame = new JFrame();

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        // panel who contains our bots
        JPanel mainPanel = new JPanel(null);

        String pluginsPath = computePluginsPath();

        ArrayList<File> path = new ArrayList<File>();
        path.add(new File(pluginsPath));

        ClassLoader classLoader = new ClassLoader(path);
        System.out.println("Plugins path : " + pluginsPath);

        // TODO : Iterate over annotations to load every plugin

        Class<?> strategy = classLoader.loadPlugin("fr.unice.miage.pa.plugins.strategies.Strategy");
        Class<?> weapons = classLoader.loadPlugin("fr.unice.miage.pa.plugins.attacks.weapons.Weapons");
        Class<?> attacks = classLoader.loadPlugin("fr.unice.miage.pa.plugins.attacks.core.Attacks");
        Class<?> graphism = classLoader.loadPlugin("fr.unice.miage.pa.plugins.graphism.Graphism");
        Class<?> filteredStream = classLoader.loadPlugin("fr.unice.miage.pa.plugins.graphism.FilteredStream");
        Class<?> console = classLoader.loadPlugin("fr.unice.miage.pa.plugins.graphism.Console");

        try {
            Object consoleInstance = __construct(console);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        // Create two stupids bots
        Robot chappy = new Robot("Chappy", 100, 25, 25);
        Robot poirot = new Robot("Poirot", 100, 200, 25);

        Board game = new Board();
        game.addBot(chappy);
        game.addBot(poirot);

        try {
            Method drawRobot = graphism.getMethod("drawRobot", Object.class);
            Object graphismInstance = __construct(graphism, mainPanel);

            drawRobot.invoke(graphismInstance, chappy);
            drawRobot.invoke(graphismInstance, poirot);

            Method drawWeapon = graphism.getMethod("drawWeapon", Object.class, weapons);

            Object[] weaponsList = weapons.getEnumConstants();

            System.out.println("Weapons ready to use : " + Arrays.toString(weaponsList));

            drawWeapon.invoke(graphismInstance, chappy, weaponsList[0]);
            drawWeapon.invoke(graphismInstance, poirot, weaponsList[0]);

            Method drawStats = graphism.getMethod("drawStats", Object.class);
            drawStats.invoke(graphismInstance, chappy);
            drawStats.invoke(graphismInstance, poirot);
        } catch (NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private static Object __construct(Class pluginClass) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor constructor = pluginClass.getDeclaredConstructor();
        return constructor.newInstance();
    }

    private static Object __construct(Class pluginClass, Object arg) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor constructor = pluginClass.getDeclaredConstructor(arg.getClass());
        return constructor.newInstance(arg);
    }

    private static String computePluginsPath(){

        return System.getProperty("user.dir") +
                "/plugins/target/classes/";
    }
}
