package unice.miage.pa.Engine;

import unice.miage.pa.Elements.Robot;

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
}
