package unice.miage.pa.boardMonitor;

import fr.unice.miage.pa.plugins.Plugin;
import fr.unice.miage.pa.plugins.PluginTrait;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.util.ArrayList;

import unice.miage.pa.engine.Board;

public class BoardMonitor implements ActionListener{
    private Board board;
    private int time;

    public BoardMonitor(Board board)
    {
        this.board = board;
    }

    public void startGame(){
        System.out.println(board.getRobots());
        boolean dead = false;

            while (!dead)
            {
                for (int i = 0; i < board.getRobots().size(); i++){
                    System.out.println(board.getRobots().get(i).getHealth());
                    dead = true;
            }

        }
    }





    public  void    actionPerformed(ActionEvent e)
    {

    }



}