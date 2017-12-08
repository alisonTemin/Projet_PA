package fr.unice.miage;

import javax.swing.*;
import java.awt.*;

class PluginGraphisme implements IGraphique{

     Color colorRandom = new Color((int)(Math.random() * 0x1000000));
     public void dessinerRobot() {
         JFrame frame = new JFrame();
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         frame.setVisible(true);
         frame.setSize(600, 400);
         JPanel panel = new JPanel() {
             @Override
             public void paintComponent(Graphics g) {
                 super.paintComponent(g);
                 g.setColor(colorRandom);
                 g.fillRect(30, 30, 30, 30);
             }
         };
         frame.add(panel);
         frame.validate();
         frame.repaint();
     }

     public void dessinerArme(){
        
     }

     public static void main(String[] args){
         PluginGraphisme pg = new PluginGraphisme();
         pg.dessinerRobot();
     }

        }