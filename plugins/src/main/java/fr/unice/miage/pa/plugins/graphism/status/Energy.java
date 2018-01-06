package fr.unice.miage.pa.plugins.graphism.status;

import fr.unice.miage.pa.plugins.Plugin;
import fr.unice.miage.pa.plugins.PluginTrait;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Plugin(name="Energy", type="core", required=1)
public class Energy {
    private JPanel panel;
    private JLabel bar;

    public Energy(JPanel panel){
        this.panel = panel;
    }

    @PluginTrait(type="draw", on="robot")
    public void drawEnergy(Object robot) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        int x = (Integer) this.callGetOnRobot("getX", robot);
        int y = (Integer) this.callGetOnRobot("getY", robot);
        int energy = (Integer) this.callGetOnRobot("getEnergy", robot);

        this.bar = new JLabel("" + energy);
        this.bar.setOpaque(true);
        this.bar.setBounds(new Rectangle(x, y , energy, 10));
        this.bar.setBackground(Color.blue);

        this.panel.add(bar);
        this.panel.repaint();
    }

    @PluginTrait(type="update", on="robot")
    public void updateEnergy(Object robot) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        int energy = (Integer) this.callGetOnRobot("getEnergy", robot);

        this.bar.setText(""+energy);

        this.panel.repaint();
    }

    private Object callGetOnRobot(String getterName, Object robot) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getter = robot.getClass().getDeclaredMethod(getterName);
        return getter.invoke(robot);
    }

}
