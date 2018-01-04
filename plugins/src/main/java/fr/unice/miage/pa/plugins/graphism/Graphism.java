package fr.unice.miage.pa.plugins.graphism;

import fr.unice.miage.pa.plugins.Plugin;
import fr.unice.miage.pa.plugins.attacks.weapons.Weapons;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

@Plugin(name="Graphism", type="core", required=1)
public class Graphism implements IGraphism {

    private JPanel panel;

    public Graphism(JPanel panel){
        this.panel = panel;
    }

    /**
     * Draw a robot on frame
     * @param robot : robot to draw
     */
    public void drawRobot(final IRobot robot) {
        InputStream robotImage = this.getResourceURL(robot.getName().toLowerCase() + ".png");

        try {
            this.panel.add(this.makeImageComponent(robotImage, robot.getX(), robot.getY()));
        } catch (IOException e) {
            e.printStackTrace();
        }
     }

    public JPanel getPanelRobot(){
        return this.panel;
    }

     public void drawWeapon(IRobot robot, Weapons weapon) {
        InputStream fileStream;
         if (weapon.equals(Weapons.Sword)) {
             fileStream = this.getResourceURL("sword.png");

             try {
                 this.panel.add(this.makeImageComponent(fileStream, robot.getX() + 70, robot.getY()));
             } catch (IOException e) {
                 e.printStackTrace();
             }
         }
         else if(weapon.equals(Weapons.Gun)){
             fileStream = this.getResourceURL("gun.png");
             try {
                 this.panel.add(this.makeImageComponent(fileStream, robot.getX() + 70, robot.getY()));
             } catch (IOException e) {
                 e.printStackTrace();
             }

         }
         else if(weapon.equals(Weapons.MachineGun)){
             fileStream = this.getResourceURL("machineGun.png");

             try {
                 this.panel.add(this.makeImageComponent(fileStream, robot.getX() + 90, robot.getY()));
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

     public void moveRobot(final Object robot, int x, int y){
         JPanel position = getPanelRobot();
         position.setLocation(x,y);
     }

     public void drawStats(IRobot robot) {
         JLabel vie = new JLabel("");
         vie.setOpaque(true);
         vie.setBounds(new Rectangle((int)robot.getX(), (int)robot.getY()  , robot.getHealth(), 10));
         vie.setBackground(Color.green);
         panel.add(vie);

         JLabel energie = new JLabel("");
         energie.setOpaque(true);
         energie.setBounds(new Rectangle((int)robot.getX(), (int)robot.getY() + 10 , robot.getHealth(), 10));
         energie.setBackground(Color.blue);
         panel.add(energie);
     }


}