package fr.unice.miage.pa.plugins.core.graphism;

import fr.unice.miage.pa.plugins.core.annotations.Plugin;
import fr.unice.miage.pa.plugins.core.annotations.PluginTrait;
import fr.unice.miage.pa.plugins.core.utils.PluginUtil;

import javax.swing.*;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;

@Plugin(name="Graphism", type="core", required=1)
public class Graphism {

    private final JPanel panel;

    public Graphism(JPanel panel){
        this.panel = panel;
    }

    /**
     * Draw a robot on frame
     * @param robot : robot to draw
     */
    @PluginTrait(type="drawRobot", on="robot")
    public JLabel drawRobot(Object robot) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
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
    public void moveRobot(JLabel label, int x,int y) {
        label.setLocation(x,y+1);
    }

    private Object callGetOnRobot(String getterName, Object robot) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return PluginUtil.getterOnBot(getterName, robot).invoke(robot);
    }
}