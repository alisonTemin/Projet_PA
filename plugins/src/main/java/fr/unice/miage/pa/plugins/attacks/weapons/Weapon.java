package fr.unice.miage.pa.plugins.attacks.weapons;

public @interface Weapon {
    int consumeEnergy();
    int baseAttack();
    int criticalAttack();
}
