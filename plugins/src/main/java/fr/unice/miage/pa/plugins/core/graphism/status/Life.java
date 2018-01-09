package fr.unice.miage.pa.plugins.core.graphism.status;

import fr.unice.miage.pa.plugins.core.annotations.Plugin;
import fr.unice.miage.pa.plugins.core.annotations.PluginTrait;
import fr.unice.miage.pa.plugins.core.utils.PluginUtil;

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

        this.bar.setForeground(Color.black);
        this.bar.setText(name + " : " + String.valueOf(health));

        if(health == 0) {
            this.bar.setForeground(Color.white);
            this.bar.setBackground(Color.red);
            this.bar.setText(name + " (Dead)");
        }


        this.panel.repaint();
    }

    private Object callGetOnRobot(String getterName, Object robot) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return PluginUtil.getterOnBot(getterName, robot).invoke(robot);
    }
}
