package unice.miage.pa.Plugins.Attacks.Core;

public interface IAttack {

    /**
     * Describe a critical attack using weapons who implements IAttack
     */
    public void critical();

    /**
     * Describe a basic attack
     */
    public void base();
}
