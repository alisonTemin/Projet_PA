package unice.miage.pa.engine;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.SecureClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
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



