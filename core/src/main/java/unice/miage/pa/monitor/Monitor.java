package unice.miage.pa.monitor;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private HashMap<String, Robot> players;

    private ArrayList<Object> strategies;

    /**
     * Contains every plugin dependencies identified by plugin name
     */
    private HashMap<String, Object> dependencies;

    public Monitor(Board board, int rounds) {
        this.board = board;
        this.rounds = rounds;
        this.plugins = new HashMap<>();
        this.dependencies = new HashMap<>();
        this.players = new HashMap<>();
        this.strategies = new ArrayList<>();
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

        this.players = this.board.getRobots();

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

        Object strategy;
        for(String player : this.players.keySet()){

            if(player.equals("Chappy")){
                strategy = ReflectionUtil.__constructStrategy((Class)plugins.get("Strategy"), chappy, poirot, weaponCapabilities);
            } else {
                strategy = ReflectionUtil.__constructStrategy((Class)plugins.get("Strategy"), poirot, chappy, weaponCapabilities);
            }

            this.strategies.add(strategy);
        }

        // Construct strategy instance by invoking his constructor with two bots
        boolean winnerFound = false;

        while (!winnerFound) {
            if (rounds % 2 ==0) {
                this.launchBot(chappy, weaponCapabilities, this.strategies.get(0));
            } else {
                this.launchBot(poirot, weaponCapabilities, this.strategies.get(1));
            }

            this.updateBars();

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

    /**
     * Update energy / life bars if loaded
     *
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private void updateBars() throws InvocationTargetException, IllegalAccessException {
        for(String pluginName : this.plugins.keySet()){
            Pattern energyOrLifeBar = Pattern.compile("Energy(.*)|Life(.*)");
            Matcher matcher = energyOrLifeBar.matcher(pluginName);

            while (matcher.find()) {
                String botName = matcher.group(1);

                // If not capturing group 1, it's a lifeBar
                if(botName == null)
                    botName = matcher.group(2);

                Robot toUpdate = this.players.get(botName);
                ReflectionUtil.invokeMethodByTrait(plugins.get(pluginName), "update", toUpdate);
            }
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

        // The energy should increment regularly, according to the docs
        bot.incrementEnergy(5);
    }


    public void declareWinner(){

    }
}