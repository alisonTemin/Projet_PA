package unice.miage.pa.Engine;


import javafx.scene.shape.Path;

import java.awt.List;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.SecureClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import javax.imageio.IIOException;

public class ClassLoader extends SecureClassLoader {

        ArrayList<File> path = new ArrayList<File>();
        private String name;

        public ClassLoader(ArrayList<File> path) {
            this.path = path;
        }

        public ClassLoader() {
            // TODO Auto-generated constructor stub
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            byte[] b = null;
            b = loadClassData(name);
            return super.defineClass(name, b, 0, b.length);
        }

        public boolean validateZip(String pathToFile) {
            try {
                ZipFile zipFile = new ZipFile(pathToFile.toString());
                String zipname = zipFile.getName();
                return true;
            } catch (IOException e) {
                return false;
            }
        }

        public boolean validateJar(String pathtoFile) {
            try {
                JarFile jarFile = new JarFile(pathtoFile.toString());
                String jarname = jarFile.getName();
                return true;
            } catch (IOException e) {
                return false;
            }

        }

    private byte[] loadClassData(String name) throws ClassNotFoundException {
        name = name.replace('.', File.separatorChar);
        name += ".class";
        byte[] result = null;
        for (File p : path){
            System.out.println(p.getAbsolutePath()+ File.separatorChar +name);
                File fichier = new File(p.getAbsolutePath()+ File.separatorChar +name);
                if(fichier.exists()){
                    try {
                        return recupTabBytes(fichier);
                    } catch (FileNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                }
        }


        if (result == null) {
            throw new ClassNotFoundException("File not found");
        }

        return result;

    }

    public Class<?> findClassWithFile(File f) throws ClassNotFoundException {
        byte[] b = null;
        try {
            b = this.recupTabBytes(f);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return super.defineClass(null, b, 0, b.length);
    }

    // Récupération du tableau de bytes pour le fichier selectionner
    private byte[] recupTabBytes(File leFichier) throws FileNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FileInputStream fichier = new FileInputStream(leFichier.getAbsoluteFile());
        BufferedInputStream bis = new BufferedInputStream(fichier);

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

    public static void main(String[] args) throws ClassNotFoundException, IOException {

        ArrayList<File> path = new ArrayList<File>();
        path.add(new File("../Plugins/out/production/Plugins"));
        ClassLoader classe = new ClassLoader(path);
        Class<?> maClasse = classe.loadClass("fr.unice.miage.pa.plugins.Strategy");

        System.out.println(maClasse);

    }

    }



