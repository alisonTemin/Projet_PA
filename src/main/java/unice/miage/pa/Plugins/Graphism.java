package unice.miage.pa.Plugins;

import javax.swing.*;
import java.awt.*;
import unice.miage.pa.Engine.Robot;

public class Graphism implements IGraphism {

    private Color colorRandom = new Color((int)(Math.random() * 0x1000000));

    /**
     * Draw a robot on frame
     * @param robot : robot to draw
     */
    public void drawRobot(JFrame frame, final Robot robot) {
        // create our new robot
         JPanel panel = new JPanel() {
             @Override
             public void paintComponent(Graphics g) {
                 super.paintComponent(g);
                 g.setColor(colorRandom);
                 g.fillRect(30, 30, 30, 30);
                 // Write his name
                 g.drawString(robot.getName(), 30, 30);
             }
         };
         // Append on main JFrame
         frame.add(panel);
     }

     public void drawWeapon(){
        
     }

}