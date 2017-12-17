package unice.miage.pa.plugins.graphism.core;

import unice.miage.pa.elements.Robot;

import javax.swing.*;

public interface IGraphism {
    public void drawRobot(JPanel panel, final Robot robot);
    public void drawWeapon(Robot robot);
}
