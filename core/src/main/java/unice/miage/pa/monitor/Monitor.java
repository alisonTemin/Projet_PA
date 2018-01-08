package unice.miage.pa.monitor;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import unice.miage.pa.elements.Robot;
import unice.miage.pa.engine.Board;
import unice.miage.pa.engine.ClassLoader;
import unice.miage.pa.util.ReflectionUtil;

import javax.swing.*;

public class Monitor {
    private final JPanel panel;
    private final Board board;

    private Object graphismInstance;
    private final HashMap<String, Object> plugins;
    private ArrayList<Robot> players;

    private final ArrayList<Object> strategies;

    /**
     * Contains every plugin dependencies identified by plugin name
     */
    private final HashMap<String, Object> dependencies;

    /**
     * Monitor constructor
     * @param board Game board
     */
    public Monitor(Board board, JPanel panel) {
        this.board = board;
        this.plugins = new HashMap<>();
        this.dependencies = new HashMap<>();
        this.players = new ArrayList<>();
        this.strategies = new ArrayList<>();
        this.panel = panel;
    }

    /**
     * Add a plugin to monitor
     * @param name plugin name
     * @param pluginInstance instance
     */
    public void addPlugin(String name, Object pluginInstance){
        this.plugins.put(name, pluginInstance);
    }

    /**
     * Add a plugin to monitor
     * @param name plugin name
     * @param pluginInstance plugin instance
     * @param dependency dependency
     */
    public void addPluginWithDependency(String name, Object pluginInstance, Object dependency){
        this.plugins.put(name, pluginInstance);
        this.dependencies.put(name, dependency);
    }

    public void generateFakePlayers(int count) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        HashMap weaponCapabilities = (HashMap) ClassLoader.annotationValues(plugins.get("Sword"));

        Random rnd = new Random();
        String firstname = "Hercules";
        String lastname = "Poirot";

        int x = 10;
        int y = 10;

        for(int i = 0; i < count; i++){
            String result = Character.toString(firstname.charAt(0)); // First char
            if (lastname.length() > 5)
                result += lastname.substring(0, 5);
            else
                result += lastname;

            result += Integer.toString(rnd.nextInt(900));

            if(i > 0 && i % 2 != 0){
                x = x + 200;
            }

            if(i % 2 == 0 && i != 0){
                x = 10;
                y = y + 100;
            }

            // Create two stupids bots
            Robot chappy = new Robot(result, 100,100, x, y);

            this.board.addBot(chappy);

        }

        int botCount = 0;
        ArrayList<Robot> bots = this.board.getRobots();

        for(Robot bot : bots){
            Object strategy = null;
            Robot opponent = null;
            if(botCount % 2 == 0 && botCount < bots.size()){
                opponent = bots.get(botCount+1);
                strategy = ReflectionUtil.__constructStrategy((Class)plugins.get("Strategy"), bot, opponent, weaponCapabilities);
            } else if(botCount % 2 != 0 && botCount < bots.size()){
                opponent = bots.get(botCount-1);
                strategy = ReflectionUtil.__constructStrategy((Class)plugins.get("Strategy"), bot, opponent, weaponCapabilities);
            }

            if(strategy != null && opponent != null){
                bot.setStrategy(strategy);
                bot.setOpponent(opponent);
                this.strategies.add(strategy);
            }
            botCount++;
        }
    }

    /**
     * Starting the game
     *
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws InterruptedException
     */
    public void startGame(int playersCount, JFrame mainFrame) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, InterruptedException {
        this.graphismInstance = plugins.get("Graphism");
        this.generateFakePlayers(playersCount);

        this.players = this.board.getRobots();

        // Drawing our bots
        // TODO : If loaded plugin Graphism..
        String playerNames = "Players : " + this.players.size();
        for(Robot robot : this.players){
            this.setupBot(robot);
        }

        mainFrame.setSize( 400,this.players.size() * 60);


        HashMap<String, Robot> winners = new HashMap<>();

        this.startWar(playerNames, winners);
    }

    private void startWar(String playerNames, HashMap<String, Robot> winners) throws InvocationTargetException, IllegalAccessException, InterruptedException {
        int winnersFound = 0;
        System.out.println("War started | " + playerNames);
        while (winnersFound != this.players.size()/2) {
            HashMap weaponCapabilities = (HashMap) ClassLoader.annotationValues(plugins.get("Sword"));


            for(Robot bot : this.players) {
                if(bot.getHealth() != 0 && bot.getOpponent().getHealth() != 0){
                    this.launchBot(bot, weaponCapabilities, bot.getStrategy());
                    this.updateBars();
                } else {
                    if(!winners.containsKey(bot.getName()) && bot.getHealth() != 0){
                        System.out.println(bot.getName() + " is dead | Killed by : " + bot.getOpponent().getName());
                        winners.put(bot.getName(), bot);
                    }

                    winnersFound = winners.size();
                }
            }

            Thread.sleep(300);
        }

        if(winnersFound == this.players.size()/2){
            System.out.println("Game has ended, will stop in a sec");
            Thread.sleep(1000);
            System.exit(0);
        }
    }

    /**
     * Setup graphism aspects of a bot
     *
     * @param robot Robot to setup
     *
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InstantiationException
     */
    private void setupBot(Robot robot) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        // Draw bot
        Object botLabel = ReflectionUtil.invokeMethodByTrait(this.graphismInstance, "drawRobot", robot);
        robot.setLabel(botLabel);

        // Draw bars
        Object statusLifeBot = ReflectionUtil.__construct((Class)plugins.get("Life"), this.panel);
        Object energyLifeBot = ReflectionUtil.__construct((Class)plugins.get("Energy"), this.panel);
        ReflectionUtil.invokeMethodByTrait(statusLifeBot, "draw", robot);
        ReflectionUtil.invokeMethodByTrait(energyLifeBot, "draw", robot);

        plugins.put("Life"+robot.getName(), statusLifeBot);
        plugins.put("Energy"+robot.getName(), energyLifeBot);

        robot.setWeapon(plugins.get("Sword"));
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

                Robot toUpdate = this.board.getRobotByName(botName);
                ReflectionUtil.invokeMethodByTrait(plugins.get(pluginName), "update", toUpdate);
            }
        }
    }

    /**
     * Launch a bot
     *
     * @param bot bot to launch
     * @param weaponCapabilities Weapon cap hashmap
     * @param strategyInstance Strategy object
     *
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private void launchBot(Robot bot, HashMap weaponCapabilities, Object strategyInstance) throws InvocationTargetException, IllegalAccessException {
        int nextMove = (int) ReflectionUtil.invokeMethodByTrait(plugins.get("RandomMove"), "move", null);

        int nextMoveY = (int) ReflectionUtil.invokeMethodByTrait(plugins.get("RandomMove"), "moveY", null);

        ReflectionUtil.invokeMethodByTrait(graphismInstance, "move", bot.getLabel(), nextMove,nextMoveY);
        bot.setX(nextMove);
        bot.setY(nextMoveY+1);

        System.out.println(bot.getY());



        if(bot.getEnergy() < (Integer)weaponCapabilities.get("consumeEnergy")){
            bot.incrementEnergy(10);
        } else {
            ReflectionUtil.invokeMethodByTrait(strategyInstance, "attack", null);
        }

        // The energy should increment regularly, according to the docs
        bot.incrementEnergy(5);
    }
}