package unice.miage.pa.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public class ReflectionUtil {
    /**
     * Invoke a method using a PluginTrait
     *
     * @param pluginInstance instance to invoke on
     * @param type "draw"
     * @param on "robot"
     * @param args ... Args array (TODO : Refactor, it's crappy, but working)
     *
     * @throws InvocationTargetException Instance target not good, probably a copy paste error
     * @throws IllegalAccessException Method is protected / private
     */
    public static Object invokeMethodByTrait(Object pluginInstance, String type, Object on, Object... args) throws InvocationTargetException, IllegalAccessException {
        Method[] methods = pluginInstance.getClass().getMethods();

        // TODO : If loaded plugins contains custom strategy override invoke

        for(Method mt : methods) {
            for(Annotation annot : mt.getAnnotations()){
                Class<? extends Annotation> trait = annot.annotationType();

                for (Method method : trait.getDeclaredMethods()) {
                    if(method.invoke(annot).equals(type)){

                        if(args.length == 1)
                            return mt.invoke(pluginInstance, on, args[0]);
                        else if(args.length == 2)
                            return mt.invoke(pluginInstance, on, args[0], args[1]);
                        else if(args.length == 3)
                            return mt.invoke(pluginInstance, on, args[0], args[1], args[2]);
                        else if(on == null)
                            return mt.invoke(pluginInstance);
                        else
                            return mt.invoke(pluginInstance, on);
                    }
                }
            }
        }

        return null;
    }

    /**
     *
     * @param pluginClass pluginClass
     * @return Object
     *
     * @throws NoSuchMethodException Constructor not found
     * @throws IllegalAccessException Constructor protected / private
     * @throws InvocationTargetException Wrong target class
     * @throws InstantiationException Probably a param mismatch
     */
    public static Object __construct(Class pluginClass) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        @SuppressWarnings("unchecked") // Just to shut down ij warn, i know it's risky, it's a reflectionUtil \o/
        Constructor constructor = pluginClass.getDeclaredConstructor();
        return constructor.newInstance();
    }

    /**
     * Constructor with one object
     * @param pluginClass pluginClass
     * @param arg param
     * @return Object
     *
     * @throws NoSuchMethodException Constructor not found
     * @throws IllegalAccessException Constructor protected / private
     * @throws InvocationTargetException Wrong target class
     * @throws InstantiationException Probably a param mismatch
     */
    public static Object __construct(Class pluginClass, Object arg) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        @SuppressWarnings("unchecked")
        Constructor constructor = pluginClass.getDeclaredConstructor(arg.getClass());

        return constructor.newInstance(arg);
    }

    /**
     * Construct a new strategy instance
     * @param pluginClass StrategyInstance
     * @param arg two bots one hashmap of weapon capabilities
     * @return StrategyObject
     *
     * @throws NoSuchMethodException Constructor not found
     * @throws IllegalAccessException Constructor protected / private
     * @throws InvocationTargetException Wrong target class
     * @throws InstantiationException Probably a param mismatch
     */
    public static Object __constructStrategy(Class pluginClass, Object... arg) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        @SuppressWarnings("unchecked")
        Constructor constructor = pluginClass.getDeclaredConstructor(Object.class, ArrayList.class, HashMap.class, HashMap.class);
        //noinspection JavaReflectionInvocation IJ static analysis cry
        return constructor.newInstance(arg);
    }
}
