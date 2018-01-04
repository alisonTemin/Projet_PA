package fr.unice.miage.pa.plugins.graphism;

import fr.unice.miage.pa.plugins.graphism.IRobot;
import fr.unice.miage.pa.plugins.attacks.weapons.Weapons;

public interface IGraphism {
    public void drawRobot(final IRobot robot);
    public void drawWeapon(IRobot robot, Weapons weapon);
}
