package unice.miage.pa.plugins.graphism.core;

import unice.miage.pa.elements.Robot;
import unice.miage.pa.plugins.attacks.weapons.Weapons;

public interface IGraphism {
    public void drawRobot(final Robot robot);
    public void drawWeapon(Robot robot, Weapons weapon);
}
