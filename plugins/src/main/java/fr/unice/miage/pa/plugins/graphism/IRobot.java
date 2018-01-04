package fr.unice.miage.pa.plugins.graphism;

import javax.swing.*;

/**
 * A robot
 */
public interface IRobot {
    /**
     * Get health points
     * @return int health points
     */
    int getHealth();

    /**
     * Set a bot health points
     * @param health next health points total
     */
    void setHealth(int health);

    int getX();

    int getY();

    String getName();

    void setPanel(JPanel panel);

    JPanel getPanel();
}
