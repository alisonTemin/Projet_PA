package fr.unice.miage.pa.plugins.strategies;

import fr.unice.miage.pa.plugins.Plugin;

@Plugin(name="Strategy", type="core", required=1)
public class Strategy {

    public String printAnything(){
        return "Print anything";
    }

    public static void main(String[] args){

    }
}
