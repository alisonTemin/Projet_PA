package unice.miage.pa.plugins.graphism.core;

import unice.miage.pa.elements.Robot;
import unice.miage.pa.plugins.Plugin;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Plugin(name="Graphism", required=1)
public class Graphism implements IGraphism {

    private JPanel panel;

    public Graphism(JPanel panel){
        this.panel = panel;
    }
    private Color colorRandom = new Color((int)(Math.random() * 0x1000000));

    /**
     * Draw a robot on frame
     * @param robot : robot to draw
     */
    public void drawRobot(JPanel panel, final Robot robot) {
        // create our new robot
       final JPanel robotPanel = new JPanel(null) {
             @Override
             public void paintComponent(Graphics g) {
                 super.paintComponent(g);
                 g.setColor(colorRandom);
                 g.fillRect(robot.getX(), robot.getY(), 30, 30);
                 // Write his name
                 g.drawString(robot.getName(), robot.getX()-10, robot.getY()-10);
             }
        };
        robot.setPanel(robotPanel);
        // Append on main JFrame
        panel.add(robotPanel);
     }

     public void drawWeapon(Robot robot){
         ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
         URI uri = null;
         try {
             uri =  classLoader.getResource("sword.png").toURI();
         } catch (URISyntaxException e) {
             e.printStackTrace();
         }

         try {
             assert uri != null;

             BufferedImage image = ImageIO.read(new File(uri));
             ImageIcon weapon = new ImageIcon(image);
             JLabel label = new JLabel(weapon);
             label.setLocation(0, 0);
             this.panel.add(label);
         } catch (IOException e) {
             e.printStackTrace();
         }
     }


}