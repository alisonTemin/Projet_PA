package unice.miage.pa.Plugins.Graphism.Core;

import unice.miage.pa.Elements.Robot;

import javax.swing.*;

public interface IGraphism {
    public void drawRobot(JPanel panel, final Robot robot);
    public void drawWeapon(Robot robot);
}
