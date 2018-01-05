package fr.unice.miage.pa.plugins.attacks.weapons;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Weapon {
    int distance();
    int consumeEnergy();
    int baseAttack();
    int criticalAttack();
}
