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
    private int energy;
    private int x;
    private int y;
    private JLabel label;
    private Object weapon;
    private Object strategy;

    /**
     * Robot constructor
     * @param health : Starting health points
     */
    public Robot(String name, int health, int energy, int x, int y){
        this.name = name;
        this.health = health;
        this.energy = energy;
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

    public int getEnergy() { return energy; }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getName() {
        return name;
    }

    public void setLabel(Object label) {
        this.label = (JLabel) label;
    }

    /**
     * Set a bot health points
     * @param health next health points total
     */
    public void decrementHealth(int health) {
        this.health = this.health - health;
    }

    public void incrementHealth(int health) {
        this.health = this.health + health;
    }

    public void decrementEnergy(int energy){
        this.energy = this.energy - energy;
    }

    public void incrementEnergy(int energy){
        this.energy = this.energy + energy;
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

    public void setStrategy(Object strategy) {
        this.strategy = strategy;
    }

    public Object getStrategy() {
        return strategy;
    }
}
