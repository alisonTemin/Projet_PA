package unice.miage.pa.plugins.attacks.core;

public interface IAttack {

    /**
     * Describe a critical attack using weapons who implements IAttack
     */
    int critical();

    /**
     * Describe a basic attack
     */
    int base();
}
