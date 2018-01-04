package fr.unice.miage.pa.plugins.graphism.status;

import fr.unice.miage.pa.plugins.Plugin;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Plugin(name="Energy", type="core", required=1)
public class Energy {
    private JPanel panel;

    public Energy(JPanel panel){
        this.panel = panel;
    }


    private Object callGetOnRobot(String getterName, Object robot) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method getter = robot.getClass().getDeclaredMethod(getterName);
        return getter.invoke(robot);
    }

    public void drawEnergy(Object robot) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        int x = (Integer) this.callGetOnRobot("getX", robot);
        int y = (Integer) this.callGetOnRobot("getY", robot);
        int health = (Integer) this.callGetOnRobot("getHealth", robot);


        JLabel energie = new JLabel("");
        energie.setOpaque(true);
        energie.setBounds(new Rectangle(x, y + 10 , health, 10));
        energie.setBackground(Color.blue);
        panel.add(energie);
    }

}
