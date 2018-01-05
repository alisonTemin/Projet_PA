package fr.unice.miage.pa.plugins.strategies;

import fr.unice.miage.pa.plugins.Plugin;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Plugin(name="Strategy", type="core", required=1)
public class Strategy {
    Object monitored;
    Object attacked;
    Object attack;

    public Strategy(Object monitored, Object attacked){
        this.attacked = attacked;
        this.monitored = monitored;
        this.attack = attack;
    }

    public void attack() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Method getterMonitored = monitored.getClass().getDeclaredMethod("getX");
        getterMonitored.invoke(monitored);
        Method getterAttacked = attacked.getClass().getDeclaredMethod("getX");
        getterAttacked.invoke(attacked);
        Method getterAttack = attack.getClass().getDeclaredMethod("getAnyAttackBasePoints");
        if(getterMonitored == getterAttacked){
        getterAttack.invoke(attack);
        }
        // check x attacked & x monitored -> if same -> attack
    }

    public String printAnything(){

        return "Print anything";
    }
}
