package unice.miage.pa.plugins.attacks.weapons;

import unice.miage.pa.plugins.attacks.core.IAttack;

/**
 * A Sword
 * use AttackAnnotation
 */
public class Sword implements IAttack {

    public int critical(){
        // Make a critical attack using Sword
        return 30;
    }

    public int base(){
        // Base attack using sword
        return 5;
    }
}
