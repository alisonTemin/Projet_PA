package fr.unice.miage.pa.plugins.core.movement;

import fr.unice.miage.pa.plugins.core.annotations.Plugin;
import fr.unice.miage.pa.plugins.core.annotations.PluginOverridable;
import fr.unice.miage.pa.plugins.core.annotations.PluginTrait;

import java.util.Random;

@Plugin(name="random", type="movement")
public class RandomMove {

    @PluginTrait(type="move", on="robot")
    @PluginOverridable(name="move", on="strategy")
    public int nextPlace(){
        Random generator = new Random();
        return generator.nextInt(30) + 1;
    }
    @PluginTrait(type="moveY", on="robot")
    @PluginOverridable(name="moveY", on="strategy")
    public int nextPlaceY(){
        Random generator = new Random();
        return generator.nextInt(10) +1 ;
    }
}
