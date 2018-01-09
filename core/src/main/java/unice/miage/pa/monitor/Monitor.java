package unice.miage.pa.monitor;

import java.awt.*;
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
    private final JFrame frame;

    private Object graphismInstance;
    private final HashMap<String, Object> plugins;
    private ArrayList<Robot> players;

    private final ArrayList<Object> strategies;
    private final HashMap<String, Robot> off;
    private boolean propagation;

    /**
     * Contains every plugin dependencies identified by plugin name
     */
    private final HashMap<String, Object> dependencies;

    /**
     * Monitor constructor
     * @param board Game board
     */
    public Monitor(Board board, JPanel panel, JFrame frame) {
        this.board = board;
        this.frame = frame;
        this.plugins = new HashMap<>();
        this.dependencies = new HashMap<>();
        this.players = new ArrayList<>();
        this.strategies = new ArrayList<>();
        this.off = new HashMap<>();
        this.panel = panel;
        this.propagation = true;
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

            // First is at left
            Robot fakePlayer = null;
            if(i > 0 && i % 2 != 0){
                // He is at right
                x = x + 200;
                fakePlayer = new Robot(result, 100,100, x, y, 1);

            }

            if(i % 2 == 0 && i != 0){
                // He is at left
                x = 10;
                y = y + 100;
            }

            if(fakePlayer == null)
                fakePlayer = new Robot(result, 100,100, x, y, 0);

            this.board.addBot(fakePlayer);
        }

        ArrayList<Robot> bots = this.board.getRobots();

        for(Robot bot : bots){
            Object strategy = ReflectionUtil.__constructStrategy((Class)plugins.get("Strategy"), bot, bots, weaponCapabilities, this.plugins);

            if(this.customs().size() != 0){
                System.out.println("Custom strategy loaded for " + bot.getName());
                strategy = ReflectionUtil.__constructStrategy((Class) this.customs().values().toArray()[0], bot, bots, weaponCapabilities, this.plugins);
            }

            bot.setStrategy(strategy);
            this.strategies.add(strategy);
        }
    }

    private HashMap<String, Class> customs(){
        HashMap<String, Class> customs = new HashMap<>();
        for(String customName : this.plugins.keySet()){
            if(customName.startsWith("Custom")){
                customs.put(customName, (Class)plugins.get(customName));
            }
        }
        return customs;
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
        JFrame barFrame= new JFrame();
        barFrame.setSize(new Dimension(400, 300));
        barFrame.setVisible(true);
        barFrame.setLayout(new GridLayout(3, 2));
        barFrame.setLocation(400, 300);
        barFrame.setTitle("Life / Energy");

        // Drawing our bots
        // TODO : If loaded plugin Graphism..
        StringBuilder playerNames = new StringBuilder("Players : " + this.players.size() + "\n");
        for(Robot robot : this.players){
            playerNames.append("\n Setup ")
                       .append(robot.getName())
                       .append(" with strategy : ")
                       .append(ReflectionUtil.invokeMethodByTrait(robot.getStrategy(), "strategyName", null));

            this.setupBot(robot, barFrame);
        }

        mainFrame.setSize( 400,this.players.size() * 60);

        this.startWar(playerNames.toString());
    }

    private void startWar(String playerNames) throws InvocationTargetException, IllegalAccessException, InterruptedException {
        System.out.println("War started \n" + playerNames);

        while (this.propagation) {
            if(this.checkGameEnd()){
                System.out.println("Game has ended");
                this.propagation = false;
            }

            HashMap weaponCapabilities = (HashMap) ClassLoader.annotationValues(plugins.get("Sword"));

            for(Robot bot : this.players) {
                this.launchBot(bot, weaponCapabilities, bot.getStrategy());
            }

            Thread.sleep(100);
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
    private void setupBot(Robot robot, JFrame barFrame) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        // Draw bot
        Object botLabel = ReflectionUtil.invokeMethodByTrait(this.graphismInstance, "drawRobot", robot);
        robot.setLabel(botLabel);

        JPanel barPanel = new JPanel();
        barPanel.setVisible(true);

        // Draw bars
        Object statusLifeBot = ReflectionUtil.__construct((Class)plugins.get("Life"), barPanel);
        Object energyLifeBot = ReflectionUtil.__construct((Class)plugins.get("Energy"), barPanel);
        ReflectionUtil.invokeMethodByTrait(statusLifeBot, "draw", robot);
        ReflectionUtil.invokeMethodByTrait(energyLifeBot, "draw", robot);
        barFrame.add(barPanel);
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
            Pattern energyOrLifeBar = Pattern.compile("Life(.*)|Energy(.*)");
            Matcher matcher = energyOrLifeBar.matcher(pluginName);

            while (matcher.find()) {
                String botName = matcher.group(1);

                // If not capturing group 1, it's a lifeBar
                if(botName == null)
                    botName = matcher.group(2);

                Robot toUpdate = this.board.getRobotByName(botName);
                ReflectionUtil.invokeMethodByTrait(plugins.get(pluginName), "update", toUpdate);
                this.frame.repaint();
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
        if(bot.getEnergy() < (Integer)weaponCapabilities.get("consumeEnergy")){
            bot.incrementEnergy(10);
        } else if(bot.getHealth() > 0) {
            ReflectionUtil.invokeMethodByTrait(strategyInstance, "movements", null);
            Object attacked = ReflectionUtil.invokeMethodByTrait(strategyInstance, "decide", null);
            if(attacked != null){
                ReflectionUtil.invokeMethodByTrait(strategyInstance, "moveTo", attacked);
                if((Boolean)ReflectionUtil.invokeMethodByTrait(strategyInstance, "couldAttack", attacked)){
                    ReflectionUtil.invokeMethodByTrait(strategyInstance, "attack", attacked);
                }
                ReflectionUtil.invokeMethodByTrait(strategyInstance, "consume", null);
            }
        }

        // The energy should increment regularly, according to the docs
        bot.incrementEnergy(5);
        this.updateBars();
    }

    private boolean checkGameEnd() {
        int playersLeft = 0;
        Robot winner = null;

        for(Robot bot : this.players){
            if(bot.getHealth() == 0 && !this.off.containsKey(bot.getName())){
                this.panel.remove(bot.getLabel());
                this.off.put(bot.getName(), bot);
            }
            winner = bot;

            if(bot.getHealth() != 0)
                playersLeft++;
        }

        if(playersLeft == 1){
            System.out.println(winner.getName() + " wins");
            return true;
        }

        return false;
    }
}