package unice.miage.pa;

import unice.miage.pa.elements.Robot;
import unice.miage.pa.engine.Board;
import unice.miage.pa.engine.ClassLoader;

import javax.swing.*;
import java.io.File;
import java.lang.annotation.Annotation;
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
    public static void main( String[] args ) throws IllegalAccessException, InstantiationException, ClassNotFoundException, InterruptedException {
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
        Class<?> statusLife = classLoader.loadPlugin("fr.unice.miage.pa.plugins.graphism.status.Life");
        Class<?> statusEnergy = classLoader.loadPlugin("fr.unice.miage.pa.plugins.graphism.status.Energy");
        Class<?> randomMove = classLoader.loadPlugin("fr.unice.miage.pa.plugins.movement.RandomMove");

        frame.add(mainPanel);
        frame.setVisible(true);

        // Create two stupids bots
        Robot chappy = new Robot("Chappy", 100, 25, 25);
        Robot poirot = new Robot("Poirot", 100, 200, 25);

        Board game = new Board();
        game.addBot(chappy);
        game.addBot(poirot);

        try {
            Object consoleInstance = __construct(console);
            Object graphismInstance = __construct(graphism, mainPanel);
            Object statusLifeInstance = __construct(statusLife, mainPanel);
            Object statusEnergyInstance = __construct(statusEnergy, mainPanel);

            invokeMethodByTrait(statusEnergyInstance, "draw", chappy);
            invokeMethodByTrait(statusEnergyInstance, "draw", poirot);

            invokeMethodByTrait(statusLifeInstance, "draw", chappy);
            invokeMethodByTrait(statusLifeInstance, "draw", poirot);

            Object chappyLabel = invokeMethodByTrait(graphismInstance, "drawRobot", chappy);
            Object poirotLabel = invokeMethodByTrait(graphismInstance, "drawRobot", poirot);

            chappy.setLabel(chappyLabel);
            poirot.setLabel(poirotLabel);

            Object[] weaponsList = weapons.getEnumConstants();

            // TODO : Find a way to move weapons with bots
            //Object chappyWeapon = invokeMethodByTrait(graphismInstance, "drawWeapon", chappy, weaponsList[0], true);
            //Object poirotWeapon = invokeMethodByTrait(graphismInstance, "drawWeapon", poirot, weaponsList[0], false);

            System.out.println("Weapons ready to use : " + Arrays.toString(weaponsList));

            Object moveInstance = __construct(randomMove);

            long endTime = System.currentTimeMillis() + 15000;
            while (System.currentTimeMillis() < endTime) {
                int nextChappyMove = (Integer) invokeMethodByTrait(moveInstance, "move", null);
                invokeMethodByTrait(graphismInstance, "move", chappyLabel, nextChappyMove);
                Thread.sleep(300);
            }

            // TODO : Call strategy related code using Traits

        } catch (NoSuchMethodException | InvocationTargetException e) {
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
