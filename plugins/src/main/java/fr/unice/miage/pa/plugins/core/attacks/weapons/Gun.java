package fr.unice.miage.pa.plugins.core.attacks.weapons;

import fr.unice.miage.pa.plugins.core.attacks.core.Attack;
import fr.unice.miage.pa.plugins.core.attacks.core.IAttack;

@Weapon(distance = 50, consumeEnergy = 10, baseAttack = 10, criticalAttack = 50)
public class Gun implements IAttack {
    @Attack(healthPoints = 10, energyPoints = 40)
    public int critical() {
        return 50;
    }

    @Attack(healthPoints = 0, energyPoints = 20)
    public int base() {
        return 10;
    }
}
