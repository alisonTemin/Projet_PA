package fr.unice.miage.pa.plugins.attacks.weapons;

import fr.unice.miage.pa.plugins.attacks.core.Attack;

@Weapon(consumeEnergy = 50, criticalAttack = 300, baseAttack = 100)
public class MachineGun extends Gun {
    @Attack(healthPoints = 50, energyPoints = 40)
    public int critical() {
        return 300;
    }

    @Attack(healthPoints = 50, energyPoints = 20)
    public int base() {
        return 100;
    }
}
