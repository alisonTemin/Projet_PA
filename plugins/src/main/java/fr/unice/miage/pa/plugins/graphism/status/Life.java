package fr.unice.miage.pa.plugins.graphism.status;

import fr.unice.miage.pa.plugins.Plugin;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Corentin on 04/01/2018.
 */
@Plugin(name="Life", type="core", required=1)
public class Life {
    private JPanel panel;

    public Life(JPanel panel){
        this.panel = panel;
    }

    private Object callGetOnRobot(String getterName, Object robot) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getter = robot.getClass().getDeclaredMethod(getterName);
        return getter.invoke(robot);
    }

    public void drawLife(Object robot) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        int x = (Integer) this.callGetOnRobot("getX", robot);
        int y = (Integer) this.callGetOnRobot("getY", robot);
        int health = (Integer) this.callGetOnRobot("getHealth", robot);


        JLabel vie = new JLabel("");
        vie.setOpaque(true);
        vie.setBounds(new Rectangle(x, y  , health, 10));
        vie.setBackground(Color.green);
        panel.add(vie);

    }


}