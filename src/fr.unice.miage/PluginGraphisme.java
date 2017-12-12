package fr.unice.miage;

import javax.swing.*;
import java.awt.*;

class PluginGraphisme implements IGraphique{

    JFrame maFenetre;

    PluginGraphisme(JFrame maFenetre){
        this.maFenetre = maFenetre;
    }

     Color colorRandom = new Color((int)(Math.random() * 0x1000000));
     public void dessinerRobot() {
         maFenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         maFenetre.setVisible(true);
         maFenetre.setSize(600, 400);
         JPanel panel = new JPanel() {
             @Override
             public void paintComponent(Graphics g) {
                 super.paintComponent(g);
                 g.setColor(colorRandom);
                 g.fillRect(30, 30, 30, 30);
             }
         };
         maFenetre.add(panel);
         maFenetre.validate();
         maFenetre.repaint();
     }

     public void dessinerArme() {
         JPanel monPanel = new JPanel() {
             @Override
             public void paintComponent(Graphics g1) {
                 g1 = maFenetre.getContentPane().getGraphics();
                 super.paintComponent(g1);
                 g1.setColor(Color.BLACK);
                 g1.fillRect(5, 5, 5, 5);
             }
         };
          maFenetre.add(monPanel);
         maFenetre.validate();
         maFenetre.repaint();
         }


     public static void main(String[] args){
         JFrame fenetre = new JFrame();
         PluginGraphisme pg = new PluginGraphisme(fenetre);
         pg.dessinerRobot();
     }

        }