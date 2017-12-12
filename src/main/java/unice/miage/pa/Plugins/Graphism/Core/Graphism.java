package unice.miage.pa.Plugins.Graphism.Core;

import unice.miage.pa.Engine.Robot;
import unice.miage.pa.Plugins.Plugin;

import javax.swing.*;
import java.awt.*;

@Plugin(name="Graphism", required=1)
public class Graphism implements IGraphism {

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
        
     }

}