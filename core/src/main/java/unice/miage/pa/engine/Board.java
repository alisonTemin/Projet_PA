package unice.miage.pa.engine;

import unice.miage.pa.elements.Robot;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class representing a board game
 *
 */
public class Board {

    private ArrayList<Robot> robots;

    public Board(){
        this.robots = new ArrayList<>();
    }

    public void addBot(Robot robot){
        this.robots.add(robot);
    }

    public Robot getRobotByName(String name){
        for(Robot r : this.robots){
            if(r.getName().equals(name))
                return r;
        }

        return null;
    }

    public ArrayList<Robot> getRobots() {
        return this.robots;
    }
}
