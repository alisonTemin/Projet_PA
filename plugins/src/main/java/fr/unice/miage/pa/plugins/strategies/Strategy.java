package fr.unice.miage.pa.plugins.strategies;

import fr.unice.miage.pa.plugins.Plugin;
import fr.unice.miage.pa.plugins.PluginTrait;

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


        int weaponDistance = (Integer) weaponCapabilities.get("distance");
        // check x attacked & x monitored
        if((monitoredX + weaponDistance > attackedX) || (monitoredX - weaponDistance < attackedX) && ( (Integer)attacked.getClass().getDeclaredMethod("getHealth").invoke(attacked) <= 0)){
            int consumeLife = (Integer) weaponCapabilities.get("baseAttack");
            int consumeEnergy = (Integer) weaponCapabilities.get("consumeEnergy");

            Method setterLife = attacked.getClass().getDeclaredMethod("setHealth",int.class);
            Method getterLife = attacked.getClass().getDeclaredMethod("getHealth");
            int health = (Integer)getterLife.invoke(attacked);

            setterLife.invoke(attacked, health - consumeLife);
        }
    }
}
