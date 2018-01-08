package fr.unice.miage.pa.plugins.strategies;

import fr.unice.miage.pa.plugins.Plugin;
import fr.unice.miage.pa.plugins.PluginTrait;
import fr.unice.miage.pa.plugins.utils.PluginUtil;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

@Plugin(name="Strategy", type="core", required=1)
public class Strategy {
    private final Object monitored;
    private final Object attacked;
    private final HashMap weaponCapabilities;
    private final HashMap<String, Class<?>> plugins;

    public Strategy(Object monitored, Object attacked, HashMap weaponCapabilities, HashMap<String, Class<?>> plugins){
        this.attacked = attacked;
        this.monitored = monitored;
        this.weaponCapabilities = weaponCapabilities;
        this.plugins = plugins;
    }

    @PluginTrait(type="attack", on="robot")
    public void attack() throws Exception {
        Method move = (Method) PluginUtil.getMethodUsingTrait(plugins.get("RandomMove"), "move");
        Method moveY = (Method) PluginUtil.getMethodUsingTrait(plugins.get("RandomMove"), "moveY");

        Method getterLabel = this.getterOnBot("getLabel", monitored);
        JLabel label = (JLabel) getterLabel.invoke(monitored);

        int nextMoveX = (Integer) move.invoke(plugins.get("RandomMove"));
        int nextMoveY = (Integer) moveY.invoke(plugins.get("RandomMove"));

        int monitoredX = (Integer) this.getterOnBot("getX", monitored).invoke(monitored);
        int monitoredY = (Integer) this.getterOnBot("getY", monitored).invoke(monitored);
        int attackedX = (Integer) this.getterOnBot("getX", attacked).invoke(attacked);
        int attackedY = (Integer) this.getterOnBot("getY", attacked).invoke(attacked);

        int direction = (Integer) this.getterOnBot("getDirection", monitored).invoke(monitored);

        Method moveInGraphics = (Method) PluginUtil.getMethodUsingTrait(plugins.get("Graphism"), "move");

        if(moveInGraphics == null){
            System.out.println("Graphism plugin not loaded");
            throw new Exception("Graphism not loaded");
        }

        this.methodOnBot("setY", monitored, int.class).invoke(monitored, monitoredY + nextMoveY);
        monitoredY = (Integer) this.getterOnBot("getY", monitored).invoke(monitored);
        
        if(direction == 0){
            int nextX = monitoredX + nextMoveX;
            if(monitoredX > attackedX){
                nextX = monitoredX - nextMoveX;
            }
            this.methodOnBot("setX", monitored, int.class).invoke(monitored, nextX);
            monitoredX = (Integer) this.getterOnBot("getX", monitored).invoke(monitored);
        }

        if(direction == 1){
            int nextX = monitoredX - nextMoveX;
            if(monitoredX < attackedX){
                nextX = monitoredX + nextMoveX;
            }
            this.methodOnBot("setX", monitored, int.class).invoke(monitored, nextX);
            monitoredX = (Integer) this.getterOnBot("getX", monitored).invoke(monitored);
        }
        moveInGraphics.invoke(plugins.get("Graphism"), label, monitoredX, monitoredY);

        if(( (Integer)this.getterOnBot("getHealth", attacked).invoke(attacked) <= 0) && ((monitoredY + 10 > attackedY) || (monitoredY - 10 < attackedY))) {
            int consumeLife = (Integer) weaponCapabilities.get("baseAttack");

            Method setterLife = this.methodOnBot("decrementHealth", attacked, int.class);

            setterLife.invoke(attacked, consumeLife);
        }

        int consumeEnergy = (Integer) weaponCapabilities.get("consumeEnergy");
        Method setterEnergy = this.methodOnBot("decrementEnergy", monitored, int.class);
        setterEnergy.invoke(monitored, consumeEnergy);
    }

    private Method getterOnBot(String getterName, Object bot) throws NoSuchMethodException {
        return bot.getClass().getDeclaredMethod(getterName);
    }

    private Method methodOnBot(String setterName, Object bot, Class clazz) throws NoSuchMethodException {
        return bot.getClass().getDeclaredMethod(setterName, clazz);
    }
}
