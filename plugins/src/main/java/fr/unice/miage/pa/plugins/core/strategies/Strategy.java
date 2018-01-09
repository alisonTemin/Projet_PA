package fr.unice.miage.pa.plugins.core.strategies;

import fr.unice.miage.pa.plugins.core.annotations.Plugin;
import fr.unice.miage.pa.plugins.core.annotations.PluginOverridable;
import fr.unice.miage.pa.plugins.core.annotations.PluginTrait;
import fr.unice.miage.pa.plugins.core.utils.PluginUtil;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Generic strategy
 *
 * PluginOverridable -> Overridable in a custom strategy
 */
@Plugin(name="Strategy", type="core", required=1)
public class Strategy {
    private final String name;
    private final Object monitored;
    private final ArrayList opponents;
    private final HashMap weaponCapabilities;
    private final HashMap<String, Class<?>> plugins;
    private Integer nextMoveY;
    private Integer nextMoveX;

    /**
     * Strategy constructor.
     * @param monitored Monitored bot
     * @param opponents Opponents array
     * @param weaponCapabilities Weapon
     * @param plugins Plugins
     */
    public Strategy(Object monitored, ArrayList opponents, HashMap weaponCapabilities, HashMap<String, Class<?>> plugins){
        this.opponents = opponents;
        this.monitored = monitored;
        this.weaponCapabilities = weaponCapabilities;
        this.plugins = plugins;
        this.name = "Base";
    }

    /**
     * Decide movement strategy
     *
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @PluginTrait(type="movements", on="strategy")
    @PluginOverridable(name="movements", on="strategy")
    public void movements() throws InvocationTargetException, IllegalAccessException {
        Method move = (Method) PluginUtil.getMethodUsingTrait(plugins.get("RandomMove"), "move");
        Method moveY = (Method) PluginUtil.getMethodUsingTrait(plugins.get("RandomMove"), "moveY");

        assert move != null;
        this.nextMoveX = (Integer) move.invoke(plugins.get("RandomMove"));
        assert moveY != null;
        this.nextMoveY = (Integer) moveY.invoke(plugins.get("RandomMove"));
    }

    /**
     * Decide of the strategy to attack
     * @return
     * @throws Exception
     */
    @PluginTrait(type="decide", on="robot")
    @PluginOverridable(name="decide", on="strategy")
    public Object decide() throws Exception {
        Double random = (Math.random() * this.opponents.size());

        // Grab someone
        Object closest = this.opponents.get(random.intValue());

        for(Object attacked : this.opponents){

            // Grab health of maybe closest guy
            int closestLife = (Integer) this.getterOnBot("getHealth", attacked).invoke(attacked);

            // if he is alive
            if(closestLife > 0) {
                int attackedX = (Integer)this.getterOnBot("getX", attacked).invoke(attacked);
                int closestX = (Integer)this.getterOnBot("getX", closest).invoke(closest);

                // He is my opponent if my X is upper of attacked
                if(closestX > attackedX + 20)
                    closest = attacked;
            }
        }

        // 404 Bot not found
        return closest;
    }

    /**
     * Get strategy name
     *
     * Todo : refactor : avoid code repeat in sample strategy ?
     * @return Base
     */
    @PluginTrait(type="strategyName", on="strategy")
    public String getName(){
        return this.name;
    }

