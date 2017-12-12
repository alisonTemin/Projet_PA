package unice.miage.pa.Plugins;

public @interface Plugin {
    /**
     * Define if attack is critical or not
     * @return int 0:1
     */
    String name();

    /**
     * Define attack time
     * @return time to complete attack
     */
    int required() default 0;
}
