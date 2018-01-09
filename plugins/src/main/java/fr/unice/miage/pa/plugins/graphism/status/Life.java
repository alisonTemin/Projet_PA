package fr.unice.miage.pa.plugins.graphism.status;

import fr.unice.miage.pa.plugins.annotations.Plugin;
import fr.unice.miage.pa.plugins.annotations.PluginTrait;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


@Plugin(name = "Life", type="core", required = 1)
public class Life {
    private JLabel bar;
    private JPanel panel;


    public Life(JPanel panel){
        this.panel = panel;
    }

    private Object callGetOnRobot(String getterName, Object robot) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getter = robot.getClass().getDeclaredMethod(getterName);
        return getter.invoke(robot);
    }

    @PluginTrait(type="draw", on="robot")
    public void drawLife(Object robot) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        int x = (Integer) this.callGetOnRobot("getX", robot);
        int y = (Integer) this.callGetOnRobot("getY", robot);
        int health = (Integer) this.callGetOnRobot("getHealth", robot);

        this.bar = new JLabel(String.valueOf(health));
        this.bar.setOpaque(true);
        this.bar.setBounds(new Rectangle(x, y+60, health, 10));
        this.bar.setBackground(Color.GREEN);

        this.panel.add(this.bar);
        this.panel.repaint();
    }

    @PluginTrait(type="update", on="robot")
    public void updateLife(Object robot) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        int health = (Integer) this.callGetOnRobot("getHealth", robot);
        String name = (String) this.callGetOnRobot("getName", robot);

        this.bar.setText(name + " : " + String.valueOf(health));
        this.bar.setForeground(Color.GRAY);

        this.panel.repaint();
    }


}
