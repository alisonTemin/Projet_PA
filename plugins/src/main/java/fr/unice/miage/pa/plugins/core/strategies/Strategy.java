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
    private final Object monitored;
    private final ArrayList opponents;
    private final HashMap weaponCapabilities;
    private final HashMap<String, Class<?>> plugins;

    public Strategy(Object monitored, ArrayList opponents, HashMap weaponCapabilities, HashMap<String, Class<?>> plugins){
        this.opponents = opponents;
        this.monitored = monitored;
        this.weaponCapabilities = weaponCapabilities;
        this.plugins = plugins;
    }

    @PluginTrait(type="attack", on="robot")
    @PluginOverridable(name="attack", on="strategy")
    public void attack() throws Exception {
        String name = (String) this.getterOnBot("getName", this.monitored).invoke(monitored);
        int monitoredX = (Integer) this.getterOnBot("getX", monitored).invoke(monitored);

        for(Object attacked : this.opponents){
            int maybeAttackedX = (Integer) this.getterOnBot("getX", attacked).invoke(attacked);
            String maybeAttackedName = (String) this.getterOnBot("getName", attacked).invoke(attacked);
            if(maybeAttackedName.equals(name))
                continue;

            if(maybeAttackedX > monitoredX + (Integer) weaponCapabilities.get("distance"))
                continue;

            if((Integer) this.getterOnBot("getHealth", attacked).invoke(attacked) > 0) {
                this.moveTo(attacked);
                this.attack(attacked);
            }
        }

        int consumeEnergy = (Integer) weaponCapabilities.get("consumeEnergy");
        Method setterEnergy = this.methodOnBot("decrementEnergy", monitored, int.class);
        setterEnergy.invoke(monitored, consumeEnergy);
    }

    @PluginOverridable(name="moveTo", on="strategy")
    private void moveTo(Object attacked) throws Exception {
        int opponentX = (Integer) this.getterOnBot("getX", attacked).invoke(attacked);
        int opponentY = (Integer) this.getterOnBot("getY", attacked).invoke(attacked);

        Method move = (Method) PluginUtil.getMethodUsingTrait(plugins.get("RandomMove"), "move");
        Method moveY = (Method) PluginUtil.getMethodUsingTrait(plugins.get("RandomMove"), "moveY");

        Method getterLabel = this.getterOnBot("getLabel", monitored);
        JLabel label = (JLabel) getterLabel.invoke(monitored);

        int nextMoveX = (Integer) move.invoke(plugins.get("RandomMove"));
        int nextMoveY = (Integer) moveY.invoke(plugins.get("RandomMove"));

        int monitoredX = (Integer) this.getterOnBot("getX", monitored).invoke(monitored);
        int monitoredY = (Integer) this.getterOnBot("getY", monitored).invoke(monitored);

        int direction = (Integer) this.getterOnBot("getDirection", monitored).invoke(monitored);

        Method moveInGraphics = (Method) PluginUtil.getMethodUsingTrait(plugins.get("Graphism"), "move");

        if(moveInGraphics == null){
            System.out.println("Graphism plugin not loaded");
            throw new Exception("Graphism not loaded");
        }
        int nextY = monitoredY + nextMoveY;
        if(monitoredX < 0){
            monitoredX = 100;
        }
        if(monitoredY < 0){
            monitoredY = 200;
        }
        // Borders
        if(monitoredY > opponentY){
            // He is down of attacked
            nextY = monitoredX - nextMoveX;
        }

        if(monitoredY < opponentY){
            nextY = monitoredY + nextMoveY;
        }

        this.methodOnBot("setY", monitored, int.class).invoke(monitored, nextY);
        monitoredY = (Integer) this.getterOnBot("getY", monitored).invoke(monitored);

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

    private void attack(Object attacked) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
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
