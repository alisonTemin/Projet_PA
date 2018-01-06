package fr.unice.miage.pa.plugins.graphism;

import fr.unice.miage.pa.plugins.Plugin;
import fr.unice.miage.pa.plugins.PluginTrait;
import fr.unice.miage.pa.plugins.attacks.weapons.Weapons;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;

@Plugin(name="Graphism", type="core", required=1)
public class Graphism {

    private JPanel panel;

    public Graphism(JPanel panel){
        this.panel = panel;
    }

    /**
     * Draw a robot on frame
     * @param robot : robot to draw
     */
    @PluginTrait(type="drawRobot", on="robot")
    public JLabel drawRobot(Object robot) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String botName = (String) this.callGetOnRobot("getName", robot);

        int x = (Integer) this.callGetOnRobot("getX", robot);
        int y = (Integer) this.callGetOnRobot("getY", robot);

        JLabel robotLabel = new JLabel("");
        robotLabel.setOpaque(true);
        Random rand = new Random();

        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();

        Color robotColor = new Color(r, g, b);
        robotLabel.setBackground(robotColor);
        robotLabel.setBounds(new Rectangle(x, y+30 , 25, 25));

        this.panel.add(robotLabel);

        this.panel.repaint();

        return robotLabel;
     }

    @PluginTrait(type="move", on="robot")
    public void moveRobot(JLabel label, int x) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        label.setLocation(x,label.getY());
    }

    @PluginTrait(type="drawWeapon", on="robot")
     public JLabel drawWeapon(Object robot, Weapons weapon, boolean isAtLeft) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        int x = (Integer) this.callGetOnRobot("getX", robot);
        int y = (Integer) this.callGetOnRobot("getY", robot);

        int weaponX = x - 70;

        if(isAtLeft){
            weaponX = x + 70;
        }
        JLabel weaponLabel = null;

        InputStream fileStream;
         if (weapon.equals(Weapons.Sword)) {
             fileStream = this.getResourceURL("sword.png");

             try {
                 weaponLabel = this.makeImageComponent(fileStream, weaponX, y, isAtLeft);
                 this.panel.add(weaponLabel);
             } catch (IOException e) {
                 e.printStackTrace();
             }
         }
         else if(weapon.equals(Weapons.Gun)){
             fileStream = this.getResourceURL("gun.png");
             try {
                 weaponLabel = this.makeImageComponent(fileStream, weaponX, y, isAtLeft);
                 this.panel.add(weaponLabel);
             } catch (IOException e) {
                 e.printStackTrace();
             }

         }
         else if(weapon.equals(Weapons.MachineGun)){
             fileStream = this.getResourceURL("machineGun.png");

             try {
                 weaponLabel = this.makeImageComponent(fileStream, weaponX, y, isAtLeft);
                 this.panel.add(weaponLabel);
             } catch (IOException e) {
                 e.printStackTrace();
             }

         }

         return weaponLabel;
     }

     private JLabel makeImageComponent(InputStream input, int x, int y, boolean invert) throws IOException {
        return this.makeImageComponent(input, x, y, 200, 200, invert);
     }

     private JLabel makeImageComponent(InputStream input, int x, int y, int width, int height, boolean invert) throws IOException {
         BufferedImage image = ImageIO.read(input);

         if(!invert){
             AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
             tx.translate(-image.getWidth(null), 0);
             AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
             image = op.filter(image, null);
         }

         ImageIcon weapon = new ImageIcon(image);
         JLabel label = new JLabel(weapon);
         label.setBounds(x, y, width, height);
         return label;
     }

    /**
     * @return URI
     */
     private InputStream getResourceURL(String resourceName){
         ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

         return classLoader.getResourceAsStream(resourceName);
     }

    private Object callGetOnRobot(String getterName, Object robot) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getter = robot.getClass().getDeclaredMethod(getterName);
        return getter.invoke(robot);
    }

    private Object callSetOnRobot(String setterName, Object robot, Object param) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method setter = robot.getClass().getDeclaredMethod(setterName, Object.class);
        return setter.invoke(robot, param);
    }
}