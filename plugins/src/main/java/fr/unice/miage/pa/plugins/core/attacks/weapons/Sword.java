package fr.unice.miage.pa.plugins.core.attacks.weapons;

import fr.unice.miage.pa.plugins.core.attacks.core.Attack;
import fr.unice.miage.pa.plugins.core.attacks.core.IAttack;

/**
 * A Sword
 * use AttackAnnotation
 */

@Weapon(distance = 10, consumeEnergy = 30, baseAttack = 5, criticalAttack = 30)
public class Sword implements IAttack {

    @Attack(healthPoints = 10, energyPoints = 40)
    public int critical(){
        // Make a critical attack using Sword
        return 30;
    }

    @Attack(healthPoints = 0, energyPoints = 10)
    public int base(){
        // Base attack using sword
        return 5;
    }
}
