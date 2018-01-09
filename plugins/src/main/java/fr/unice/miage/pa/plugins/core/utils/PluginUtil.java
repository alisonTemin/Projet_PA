package fr.unice.miage.pa.plugins.core.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PluginUtil {

    /**
     * Generic get on bot
     * @param getterName
     * @param bot
     * @return getter method
     * @throws NoSuchMethodException
     */
    public static Method getterOnBot(String getterName, Object bot) throws NoSuchMethodException {
        return bot.getClass().getDeclaredMethod(getterName);
    }

    /**
     * Get a method on bot
     * @param methodName method name
     * @param bot bot object
     * @param clazz int.class for example
     * @return method
     * @throws NoSuchMethodException
     */
    public static Method methodOnBot(String methodName, Object bot, Class clazz) throws NoSuchMethodException {
        return bot.getClass().getDeclaredMethod(methodName, clazz);
    }

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
