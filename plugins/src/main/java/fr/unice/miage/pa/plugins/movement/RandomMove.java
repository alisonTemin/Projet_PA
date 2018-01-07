package fr.unice.miage.pa.plugins.movement;

import fr.unice.miage.pa.plugins.Plugin;
import fr.unice.miage.pa.plugins.PluginTrait;

import java.util.Random;

@Plugin(name="random", type="movement")
public class RandomMove {

    @PluginTrait(type="move", on="robot")
    public int nextPlace(){
        Random generator = new Random();
        return generator.nextInt(300) + 1;
    }
}
