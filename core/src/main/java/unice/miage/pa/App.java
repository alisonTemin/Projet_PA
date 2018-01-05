package unice.miage.pa;

import unice.miage.pa.elements.Robot;
import unice.miage.pa.engine.Board;
import unice.miage.pa.engine.ClassLoader;

import javax.swing.*;
import java.io.File;
import java.io.FilenameFilter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Main App
 *
 */

public class App 
{
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
                if(loadedPlugin != null)
                    plugins.put(loadedPlugin.getSimpleName(), loadedPlugin);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        HashMap strategyAnnotations = (HashMap) ClassLoader.annotationValues(plugins.get("Strategy"));

        frame.add(mainPanel);
        frame.setVisible(true);

        // Create two stupids bots
        Robot chappy = new Robot("Chappy", 100, 25, 25);
        Robot poirot = new Robot("Poirot", 100, 200, 25);

        Board game = new Board();
        game.addBot(chappy);
        game.addBot(poirot);

        try {
            Object consoleInstance = __construct(plugins.get("Console"));
            Object graphismInstance = __construct(plugins.get("Graphism"), mainPanel);
            Object statusLifeInstance = __construct(plugins.get("Life"), mainPanel);
            Object statusEnergyInstance = __construct(plugins.get("Energy"), mainPanel);

            invokeMethodByTrait(statusEnergyInstance, "draw", chappy);
            invokeMethodByTrait(statusEnergyInstance, "draw", poirot);

            invokeMethodByTrait(statusLifeInstance, "draw", chappy);
            invokeMethodByTrait(statusLifeInstance, "draw", poirot);

            Object chappyLabel = invokeMethodByTrait(graphismInstance, "drawRobot", chappy);
            Object poirotLabel = invokeMethodByTrait(graphismInstance, "drawRobot", poirot);

            chappy.setLabel(chappyLabel);
            poirot.setLabel(poirotLabel);

            Object[] weaponsList = plugins.get("Weapons").getEnumConstants();

            // TODO : Find a way to move weapons with bots
            //Object chappyWeapon = invokeMethodByTrait(graphismInstance, "drawWeapon", chappy, weaponsList[0], true);
            //Object poirotWeapon = invokeMethodByTrait(graphismInstance, "drawWeapon", poirot, weaponsList[0], false);

            System.out.println("Weapons ready to use : " + Arrays.toString(weaponsList));

            chappy.setWeapon(plugins.get("Sword"));
            poirot.setWeapon(plugins.get("Sword"));

            HashMap weaponCapabilities = (HashMap) ClassLoader.annotationValues(plugins.get("Sword"));

            Object moveInstance = __construct(plugins.get("RandomMove"));

            long endTime = System.currentTimeMillis() + 15000;
            while (System.currentTimeMillis() < endTime) {
                int nextChappyMove = (Integer) invokeMethodByTrait(moveInstance, "move", null);
                invokeMethodByTrait(graphismInstance, "move", chappyLabel, nextChappyMove);
                chappy.setX(nextChappyMove);

                System.out.println("Weapon capabilities :" + weaponCapabilities);

                int weaponDistance = (Integer) weaponCapabilities.get("distance");

                /* TODO : Check current robot location
                    If robot X can be attacked with current weapon distance (using weaponCapabilities->distance up)
                */
                Thread.sleep(300);
            }

            // TODO : Call strategy related code using Traits

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

    }

    /**
     * Invoke a method using a PluginTrait
     *
     * @param pluginInstance instance to invoke on
     * @param type "draw"
     * @param on "robot"
     * @param args ... Args array (TODO : Refactor, it's crappy, but working)
     *
     * @throws InvocationTargetException Instance target not good, probably a copy paste error
     * @throws IllegalAccessException Method is protected / private
     */
    private static Object invokeMethodByTrait(Object pluginInstance, String type, Object on, Object... args) throws InvocationTargetException, IllegalAccessException {
        Method[] methods = pluginInstance.getClass().getMethods();

        for(Method mt : methods) {
            for(Annotation annot : mt.getAnnotations()){
                Class<? extends Annotation> trait = annot.annotationType();

                for (Method method : trait.getDeclaredMethods()) {
                    if(method.invoke(annot).equals(type)){

                        if(args.length > 0)
                            System.out.println("Invoke " + type + " | with : "+ Arrays.toString(args));
                        else if(on != null)
                            System.out.println("Invoke " + type + " | on : " + on);
                        else
                            System.out.println("Invoke " + type);

                        if(args.length == 1)
                            return mt.invoke(pluginInstance, on, args[0]);
                        else if(args.length == 2)
                            return mt.invoke(pluginInstance, on, args[0], args[1]);
                        else if(on == null)
                            return mt.invoke(pluginInstance);
                        else
                            return mt.invoke(pluginInstance, on);
                    }
                }
            }
        }

        return null;
    }

    /**
     *
     * @param pluginClass pluginClass
     * @return Object
     *
     * @throws NoSuchMethodException Constructor not found
     * @throws IllegalAccessException Constructor protected / private
     * @throws InvocationTargetException Wrong target class
     * @throws InstantiationException Probably a param mismatch
     */
    private static Object __construct(Class pluginClass) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor constructor = pluginClass.getDeclaredConstructor();
        return constructor.newInstance();
    }

    /**
     * Constructor with one object
     * @param pluginClass pluginClass
     * @param arg param
     * @return Object
     *
     * @throws NoSuchMethodException Constructor not found
     * @throws IllegalAccessException Constructor protected / private
     * @throws InvocationTargetException Wrong target class
     * @throws InstantiationException Probably a param mismatch
     */
    private static Object __construct(Class pluginClass, Object arg) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor constructor = pluginClass.getDeclaredConstructor(arg.getClass());
        return constructor.newInstance(arg);
    }

    /**
     * Get plugin compiled classes using user.dir variables (to avoid mac/win issues)
     * @return path to plugins classes "/robotwar/plugins/target/classes/"
     */
    private static String computePluginsPath(){
        return System.getProperty("user.dir") +
                "/plugins/target/classes/";
    }
}
