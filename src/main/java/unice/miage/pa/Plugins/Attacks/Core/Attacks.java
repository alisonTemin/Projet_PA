package unice.miage.pa.Plugins.Attacks.Core;

import unice.miage.pa.Plugins.Attacks.Weapons.Weapons;
import unice.miage.pa.Plugins.Plugin;

@Plugin(name="Attacks")
public class Attacks {
    /**
     * Get every weapons described in Weapons enum
     * @return Weapons array
     */
    public Weapons[] getWeapons(){
        return Weapons.values();
    }

}
