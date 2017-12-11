package unice.miage.pa.Engine;

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

    /**
     * Robot constructor
     * @param health : Starting health points
     */
    public Robot(String name, int health){
        this.name = name;
        this.health = health;
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

    public String getName() {
        return name;
    }
}
