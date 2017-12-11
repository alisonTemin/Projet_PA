package unice.miage.pa.Plugins.Attacks;

public @interface SwordAnnotation {
    int critical() default 0;
    int normalAttack() default 20;
}