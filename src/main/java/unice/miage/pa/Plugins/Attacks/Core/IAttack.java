package unice.miage.pa.Plugins.Attacks.Core;

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
