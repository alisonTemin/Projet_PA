package unice.miage.pa.Plugins.Attacks.Core;

import unice.miage.pa.Plugins.Attacks.Weapons.*;
import unice.miage.pa.Plugins.Plugin;

import java.util.HashMap;
import java.util.Random;
import unice.miage.pa.Plugins.Attacks.Weapons.Weapons;

@Plugin(name="Graphism", required=1)
public class Attacks {

    private HashMap<Weapons, IAttack> weapons;

    public Attacks() {
        HashMap<Weapons, IAttack> weapons = new HashMap<Weapons, IAttack>();
        Sword s = new Sword();
        Gun g = new Gun();
        MachineGun mg = new MachineGun();
        weapons.put(Weapons.Sword, s);
        weapons.put(Weapons.Gun, g);
        weapons.put(Weapons.MachineGun, mg);
        this.weapons = weapons;
    }

    /**
     * Get every weapons described in Weapons enum
     * @return Hashmap containing every name/weapon
     */
    public HashMap<Weapons, IAttack> getWeapons(){
        return this.weapons;
    }

    /**
     *
     * @return base attack point of a random weapon in hashmap
     */
    public int getAnyAttackBasePoints(){
        Weapons[] weapons = Weapons.values();
        int rnd = new Random().nextInt(weapons.length);

        IAttack randomAttack = this.weapons.get(weapons[rnd]);
        return randomAttack.base();
    }

}
