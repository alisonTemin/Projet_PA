package unice.miage.pa.Plugins.Graphism.Core;

import unice.miage.pa.Elements.Robot;
import unice.miage.pa.Plugins.Plugin;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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
       JPanel robotPanel = new JPanel() {
             @Override
             public void paintComponent(Graphics g) {
                 super.paintComponent(g);
                 g.setColor(colorRandom);
                 g.fillRect(robot.getX(), robot.getY(), 30, 30);
                 // Write his name
                 g.drawString(robot.getName(), robot.getX()-10, robot.getY()-10);
             }
        };
        // Append on main JFrame
        panel.add(robotPanel);
     }

     public void drawWeapon(){
         BufferedImage image = null;
         try {
             image = ImageIO.read(new File("images/images.jpg"));
            ImageIcon myImage =  new ImageIcon(image);
            panel.add(new JLabel(myImage));
         } catch (IOException e) {
         }


     }


}