package unice.miage.pa.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;

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

        for(Method mt : methods) {
            for(Annotation annot : mt.getAnnotations()){
                Class<? extends Annotation> trait = annot.annotationType();

                for (Method method : trait.getDeclaredMethods()) {
                    if(method.invoke(annot).equals(type)){

                        if(args.length > 0)
                            System.out.println("Invoke " + type + " | with : "+ Arrays.toString(args));
                        else if(on != null)
                            System.out.println("Invoke " + type + " | on : " + pluginInstance + " | with :" + on);
                        else
                            System.out.println("Invoke " + type);

                        if(args.length == 1)
                            return mt.invoke(pluginInstance, on, args[0]);
                        else if(args.length == 2)
                            return mt.invoke(pluginInstance, on, args[0], args[1]);
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
        Constructor constructor = pluginClass.getDeclaredConstructor(Object.class, Object.class, HashMap.class);
        return constructor.newInstance(arg);
    }
}
