package fr.unice.miage.pa.plugins.attacks.weapons;

import fr.unice.miage.pa.plugins.attacks.core.IAttack;

public class Gun implements IAttack {
    public int critical() {
        return 50;
    }

    public int base() {
        return 10;
    }
}
