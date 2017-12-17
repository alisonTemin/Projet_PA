package unice.miage.pa.plugins.graphism.core;

import unice.miage.pa.elements.Robot;
import unice.miage.pa.plugins.Plugin;

import javax.imageio.ImageIO;
import javax.swing.*;
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

    /**
     * Draw a robot on frame
     * @param robot : robot to draw
     */
    public void drawRobot(final Robot robot) {
        URI uri = this.getResourceURL(robot.getName().toLowerCase() + ".png");

        try {
            this.panel.add(this.makeImageComponent(uri, robot.getX(), robot.getY()));
        } catch (IOException e) {
            e.printStackTrace();
        }
     }

     public void drawWeapon(Robot robot){
         URI uri = this.getResourceURL("sword.png");

         try {
             this.panel.add(this.makeImageComponent(uri, robot.getX()+70, robot.getY()));
         } catch (IOException e) {
             e.printStackTrace();
         }
     }

     private JLabel makeImageComponent(URI uri, int x, int y) throws IOException {
        return this.makeImageComponent(uri, x, y, 200, 200);
     }

     private JLabel makeImageComponent(URI uri, int x, int y, int width, int height) throws IOException {
         BufferedImage image = ImageIO.read(new File(uri));
         ImageIcon weapon = new ImageIcon(image);
         JLabel label = new JLabel(weapon);
         label.setBounds(x, y, width, height);
         return label;
     }

    /**
     * @return URI
     */
     private URI getResourceURL(String resourceName){
         ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
         try {
             return classLoader.getResource(resourceName).toURI();
         } catch (URISyntaxException e) {
             return null;
         }
     }


}