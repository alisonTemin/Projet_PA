package unice.miage.pa.Elements;

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
    private JPanel panel;

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

    public void setPanel(JPanel panel) {
        this.panel = panel;
    }

    public JPanel getPanel() {
        return panel;
    }
}
