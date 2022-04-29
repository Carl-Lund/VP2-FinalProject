/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package playmaker.ui;

/**
 *
 * @author carl
 */
public interface PlayAreaHandler {
    // Communication from the Player Details Panel
    public void movePlayer();
    public void stopPlayer();
    public void resetPlayer();
    public void removePlayer();
    public void removePlayerPath();
    
    // So that the players can repaint themselves.
    public void repaintArea();
    
    // Communication from the Play Controls Panel.
    public void startPlay();
    public void resetPlayers();
    public void emptyArea();
    public void savePlay();
    public void loadPlay();
}
