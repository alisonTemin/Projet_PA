package fr.unice.miage.pa.plugins.attacks.core;

public @interface Attack {

    /**
     * Describe a critical attack using weapons who implements IAttack
     */
    int healthPoints();

    /**
     * Describe a basic attack
     */
    int energyPoints();
}
