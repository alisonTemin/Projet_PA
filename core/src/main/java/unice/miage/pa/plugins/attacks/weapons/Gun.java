package unice.miage.pa.plugins.attacks.weapons;

import unice.miage.pa.plugins.attacks.core.Attack;
import unice.miage.pa.plugins.attacks.core.IAttack;

import java.lang.annotation.Annotation;

public class Gun implements IAttack {
    public int critical() {
        return 50;
    }

    public int base() {
        return 10;
    }
}
