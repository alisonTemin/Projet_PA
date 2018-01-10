package unice.miage.pa;

import unice.miage.pa.engine.Board;
import unice.miage.pa.engine.ClassLoader;
import unice.miage.pa.monitor.Monitor;
import unice.miage.pa.util.ReflectionUtil;

import javax.swing.*;
import java.io.File;
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
    public static void main( String[] args ) throws Exception {
        JFrame frame = new JFrame();

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setTitle("RobotWar");


        // panel who contains our bots
        JPanel mainPanel = new JPanel(null);

        String pluginsPath = computePluginsPath();

        ClassLoader classLoader = new ClassLoader();
        HashMap<String, Class<?>> plugins = classLoader.getPluginsMap(pluginsPath);
        
        frame.add(mainPanel);
        frame.setVisible(true);

        Board game = new Board();

        Monitor boardMonitor = new Monitor(game, mainPanel, frame);

        try {
            Object consoleInstance = ReflectionUtil.__construct(plugins.get("Console"));
            Object graphismInstance = ReflectionUtil.__construct(plugins.get("Graphism"), mainPanel);
            Object moveInstance = ReflectionUtil.__construct(plugins.get("RandomMove"));

            for(String plugin : plugins.keySet()){
                if(plugin.startsWith("Custom")){
                    boardMonitor.addPlugin(plugin, plugins.get(plugin));
                }
                if(plugin.endsWith("Move")){
                    boardMonitor.addPlugin(plugin, plugins.get(plugin));
                }
            }

            boardMonitor.addPlugin("Console", consoleInstance);
            boardMonitor.addPlugin("Weapons", plugins.get("Weapons"));
            boardMonitor.addPlugin("Sword", plugins.get("Sword"));

            boardMonitor.addPlugin("RandomMove", moveInstance);

            boardMonitor.addPlugin("Strategy", plugins.get("Strategy"));
            boardMonitor.addPluginWithDependency("Graphism", graphismInstance, mainPanel);
            boardMonitor.addPluginWithDependency("Life", plugins.get("Life"), mainPanel);
            boardMonitor.addPluginWithDependency("Energy", plugins.get("Energy"), mainPanel);

            int countBotAsParam = 6;

            if(args.length > 0) countBotAsParam = Integer.valueOf(args[0]);

            // Workaround defaulting to pair value
            if(countBotAsParam % 2 != 0)
                countBotAsParam = countBotAsParam - 1;

            boardMonitor.startGame(countBotAsParam, frame);
        } catch (Exception e) {
            e.printStackTrace();

            throw new Exception("Fatal error");
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
