/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package playmaker.model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import playmaker.ui.PlayListener;

/**
 *
 * @author carl
 */
public class PlayModel implements Serializable {
    private ArrayList<OPlayer> offense;
    private ArrayList<DPlayer> defense;
    private transient ArrayList<PlayListener> observers; // List of observers that view the contents of PlayModel.
    private static PlayModel currentPlay = null;
    private transient final PlaySerialization playIO; // playIO handles the saving and loading of the plays through gson/json.
    private transient Player playerChosen; // The player chosen is the current selected player by the user, 
                                           // which is highlighted blue and shown in the player details frame.
    
    public static PlayModel getInstance(){
        if (currentPlay == null) 
            currentPlay = new PlayModel();
        return currentPlay;
    }
    
    private PlayModel() {
        offense = new ArrayList<>();
        defense = new ArrayList<>();
        observers = new ArrayList<>();
        playIO = new PlaySerialization(observers);
    }
    
    public int getNumOPlayers() {return offense.size();}
    public int getNumDPlayers() {return defense.size();}
    public OPlayer getOPlayer(int i){return offense.get(i);}
    public DPlayer getDPlayer(int i){return defense.get(i);}
    public ArrayList<OPlayer> getOffense() {return offense;}
    public ArrayList<DPlayer> getDefense() {return defense;}
    public ArrayList<PlayListener> getListeners() {return observers;}
    public Player getPlayerChosen() {return playerChosen;}
    public String getPlayerText() {return playerChosen.getJob();}
    public String getPlayerPos() {return playerChosen.getPos();}
    
    public void setListeners(ArrayList<PlayListener> observers) {this.observers = observers;}
    public void setPlayerChosen(Player p) {playerChosen = p;}
    public void setOffense(ArrayList<OPlayer> oPlayers) { offense = oPlayers;}
    public void setDefense(ArrayList<DPlayer> dPlayers) { defense = dPlayers;}
    public void setPlayerText(String tmp) {playerChosen.setJob(tmp);}
    public void setPlayerPos(String tmpPos) {playerChosen.setPos(tmpPos);}
    
    public void addObserver(PlayListener p){observers.add(p);}
    
    
    // ----------------------------------------------------------------------------------
    // Methods that are controlled by the Play Controller Panel.
    public void savePlay(){
        playIO.savePlay();
    }
    
    public void loadPlay() {
        // Receives the selected play file in terms of PlayModel, if null is returned 
        // then do nothing because that means the user hit cancel, else set currentPlay to
        // the PlayModel of the selected play file.
        PlayModel tmpPlay = PlayModel.getInstance();
        
        try {
            tmpPlay = playIO.loadPlay();
        } catch (IOException ex) {
            System.out.println("IO Exception when trying to load play.");
        } catch (ClassNotFoundException ex) {
            System.out.println("Class Not Found Exception when trying to load play.");
        }
        
        if (tmpPlay != null) {
            // Why won't you work :(
            currentPlay = tmpPlay;
            updateListeners();
        }
        
    }
    
    public void emptyPlay(){
        offense.clear();
        defense.clear();
        updateListeners();
    }
    
    public void movePlayers() {
       for (OPlayer p : offense) {
           p.movePlayer();
       }
       for (DPlayer p : defense) {
           p.movePlayer();
       }
    }
    
    public void stopPlayers() {
       for (OPlayer p : offense) {
           p.stop();
       }
       for (DPlayer p : defense) {
           p.stop();
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
    
    
    // ----------------------------------------------------------------------------------
    // Player manipulation methods.
    public void addOPlayer(OPlayer tmpOP){
        offense.add(tmpOP); 
        playerChosen = tmpOP;
        if (tmpOP.path.getListSize() == 0) {
                tmpOP.path.addXPoint((int)tmpOP.getX());
                tmpOP.path.addYPoint((int)tmpOP.getY());
        }
    }
    
    public void addDPlayer(DPlayer tmpDP){
        defense.add(tmpDP); 
        playerChosen = tmpDP;
        if (tmpDP.path.getListSize() == 0) {
                tmpDP.path.addXPoint((int)tmpDP.getX());
                tmpDP.path.addYPoint((int)tmpDP.getY());
        }
    }
    
    // Adds a path point to the selected player's path.
    public void addPathPoint (int mouseX, int mouseY) {
        playerChosen.path.addXPoint(mouseX);
        playerChosen.path.addYPoint(mouseY);
    }
    
    // Adds the first point in the path for the selected player (which should be in the middle of the player)
    // if this player has a path size of 0. Then it makes the player clicked the chosen one.
    public void onPlayerClicked(Player p) {
        if (p != null) {
            if (p.path.getListSize() == 0) { 
                p.path.addXPoint((int)p.getX());
                p.path.addYPoint((int)p.getY());
            }
            playerChosen = p;
            updateListeners();
        }    
    }
    
    
    // ----------------------------------------------------------------------------------
    // Methods for the selected player. Which are controlled by the player details panel.
    public void movePlayer() {playerChosen.movePlayer();}
    public void stopPlayer() {playerChosen.stop();}
    public void resetPlayer() {playerChosen.reset();}
    
    public void removePlayerPath() {
        playerChosen.path.clearPath();
        updateListeners();
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
        updateListeners();
    }
    
    
    // ----------------------------------------------------------------------------------
    // General Methods
    private void updateListeners(){
        for (PlayListener p : observers) {
            p.onUpdate();
        }
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
            if (p.path != null) {
                p.path.paint(g2);
            }
        }
        
        for(DPlayer p : defense){
            if (p.path != null) {
                p.path.paint(g2);
            }
        }
    }

}
