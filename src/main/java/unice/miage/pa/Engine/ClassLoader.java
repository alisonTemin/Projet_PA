package unice.miage.pa.Engine;


import java.io.*;
import java.security.SecureClassLoader;
import java.util.ArrayList;

public class ClassLoader extends SecureClassLoader {

    ArrayList<File> path = new ArrayList<File>();
    private String name;

    public ClassLoader(ArrayList<File> path) {
        this.path = path;
    }

    private Class<?> loadPlugin(String name) throws ClassNotFoundException {
        name = name.replace('.', '\\');
        Class<? extends File> result = null;
        for (File p : path) {
            result = p.getClass();
        }
        return result;
    }




    public static void main(String[] args) throws ClassNotFoundException, IOException {

        ArrayList<File> path = new ArrayList<File>();
        path.add(new File("unice.miage.pa.Plugins.Attacks.Core.Attacks"));
        ClassLoader classe = new ClassLoader(path);
        Class<?> maClasse = classe.loadPlugin("unice.miage.pa.Plugins.Attacks.Core.Attacks");
        System.out.println(maClasse);

    }

    }