    @PluginTrait(type="consume", on="monitored")
    public void consume() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        int consumeEnergy = (Integer) weaponCapabilities.get("consumeEnergy");
        this.methodOnBot("decrementEnergy", monitored, int.class).invoke(monitored, consumeEnergy);
    }

    /**
     * Decide if can attack or not ( by monitor to avoid cheat ;) )
     * @param attacked
     * @return
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @PluginTrait(type="couldAttack", on="opponent")
    public boolean couldAttack(Object attacked) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        int monitoredX = (Integer) this.getterOnBot("getX", monitored).invoke(monitored);

        return checks(name, monitoredX, attacked);
    }

    /**
     * Move to attacked
     * @param attacked
     * @return
     * @throws Exception
     */
    @PluginTrait(type="moveTo", on="strategy")
    public int moveTo(Object attacked) throws Exception {
        int opponentX = (Integer) this.getterOnBot("getX", attacked).invoke(attacked);
        int opponentY = (Integer) this.getterOnBot("getY", attacked).invoke(attacked);

        int monitoredX = (Integer) this.getterOnBot("getX", monitored).invoke(monitored);
        int monitoredY = (Integer) this.getterOnBot("getY", monitored).invoke(monitored);

        // Avoid stacking in upper right
        if(monitoredX < 20 || monitoredY < 20){
            monitoredX = 50;
            monitoredY = 50;
        }

        int nextY = monitoredY + nextMoveY;

        // Borders
        if(monitoredY > opponentY){
            // He is down of attacked
            nextY = monitoredX - nextMoveX;
        }

        if(monitoredY < opponentY){
            nextY = monitoredY + nextMoveY;
        }
        this.methodOnBot("setX", monitored, int.class).invoke(monitored, monitoredX);
        this.methodOnBot("setY", monitored, int.class).invoke(monitored, nextY);

        this.processNextMove(opponentX);

        return opponentX;
    }

    /**
     * Basic checking
     *
     * @param name
     * @param monitoredX
     * @param attacked
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    @PluginTrait(type="checks", on="strategy")
    private boolean checks(String name, int monitoredX, Object attacked) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        int maybeAttackedX = (Integer) this.getterOnBot("getX", attacked).invoke(attacked);
        int maybeAttackedLife = (Integer) this.getterOnBot("getHealth", attacked).invoke(attacked);

        int monitoredEnergy = (Integer) this.getterOnBot("getEnergy", attacked).invoke(attacked);

        String maybeAttackedName = (String) this.getterOnBot("getName", attacked).invoke(attacked);
        if (maybeAttackedName.equals(name))
            return false;

        if (maybeAttackedLife == 0)
            return false;

        return  monitoredEnergy > (Integer) weaponCapabilities.get("consumeEnergy") ||
                maybeAttackedX > (monitoredX + (Integer) weaponCapabilities.get("distance"));

    }

    /**
     * Process the next.. move
     * @param opponentX
     * @throws Exception
     */
    @PluginTrait(type="nextMove", on="strategy")
    private void processNextMove(int opponentX) throws Exception {
        JLabel label = (JLabel) this.getterOnBot("getLabel", monitored).invoke(monitored);
        int direction = (Integer) this.getterOnBot("getDirection", monitored).invoke(monitored);
        int monitoredX = (Integer) this.getterOnBot("getX", monitored).invoke(monitored);
        int monitoredY = (Integer) this.getterOnBot("getY", monitored).invoke(monitored);

        Method moveInGraphics = (Method) PluginUtil.getMethodUsingTrait(plugins.get("Graphism"), "move");

        if(moveInGraphics == null){
            System.out.println("Graphism plugin not loaded");
            throw new Exception("Graphism not loaded");
        }
        if(direction == 0){
            int nextX = monitoredX + nextMoveX;
            if(monitoredX > opponentX){
                nextX = monitoredX - nextMoveX;
            }
            this.methodOnBot("setX", monitored, int.class).invoke(monitored, nextX);
            monitoredX = (Integer) this.getterOnBot("getX", monitored).invoke(monitored);
        }

        if(direction == 1){
            int nextX = monitoredX - nextMoveX;
            if(monitoredX < opponentX){
                nextX = monitoredX + nextMoveX;
            }
            this.methodOnBot("setX", monitored, int.class).invoke(monitored, nextX);
            monitoredX = (Integer) this.getterOnBot("getX", monitored).invoke(monitored);
        }
        moveInGraphics.invoke(plugins.get("Graphism"), label, monitoredX, monitoredY);
    }

    /**
     * Attack generic
     * @param attacked
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @PluginTrait(type="attack", on="opponent")
    public void attack(Object attacked) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        int consumeLife = (Integer) weaponCapabilities.get("baseAttack");

        Method setterLife = this.methodOnBot("decrementHealth", attacked, int.class);

        setterLife.invoke(attacked, consumeLife);
    }

    /**
     * Generic get on bot
     * @param getterName
     * @param bot
     * @return getter method
     * @throws NoSuchMethodException
     */
    private Method getterOnBot(String getterName, Object bot) throws NoSuchMethodException {
        return bot.getClass().getDeclaredMethod(getterName);
    }

    /**
     * Get a method on bot
     * @param methodName method name
     * @param bot bot object
     * @param clazz int.class for example
     * @return method
     * @throws NoSuchMethodException
     */
    private Method methodOnBot(String methodName, Object bot, Class clazz) throws NoSuchMethodException {
        return bot.getClass().getDeclaredMethod(methodName, clazz);
    }
}
