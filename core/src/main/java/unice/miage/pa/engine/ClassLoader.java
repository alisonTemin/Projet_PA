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
import java.util.jar.JarFile;
import java.util.zip.ZipFile;

public class ClassLoader extends SecureClassLoader {

    ArrayList<File> path;

    private String name;

    /**
     * Classloader constructor.
     * @param path path to file
     */
    public ClassLoader(ArrayList<File> path) {
        this.path = path;
    }

    public Class<?> loadPlugin(String name) throws ClassNotFoundException {
        byte[] b = null;
        b = loadPluginData(name);
        return super.defineClass(name, b, 0, b.length);
    }

    public static List<File> findEveryPlugin(File node, List<File> test, String suffix) {
        // Cause it's crappy.
        if(node == null || test == null) System.exit(0);

        if(node.isDirectory()) {
            for(File file : node.listFiles()) {
                // recursive, beware
                findEveryPlugin(file, test, suffix);
            }
        } else if(node.isFile() && node.getName().endsWith(suffix)) {
            // Hacky but working
            test.add(node);
        }

        return test;
    }


    public boolean validateZip(String pathToFile) {
        try {
            ZipFile zipFile = new ZipFile(pathToFile);
            String zipName = zipFile.getName();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean validateJar(String pathtoFile) {
        try {
            JarFile jarFile = new JarFile(pathtoFile);
            String jarName = jarFile.getName();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public Class<?> loadPluginFromFile(File plugin) throws Exception {
        String pluginName = plugin.getAbsolutePath().replace(".class", "");
        pluginName = pluginName.replace("/", ".");

        // Handle windows paths
        pluginName = pluginName.replace("\\", ".");

        int fr = pluginName.indexOf("fr");
        pluginName = pluginName.substring(fr, pluginName.length());

        // Blacklist annotations loading
        if(pluginName.equals("fr.unice.miage.pa.plugins.Plugin") || pluginName.equals("fr.unice.miage.pa.plugins.attacks.weapons.Weapon"))
            return null;

        File file = new File(plugin.getAbsolutePath());
        if(file.exists()){
            byte[] pluginBytecode = recupTabBytes(file);
            try {
                Class<?> clazz = this.defineClass(pluginName, pluginBytecode, 0, pluginBytecode.length);
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

    private byte[] loadPluginData(String name) throws ClassNotFoundException {
        name = name.replace('.', File.separatorChar);
        name += ".class";

        for (File p : path){
            File file = new File(p.getAbsolutePath()+ File.separatorChar +name);
            if(file.exists()){
                return recupTabBytes(file);
            }
        }

        throw new ClassNotFoundException("File not found");
    }

    /**
     * Récupération du tableau de bytes pour le fichier demandé
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



