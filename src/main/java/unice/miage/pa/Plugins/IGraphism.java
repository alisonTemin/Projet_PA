package unice.miage.pa.Plugins;

import unice.miage.pa.Engine.Robot;

import javax.swing.*;

public interface IGraphism {
    public void drawRobot(JPanel panel, final Robot robot);
    public void drawWeapon();
}
