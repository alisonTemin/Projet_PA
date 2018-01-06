package unice.miage.pa.engine;

import unice.miage.pa.elements.Robot;

import java.util.ArrayList;

/**
 * Class representing a board game
 *
 */
public class Board {

    private ArrayList<Robot> robots;

    public Board(){
        this.robots = new ArrayList<Robot>();
    }

    public boolean addBot(Robot robot){
        return robots.add(robot);
    }

    public boolean removeBot(Robot robot){
        return robots.remove(robot);
    }

    public ArrayList<Robot> getRobots() {
        return robots;
    }

    public void setRobots(ArrayList<Robot> robots) {
        this.robots = robots;
    }
}
