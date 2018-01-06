package unice.miage.pa;

import unice.miage.pa.elements.Robot;
import unice.miage.pa.engine.Board;
import unice.miage.pa.engine.ClassLoader;
import unice.miage.pa.monitor.Monitor;
import unice.miage.pa.util.ReflectionUtil;

import javax.swing.*;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static unice.miage.pa.util.ReflectionUtil.invokeMethodByTrait;

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
        frame.setSize(600, 400);

        // panel who contains our bots
        JPanel mainPanel = new JPanel(null);

        String pluginsPath = computePluginsPath();

        ArrayList<File> path = new ArrayList<File>();
        path.add(new File(pluginsPath));

        ClassLoader classLoader = new ClassLoader(path);
        System.out.println("Plugins path : " + pluginsPath);

        File pluginsRepository = new File(pluginsPath);

        List<File> repository = ClassLoader.findEveryPlugin(pluginsRepository, new ArrayList<>(), ".class");

        HashMap<String, Class<?>> plugins = new HashMap<>();
        for(File plugin : repository){
            try {
                Class<?> loadedPlugin = classLoader.loadPluginFromFile(plugin);
                if(loadedPlugin != null){
                    System.out.println("Plugin loaded " + loadedPlugin.getSimpleName());
                    plugins.put(loadedPlugin.getSimpleName(), loadedPlugin);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        HashMap strategyAnnotations = (HashMap) ClassLoader.annotationValues(plugins.get("Strategy"));

        frame.add(mainPanel);
        frame.setVisible(true);

        // Create two stupids bots
        Robot chappy = new Robot("Chappy", 100,100, 25, 25);
        Robot poirot = new Robot("Poirot", 100,100, 200, 25);


        Board game = new Board();
        game.addBot(chappy);
        game.addBot(poirot);

        Monitor boardMonitor = new Monitor(game, 20);

        try {
            Object consoleInstance = ReflectionUtil.__construct(plugins.get("Console"));
            Object graphismInstance = ReflectionUtil.__construct(plugins.get("Graphism"), mainPanel);
            Object statusLifeInstance = ReflectionUtil.__construct(plugins.get("Life"), mainPanel);
            Object statusEnergyInstance = ReflectionUtil.__construct(plugins.get("Energy"), mainPanel);
            Object moveInstance = ReflectionUtil.__construct(plugins.get("RandomMove"));

            boardMonitor.addPlugin("Console", consoleInstance);
            boardMonitor.addPlugin("Weapons", plugins.get("Weapons"));
            boardMonitor.addPlugin("Sword", plugins.get("Sword"));

            boardMonitor.addPlugin("RandomMove", moveInstance);

            boardMonitor.addPlugin("Strategy", plugins.get("Strategy"));
            boardMonitor.addPluginWithDependency("Graphism", graphismInstance, mainPanel);
            boardMonitor.addPluginWithDependency("Life", statusLifeInstance, mainPanel);
            boardMonitor.addPluginWithDependency("Energy", statusEnergyInstance, mainPanel);

            ReflectionUtil.invokeMethodByTrait(statusEnergyInstance, "draw", chappy);
            ReflectionUtil.invokeMethodByTrait(statusEnergyInstance, "draw", poirot);

            ReflectionUtil.invokeMethodByTrait(statusLifeInstance, "draw", chappy);
            ReflectionUtil.invokeMethodByTrait(statusLifeInstance, "draw", poirot);

            boardMonitor.startGame();


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
