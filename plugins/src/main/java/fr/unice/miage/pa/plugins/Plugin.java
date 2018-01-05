package fr.unice.miage.pa.plugins;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
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

    /**
     * PluginType (system, enhancement, core..)
     * @return String pluginType
     */
    String type();
}
