package fr.unice.miage.pa.plugins.custom;

import fr.unice.miage.pa.plugins.core.annotations.Plugin;
import fr.unice.miage.pa.plugins.core.annotations.PluginOverride;

@Plugin(name="SampleCustomStrategy", type="core", required=1)
public class SampleCustomStrategy {
    @PluginOverride(name = "moveTo", on = "strategy")
    private void moveTo(Object attacked) throws Exception {
    }

    @PluginOverride(name = "attack", on = "strategy")
    public void attack() throws Exception {

    }
}
