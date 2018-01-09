package fr.unice.miage.pa.plugins.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PluginUtil {

    /**
     * Invoke a method using a PluginTrait
     *
     * @param pluginInstance instance to invoke on
     * @param type "draw"
     *
     * @throws InvocationTargetException Instance target not good, probably a copy paste error
     * @throws IllegalAccessException Method is protected / private
     */
    public static Object getMethodUsingTrait(Object pluginInstance, String type) throws InvocationTargetException, IllegalAccessException {
        Method[] methods = pluginInstance.getClass().getMethods();

        for(Method mt : methods) {
            for(Annotation annot : mt.getAnnotations()){
                Class<? extends Annotation> trait = annot.annotationType();

                for (Method method : trait.getDeclaredMethods()) {
                    if(method.invoke(annot).equals(type)){
                        return mt;
                    }
                }
            }
        }

        return null;
    }

}
