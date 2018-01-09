package fr.unice.miage.pa.plugins.core.attacks.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
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
