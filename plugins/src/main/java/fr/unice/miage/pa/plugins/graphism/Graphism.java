package fr.unice.miage.pa.plugins.graphism;

import fr.unice.miage.pa.plugins.Plugin;
import fr.unice.miage.pa.plugins.PluginTrait;

import javax.swing.*;

import java.awt.*;
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

    private Object callGetOnRobot(String getterName, Object robot) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getter = robot.getClass().getDeclaredMethod(getterName);
        return getter.invoke(robot);
    }
}