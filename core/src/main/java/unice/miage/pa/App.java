package unice.miage.pa;

import unice.miage.pa.engine.Board;
import unice.miage.pa.engine.ClassLoader;
import unice.miage.pa.monitor.Monitor;
import unice.miage.pa.util.ReflectionUtil;

import javax.swing.*;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * Main App
 *
 */
public class App 
{
    /**
     * When launching from java -jar, we need to be at project path (where plugins dir is located, if not, the Autoload can't work)
     * @param args args
     */
    public static void main( String[] args ) throws IllegalAccessException, InstantiationException, ClassNotFoundException, InterruptedException, InvocationTargetException {
        JFrame frame = new JFrame();

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(400, 450);

        // panel who contains our bots
        JPanel mainPanel = new JPanel(null);

        String pluginsPath = computePluginsPath();

        ClassLoader classLoader = new ClassLoader();
        HashMap<String, Class<?>> plugins = classLoader.getPluginsMap(pluginsPath);
        
        frame.add(mainPanel);
        frame.setVisible(true);

        Board game = new Board();

        Monitor boardMonitor = new Monitor(game, mainPanel);

        // TODO : Refactor below
        try {
            Object consoleInstance = ReflectionUtil.__construct(plugins.get("Console"));
            Object graphismInstance = ReflectionUtil.__construct(plugins.get("Graphism"), mainPanel);
            Object moveInstance = ReflectionUtil.__construct(plugins.get("RandomMove"));

            boardMonitor.addPlugin("Console", consoleInstance);
            boardMonitor.addPlugin("Weapons", plugins.get("Weapons"));
            boardMonitor.addPlugin("Sword", plugins.get("Sword"));

            boardMonitor.addPlugin("RandomMove", moveInstance);

            boardMonitor.addPlugin("Strategy", plugins.get("Strategy"));
            boardMonitor.addPluginWithDependency("Graphism", graphismInstance, mainPanel);
            boardMonitor.addPluginWithDependency("Life", plugins.get("Life"), mainPanel);
            boardMonitor.addPluginWithDependency("Energy", plugins.get("Energy"), mainPanel);

            boardMonitor.startGame(8);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

    }

    /**
     * Get plugin compiled classes using user.dir variables (to avoid mac/win issues)
     * @return path to plugins classes "/robotwar/plugins/target/classes/"
     */
    private static String computePluginsPath(){
        return System.getProperty("user.dir") +
                File.separatorChar + "plugins"+
                File.separatorChar + "target" +
                File.separatorChar + "classes"+
                File.separatorChar;

    }
}
