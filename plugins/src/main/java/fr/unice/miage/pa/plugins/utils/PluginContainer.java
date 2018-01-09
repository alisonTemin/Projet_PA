package fr.unice.miage.pa.plugins.utils;

import fr.unice.miage.pa.plugins.annotations.Plugin;
import fr.unice.miage.pa.plugins.annotations.PluginTrait;

import java.util.HashMap;

@Plugin(name="PluginContainer", type="core", required=1)
public class PluginContainer {

    private final HashMap<String, Class<?>> plugins;

    public PluginContainer(HashMap<String, Class<?>> plugins){
        this.plugins = plugins;
    }

    @PluginTrait(type="get", on="plugins")
    public Class<?> getPlugin(String key){
        return this.plugins.get(key);
    }
}
