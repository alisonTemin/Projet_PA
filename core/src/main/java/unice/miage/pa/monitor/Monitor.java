package unice.miage.pa.monitor;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;

import unice.miage.pa.elements.Robot;
import unice.miage.pa.engine.Board;
import unice.miage.pa.engine.ClassLoader;
import unice.miage.pa.util.ReflectionUtil;

public class Monitor {
    private Board board;
    private int kTime = 200;
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

    public void startGame() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, InterruptedException {
        // Setting up our bots
        this.graphismInstance = plugins.get("Graphism");

        Robot chappy = this.board.getRobotByName("Chappy");
        Robot poirot = this.board.getRobotByName("Poirot");

        Object chappyLabel = ReflectionUtil.invokeMethodByTrait(this.graphismInstance, "drawRobot", chappy);
        Object poirotLabel = ReflectionUtil.invokeMethodByTrait(this.graphismInstance, "drawRobot", poirot);

        chappy.setLabel(chappyLabel);
        poirot.setLabel(poirotLabel);

        Class weapons = (Class) plugins.get("Weapons");
        Object[] weaponsList = weapons.getEnumConstants();

        HashMap weaponCapabilities = (HashMap) ClassLoader.annotationValues(plugins.get("Sword"));

        System.out.println("Weapons ready to use : " + Arrays.toString(weaponsList));

        chappy.setWeapon(plugins.get("Sword"));
        poirot.setWeapon(plugins.get("Sword"));

        HashMap<String, Robot> currentBots = this.board.getRobots();

        // Construct strategy instance by invoking his constructor with two bots
        Object strategyInstanceJoueur1 = ReflectionUtil.__constructStrategy((Class)plugins.get("Strategy"), chappy, poirot, weaponCapabilities);
        Object strategyInstanceJoueur2 = ReflectionUtil.__constructStrategy((Class)plugins.get("Strategy"), poirot, chappy, weaponCapabilities);

        // TODO Implement round system
        long endTime = System.currentTimeMillis() + 36000;
        boolean winnerFound = false;

        while (System.currentTimeMillis() < endTime && !winnerFound) {
            if (rounds % 2 ==0) {
                this.launchBot(chappy, weaponCapabilities, strategyInstanceJoueur1);
            } else {
                this.launchBot(poirot, weaponCapabilities, strategyInstanceJoueur2);
            }

            ReflectionUtil.invokeMethodByTrait(plugins.get("Energy"+chappy.getName()), "update", chappy);
            ReflectionUtil.invokeMethodByTrait(plugins.get("Energy"+poirot.getName()), "update", poirot);

            ReflectionUtil.invokeMethodByTrait(plugins.get("Life"+chappy.getName()), "update", chappy);
            ReflectionUtil.invokeMethodByTrait(plugins.get("Life"+poirot.getName()), "update", poirot);

            for(String botName : currentBots.keySet()){
                if(currentBots.get(botName).getHealth() == 0){
                    System.out.println(botName + " is dead");
                    winnerFound = true;
                }
            }
            rounds--;
            Thread.sleep(100);
        }
    }

    private void launchBot(Robot bot, HashMap weaponCapabilities, Object strategyInstance) throws InvocationTargetException, IllegalAccessException {
        int nextMove = (Integer) ReflectionUtil.invokeMethodByTrait(plugins.get("RandomMove"), "move", null);
        ReflectionUtil.invokeMethodByTrait(graphismInstance, "move", bot.getLabel(), nextMove);
        bot.setX(nextMove);

        if(bot.getEnergy() < (Integer)weaponCapabilities.get("consumeEnergy")){
            bot.incrementEnergy(10);
        } else {
            ReflectionUtil.invokeMethodByTrait(strategyInstance, "attack", null);
        }
    }


    public void declareWinner(){

    }
}