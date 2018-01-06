package fr.unice.miage.pa.plugins.strategies;

import fr.unice.miage.pa.plugins.Plugin;
import fr.unice.miage.pa.plugins.PluginTrait;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

@Plugin(name="Strategy", type="core", required=1)
public class Strategy {
    private Object monitored;
    private Object attacked;
    private HashMap weaponCapabilities;

    public Strategy(Object monitored, Object attacked, HashMap weaponCapabilities){
        this.attacked = attacked;
        this.monitored = monitored;
        this.weaponCapabilities = weaponCapabilities;
    }

    @PluginTrait(type="attack", on="robot")
    public void attack() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Method getterMonitored = monitored.getClass().getDeclaredMethod("getX");
        //Call to invoke return value !
        int monitoredX = (Integer) getterMonitored.invoke(monitored);

        Method getterAttacked = attacked.getClass().getDeclaredMethod("getX");
        int attackedX = (Integer) getterAttacked.invoke(attacked);

        System.out.println("Monitored X is " + monitoredX);
        System.out.println("Attacked X is "+ attackedX);
        // check x attacked & x monitored -> if same -> attack
    }

    public String printAnything(){

        return "Print anything";
    }
}
