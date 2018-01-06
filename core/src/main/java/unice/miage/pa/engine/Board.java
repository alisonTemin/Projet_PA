package unice.miage.pa.engine;

import unice.miage.pa.elements.Robot;

import java.util.HashMap;

/**
 * Class representing a board game
 *
 */
public class Board {

    private HashMap<String, Robot> robots;

    public Board(){
        this.robots = new HashMap<>();
    }

    public void addBot(Robot robot){
        this.robots.put(robot.getName(), robot);
    }

    public void removeBot(Robot robot){
        this.robots.remove(robot.getName());
    }

    public Robot getRobotByName(String name){
        return this.robots.get(name);
    }

    public HashMap<String, Robot> getRobots() {
        return this.robots;
    }
}
