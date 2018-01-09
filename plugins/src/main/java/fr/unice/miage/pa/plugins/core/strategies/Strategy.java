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

@Plugin(name="Strategy", type="core", required=1)
public class Strategy {
    private final String name;
    private final Object monitored;
    private final ArrayList opponents;
    private final HashMap weaponCapabilities;
    private final HashMap<String, Class<?>> plugins;
    private Integer nextMoveY;
    private Integer nextMoveX;

    public Strategy(Object monitored, ArrayList opponents, HashMap weaponCapabilities, HashMap<String, Class<?>> plugins){
        this.opponents = opponents;
        this.monitored = monitored;
        this.weaponCapabilities = weaponCapabilities;
        this.plugins = plugins;
        this.name = "Base";
    }

    @PluginTrait(type="decide", on="robot")
    @PluginOverridable(name="decide", on="strategy")
    public Object decide() throws Exception {
        String name = (String) this.getterOnBot("getName", this.monitored).invoke(monitored);
        int monitoredX = (Integer) this.getterOnBot("getX", monitored).invoke(monitored);

        for(Object attacked : this.opponents){
            if (moveBasics(name, monitoredX, attacked)) continue;

            if((Integer) this.getterOnBot("getHealth", attacked).invoke(attacked) > 0) {
                this.moveTo(attacked);

                int consumeEnergy = (Integer) weaponCapabilities.get("consumeEnergy");
                Method setterEnergy = this.methodOnBot("decrementEnergy", monitored, int.class);
                setterEnergy.invoke(monitored, consumeEnergy);

                return attacked;
            }
        }

        // 404 Bot not found
        return null;
    }

    private boolean moveBasics(String name, int monitoredX, Object attacked) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        int maybeAttackedX = (Integer) this.getterOnBot("getX", attacked).invoke(attacked);
        String maybeAttackedName = (String) this.getterOnBot("getName", attacked).invoke(attacked);
        if(maybeAttackedName.equals(name))
            return true;

        if(maybeAttackedX > monitoredX + (Integer) weaponCapabilities.get("distance"))
            return true;
        return false;
    }

    @PluginTrait(type="movements", on="strategy")
    public void movements() throws InvocationTargetException, IllegalAccessException {
        Method move = (Method) PluginUtil.getMethodUsingTrait(plugins.get("RandomMove"), "move");
        Method moveY = (Method) PluginUtil.getMethodUsingTrait(plugins.get("RandomMove"), "moveY");

        this.nextMoveX = (Integer) move.invoke(plugins.get("RandomMove"));
        this.nextMoveY = (Integer) moveY.invoke(plugins.get("RandomMove"));
    }

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

    @PluginTrait(type="nextMove", on="strategy")
    public void processNextMove(int opponentX) throws Exception {
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

    @PluginTrait(type="strategyName", on="strategy")
    public String getName(){
        return this.name;
    }

    @PluginTrait(type="attack", on="opponent")
    public void attack(Object attacked) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        int consumeLife = (Integer) weaponCapabilities.get("baseAttack");

        Method setterLife = this.methodOnBot("decrementHealth", attacked, int.class);

        setterLife.invoke(attacked, consumeLife);
    }

    private Method getterOnBot(String getterName, Object bot) throws NoSuchMethodException {
        return bot.getClass().getDeclaredMethod(getterName);
    }

    private Method methodOnBot(String setterName, Object bot, Class clazz) throws NoSuchMethodException {
        return bot.getClass().getDeclaredMethod(setterName, clazz);
    }
}
