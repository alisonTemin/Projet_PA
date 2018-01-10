package fr.unice.miage.pa.plugins.custom;

import fr.unice.miage.pa.plugins.core.annotations.Plugin;
import fr.unice.miage.pa.plugins.core.annotations.PluginTrait;
import fr.unice.miage.pa.plugins.core.utils.PluginUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

@Plugin(name="SampleCustomStrategy", type="strategy")
public class SampleCustomStrategy {
    private final String name;
    private final Object monitored;
    private final ArrayList opponents;
    private final HashMap weaponCapabilities;
    private final HashMap<String, Class<?>> plugins;
    private final Object movement;
    private Integer nextMoveY;
    private Integer nextMoveX;

    public SampleCustomStrategy(Object monitored, ArrayList opponents, HashMap weaponCapabilities, HashMap<String, Class<?>> plugins, Object moveInstance){
        this.opponents = opponents;
        this.monitored = monitored;
        this.weaponCapabilities = weaponCapabilities;
        this.plugins = plugins;
        this.movement = moveInstance;
        this.name = "SampleCustomStrategy";
    }

    @PluginTrait(type="movements", on="strategy")
    public void movements() throws InvocationTargetException, IllegalAccessException {
        // Implement random strategy by setting this.nextMove values
        // core plugin RandomMove could be used
        Method move = (Method) PluginUtil.getMethodUsingTrait(plugins.get("RandomMove"), "move");
        Method moveY = (Method) PluginUtil.getMethodUsingTrait(plugins.get("RandomMove"), "moveY");
        Object randomMove = plugins.get("RandomMove");

        this.nextMoveX = (Integer) (move != null ? move.invoke(randomMove) : null);
        this.nextMoveY = (Integer) (moveY != null ? moveY.invoke(randomMove) : null);
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
}
