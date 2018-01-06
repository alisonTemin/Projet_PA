package unice.miage.pa.monitor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;

import unice.miage.pa.elements.Robot;
import unice.miage.pa.engine.Board;
import unice.miage.pa.util.ReflectionUtil;

public class Monitor implements ActionListener{
    private Board board;
    private int time;
    private int rounds;

    private Object graphismInstance;
    private HashMap<String, Object> plugins;

    /**
     * Contains every plugin dependencies identified by plugin name
     */
    private HashMap<String, Object> dependencies;

    public Monitor(Board board, int rounds) {
        this.board = board;
        this.rounds = rounds;
        this.plugins = new HashMap<>();
        this.dependencies = new HashMap<>();
    }

    public void addPlugin(String name, Object pluginInstance){
        this.plugins.put(name, pluginInstance);
    }

    public void addPluginWithDependency(String name, Object pluginInstance, Object dependency){
        this.plugins.put(name, pluginInstance);
        this.dependencies.put(name, dependency);
    }

    public void startGame() throws InvocationTargetException, IllegalAccessException {
        boolean dead = false;

        // Setting up our bots
        this.graphismInstance = plugins.get("Graphism");

        Robot chappy = this.board.getRobotByName("Chappy");
        Robot poirot = this.board.getRobotByName("Poirot");

        Object chappyLabel = ReflectionUtil.invokeMethodByTrait(this.graphismInstance, "drawRobot", chappy);
        Object poirotLabel = ReflectionUtil.invokeMethodByTrait(this.graphismInstance, "drawRobot", poirot);

        chappy.setLabel(chappyLabel);
        poirot.setLabel(poirotLabel);

        Object[] weaponsList = plugins.get("Weapons").getClass().getEnumConstants();

        System.out.println("Weapons ready to use : " + Arrays.toString(weaponsList));

        chappy.setWeapon(plugins.get("Sword"));
        poirot.setWeapon(plugins.get("Sword"));

        HashMap<String, Robot> currentBots = this.board.getRobots();

        while (!dead) {
            currentBots.forEach((botName,botObject) -> System.out.println("Bot: "+botName+" Object:"+botObject));
            dead = true;
        }
    }





    public  void    actionPerformed(ActionEvent e)
    {

    }



}