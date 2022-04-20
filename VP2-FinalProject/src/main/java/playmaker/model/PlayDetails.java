/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package playmaker.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 *
 * @author carl
 */
public class PlayDetails{
    private ArrayList<OPlayer> offense;
    private ArrayList<DPlayer> defense;
    
    private Player playerChosen;
    
    public PlayDetails() {
        offense = new ArrayList<OPlayer>();
        defense = new ArrayList<DPlayer>();
    }
    
    public int getNumOPlayers() {return offense.size();}
    public int getNumDPlayers() {return defense.size();}
    public OPlayer getOPlayer(int i){return offense.get(i);}
    public DPlayer getDPlayer(int i){return defense.get(i);}
    public void setPlayerChosen(Player p) {playerChosen = p;}
    
    public void emptyPlay(){
        offense.clear();
        defense.clear();
    }
    
    public void addPathPoint (int mouseX, int mouseY) {
        playerChosen.path.addXPoint(mouseX);
        playerChosen.path.addYPoint(mouseY);
    }
    
    public void addOPlayer(OPlayer tmpOP){
        offense.add(tmpOP); 
        playerChosen = tmpOP;
        if (tmpOP.path.getListSize() == 0) {
                tmpOP.path.addXPoint(tmpOP.getX());
                tmpOP.path.addYPoint(tmpOP.getY());
        }
    }
    
    public void addDPlayer(DPlayer tmpDP){
        defense.add(tmpDP); 
        playerChosen = tmpDP;
        if (tmpDP.path.getListSize() == 0) {
                tmpDP.path.addXPoint(tmpDP.getX());
                tmpDP.path.addYPoint(tmpDP.getY());
        }
    }
    
    // Adds the first point in the path for the selected player (which should be in the middle of the player) if this player has a path size of 0. Then it makes the player clicked the chosen one.
    public void onPlayerClicked(Player p) {
        if (p != null) {
            if (p.path.getListSize() == 0) { 
                p.path.addXPoint(p.getX());
                p.path.addYPoint(p.getY());
            }
            playerChosen = p;         
        }    
    }
    
    public void movePlayers() {
       for (OPlayer p : offense) {
           p.movePlayer();
       }
       for (DPlayer p : defense) {
           p.movePlayer();
       }
    }
    
    public void resetPlayers() {
        for (OPlayer p : offense) {
           p.reset();
        }
        for (DPlayer p : defense) {
           p.reset();
        }
    }
    
    public void movePlayer() {
        playerChosen.movePlayer();
    }
    
    public void stopPlayer() {
        playerChosen.stop();
    }
    
    public void resetPlayer() {
        playerChosen.reset();
    }
    
    public void removePlayer() {
        for (OPlayer p : offense) {
           if (playerChosen == p) {
               offense.remove(playerChosen);
           }
        }
        for (DPlayer p : defense) {
           if (playerChosen == p) {
               defense.remove(playerChosen);
           }
        }
        
        if (offense.size() > 0) {
            playerChosen = offense.get(0);
        } else if (defense.size() > 0) {
            playerChosen = defense.get(0);
        } else {
            playerChosen = null;
        }
        playerChosen.repaint();
    }
    
    public void removePlayerPath() {
        playerChosen.path.clearPath();
    }
    
    public void drawPlayers(Graphics2D g2) {  
        for(OPlayer p : offense){
            if (p == playerChosen) {
                p.setColor(Color.BLUE);
            } else {
                p.setColor(Color.BLACK);
            }
            p.paint(g2);
        }
        
        for(DPlayer p : defense){
            if (p == playerChosen) {
                p.setColor(Color.BLUE);
            } else {
                p.setColor(Color.BLACK);
            }
            p.paint(g2);
        }
    }
    
    public void drawPaths(Graphics2D g2) {
        for(OPlayer p : offense){
            p.path.paint(g2);
        }
        
        for(DPlayer p : defense){
            p.path.paint(g2);
        }
    }

}
