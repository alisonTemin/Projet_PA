package fr.unice.miage.pa.plugins.custom;

import fr.unice.miage.pa.plugins.core.annotations.Plugin;
import fr.unice.miage.pa.plugins.core.annotations.PluginTrait;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

@Plugin(name="SampleCustomStrategy", type="core", required=1)
public class SampleCustomStrategy {
    private final String name;
    private final Object monitored;
    private final ArrayList opponents;
    private final HashMap weaponCapabilities;
    private final HashMap<String, Class<?>> plugins;
    private Integer nextMoveY;
    private Integer nextMoveX;

    public SampleCustomStrategy(Object monitored, ArrayList opponents, HashMap weaponCapabilities, HashMap<String, Class<?>> plugins){
        this.opponents = opponents;
        this.monitored = monitored;
        this.weaponCapabilities = weaponCapabilities;
        this.plugins = plugins;
        this.name = "SampleCustomStrategy";
    }
    @PluginTrait(type="movements", on="strategy")
    public void movements() throws InvocationTargetException, IllegalAccessException {
        // Implement random strategy by setting this.nextMove values
        // core plugin RandomMove could be used

        /* example :
        Object randomMove = plugins.get("RandomMove");
        this.nextMoveX = (Method) PluginUtil.getMethodUsingTrait(randomMove, "move").invoke(randomMove);
        this.nextMoveY = (Method) PluginUtil.getMethodUsingTrait(randomMove, "moveY").invoke(randomMove);
        */
    }

    @PluginTrait(type="decide", on="robot")
    public Object decide() throws Exception {
        // Implement decide strategy
        // Iterate over this.opponents check health for example
        // Return wanted bot

        // 404 Bot not found atm
        return null;
    }

    /**
     * Get strategy name
     * @return Base
     */
    @PluginTrait(type="strategyName", on="strategy")
    public String getName(){
        return this.name;
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
