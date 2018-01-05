package unice.miage.pa.elements;

import javax.swing.*;

/**
 * A robot
 */
public class Robot {

    /**
     * Robot name
     */
    private String name;
    /**
     * Health points
     */
    private int health;
    private int x;
    private int y;
    private JLabel label;
    private Object weapon;

    /**
     * Robot constructor
     * @param health : Starting health points
     */
    public Robot(String name, int health, int x, int y){
        this.name = name;
        this.health = health;
        this.x = x;
        this.y = y;
    }

    /**
     * Get health points
     * @return int health points
     */
    public int getHealth() {
        return health;
    }

    /**
     * Set a bot health points
     * @param health next health points total
     */
    public void setHealth(int health) {
        this.health = health;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getName() {
        return name;
    }

    public void setLabel(Object panel) {
        this.label = label;
    }

    public JLabel getLabel() {
        return this.label;
    }

    public void setWeapon(Object weapon) {
        this.weapon = weapon;
    }

    public void setX(int x) {
        this.x = x;
    }
}
