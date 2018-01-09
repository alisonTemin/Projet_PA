package unice.miage.pa.engine;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.SecureClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ClassLoader extends SecureClassLoader {

    /**
     * Load a plugin from a .class file (if annotation complies...)
     * @param plugin plugin file
     * @return plugin Class object
     */
    private Class<?> loadPluginFromFile(File plugin) {
        String pluginName = plugin.getAbsolutePath().replace(".class", "");
        pluginName = pluginName.replace("/", ".");

        // Handle windows paths
        pluginName = pluginName.replace("\\", ".");

        int fr = pluginName.indexOf("fr");
        pluginName = pluginName.substring(fr, pluginName.length());

        // Blacklist annotations loading
        if(pluginName.equals("fr.unice.miage.pa.plugins.core.annotations.Plugin") || pluginName.equals("fr.unice.miage.pa.plugins.core.attacks.weapons.Weapon"))
            return null;

        File file = new File(plugin.getAbsolutePath());
        if(file.exists()){
            byte[] pluginBytecode = recupTabBytes(file);
            try {
                Class<?> clazz = this.defineClass(pluginName, pluginBytecode, 0, Objects.requireNonNull(pluginBytecode).length);
                HashMap values = (HashMap) annotationValues(clazz);
                if(values.containsKey("required"))
                    return clazz;
                if(values.containsKey("distance"))
                    return clazz;
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * Get loaded plugins map
     * @param pluginsPath path to plugins .class
     * @return plugins hashMap
     */
    public HashMap<String, Class<?>> getPluginsMap(String pluginsPath) {
        File pluginsRepository = new File(pluginsPath);

        List<File> repository = findEveryPlugin(pluginsRepository, new ArrayList<>(), ".class");

        HashMap<String, Class<?>> plugins = new HashMap<>();
        for(File plugin : repository){
            try {
                Class<?> loadedPlugin = this.loadPluginFromFile(plugin);
                if(loadedPlugin != null){
                    System.out.println("Plugin loaded " + loadedPlugin.getSimpleName());
                    plugins.put(loadedPlugin.getSimpleName(), loadedPlugin);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return plugins;
    }

    /**
     * Grab every annotation value in an Hashmap identified by his name -> value
     * @param annotated annotated object to inspect
     *
     * @return HashMap of annotations (string, annot value)
     *
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static Object annotationValues(Object annotated) throws InvocationTargetException, IllegalAccessException {
        HashMap<String, Object> annotationsMap = new HashMap<>();

        Annotation[] annotations = ((Class) annotated).getAnnotations();

        for(Annotation annot : annotations){
            Class<? extends Annotation> currentAnnotation = annot.annotationType();
            for(Method method : currentAnnotation.getDeclaredMethods()){
                annotationsMap.put(method.getName(), method.invoke(annot));
            }
        }

        return annotationsMap;
    }

    /**
     * Find recursively every .class file to load
     * @param node base path
     * @param test list (writed values will be there)
     * @param suffix searched suffix
     * @return List of .class paths
     */
    private static List<File> findEveryPlugin(File node, List<File> test, String suffix) {
        assert node != null;

        if(node.isDirectory()) {
            for(File file : Objects.requireNonNull(node.listFiles())) {
                // recursively call me using the same list instance, to append in list
                findEveryPlugin(file, test, suffix);
            }
        } else if(node.isFile() && node.getName().endsWith(suffix)) {
            test.add(node);
        }

        return test;
    }

    /**
     * Grab bytes array for specified file
     * @param file file to get as an array of bytes
     *
     * @return bytes array containing our class
     */
    private byte[] recupTabBytes(File file) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FileInputStream stream = null;

        //couldn't happen, has the file exists
        try {
            stream = new FileInputStream(file.getAbsoluteFile());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        assert(stream != null);

        BufferedInputStream bis = new BufferedInputStream(stream);

        boolean eof = false;
        while (!eof) {
            try {
                int i = bis.read();
                if (i == -1)
                    eof = true;
                else
                    baos.write(i);
            } catch (IOException e) {
                return null;
            }
        }
        return baos.toByteArray();
    }

}



