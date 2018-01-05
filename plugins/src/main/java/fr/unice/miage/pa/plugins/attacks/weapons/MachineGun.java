package fr.unice.miage.pa.plugins.attacks.weapons;

import fr.unice.miage.pa.plugins.attacks.core.Attack;
import fr.unice.miage.pa.plugins.attacks.core.IAttack;

@Weapon(distance = 100, consumeEnergy = 50, criticalAttack = 300, baseAttack = 100)
public class MachineGun implements IAttack {
    @Attack(healthPoints = 50, energyPoints = 40)
    public int critical() {
        return 300;
    }

    @Attack(healthPoints = 50, energyPoints = 20)
    public int base() {
        return 100;
    }
}
