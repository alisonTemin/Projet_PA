package fr.unice.miage.pa.plugins.graphism;

import fr.unice.miage.pa.plugins.Plugin;
import fr.unice.miage.pa.plugins.PluginTrait;
import fr.unice.miage.pa.plugins.attacks.weapons.Weapons;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Plugin(name="Graphism", type="core", required=1)
public class Graphism {

    private JPanel panel;

    public Graphism(JPanel panel){
        this.panel = panel;
    }

    private Object callGetOnRobot(String getterName, Object robot) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getter = robot.getClass().getDeclaredMethod(getterName);
        return getter.invoke(robot);
    }

    /**
     * Draw a robot on frame
     * @param robot : robot to draw
     */
    @PluginTrait(type="drawRobot", on="gui")
    public void drawRobot(final Object robot) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String botName = (String) this.callGetOnRobot("getName", robot);

        int x = (Integer) this.callGetOnRobot("getX", robot);
        int y = (Integer) this.callGetOnRobot("getY", robot);

        InputStream robotImage = this.getResourceURL(botName.toLowerCase() + ".png");

        try {
            this.panel.add(this.makeImageComponent(robotImage, x, y));
        } catch (IOException e) {
            e.printStackTrace();
        }
     }

    public JPanel getPanelRobot(){
        return this.panel;
    }

    @PluginTrait(type="drawWeapon", on="gui")
     public void drawWeapon(Object robot, Weapons weapon) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        int x = (Integer) this.callGetOnRobot("getX", robot);
        int y = (Integer) this.callGetOnRobot("getY", robot);

        InputStream fileStream;
         if (weapon.equals(Weapons.Sword)) {
             fileStream = this.getResourceURL("sword.png");

             try {
                 this.panel.add(this.makeImageComponent(fileStream, x + 70, y));
             } catch (IOException e) {
                 e.printStackTrace();
             }
         }
         else if(weapon.equals(Weapons.Gun)){
             fileStream = this.getResourceURL("gun.png");
             try {
                 this.panel.add(this.makeImageComponent(fileStream, x + 70, y));
             } catch (IOException e) {
                 e.printStackTrace();
             }

         }
         else if(weapon.equals(Weapons.MachineGun)){
             fileStream = this.getResourceURL("machineGun.png");

             try {
                 this.panel.add(this.makeImageComponent(fileStream, x + 90, y));
             } catch (IOException e) {
                 e.printStackTrace();
             }

         }
     }

     private JLabel makeImageComponent(InputStream input, int x, int y) throws IOException {
        return this.makeImageComponent(input, x, y, 200, 200);
     }

     private JLabel makeImageComponent(InputStream input, int x, int y, int width, int height) throws IOException {
         BufferedImage image = ImageIO.read(input);
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

     @PluginTrait(type="move", on="robot")
     public void moveRobot(final Object robot, int x, int y){
         JPanel position = getPanelRobot();
         position.setLocation(x,y);
     }

    public void drawStats(Object robot) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
         int x = (Integer) this.callGetOnRobot("getX", robot);
         int y = (Integer) this.callGetOnRobot("getY", robot);
         int health = (Integer) this.callGetOnRobot("getHealth", robot);


         JLabel vie = new JLabel("");
         vie.setOpaque(true);
         vie.setBounds(new Rectangle(x, y  , health, 10));
         vie.setBackground(Color.green);
         panel.add(vie);

         JLabel energie = new JLabel("");
         energie.setOpaque(true);
         energie.setBounds(new Rectangle(x, y + 10 , health, 10));
         energie.setBackground(Color.blue);
         panel.add(energie);
     }


}