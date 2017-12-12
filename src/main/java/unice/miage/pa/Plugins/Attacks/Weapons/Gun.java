package unice.miage.pa.Plugins.Attacks.Weapons;

import unice.miage.pa.Plugins.Attacks.Core.IAttack;

public class Gun implements IAttack {
    public int critical() {
        return 50;
    }

    public int base() {
        return 10;
    }
}
