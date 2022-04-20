/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package playmaker.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.Timer;
import playmaker.ui.RepaintHandler;

/**
 *
 * @author carl
 */
public abstract class Player implements ActionListener {
    protected String side;
    protected int playerX;
    protected int playerY;
    protected int playerW = 30;
    protected int playerH = 30;
    protected int halfW = playerW/2;
    protected int halfH = playerH/2;
    protected Color color;
    
    protected int xDif, yDif, xVel, yVel;
    protected String xDir, yDir;
    
    protected Path path;
    protected String position;
    protected ArrayList<Integer> pathX;
    protected ArrayList<Integer> pathY;

    protected RepaintHandler listener;
    
    int nextPoint = 1;
    int pastPoint = 0;
    
    Timer tm = new Timer(33, this); // 33 for ~30 frames a second.

    public int getX() {return playerX;}  
    public int getY() {return playerY;}    
    public String getPos() {return position; }    
    public void setX(int x) {this.playerX = x;}   
    public void setY(int y) {this.playerY = y;}   
    public void setPos(String position) {this.position = position; }
    public void repaint() {listener.repaintArea();}
    public abstract void paint(Graphics2D g2);
    
    public boolean pointContainsMe(int mouseX, int mouseY) {
            int a = mouseX - getX();
            int b = mouseY - getY();
            double dist =  Math.sqrt(a*a + b*b);
            return (dist <= halfW);
    }
    
    // If the player has a path, get the points in that path, make sure 
    public void movePlayer(){
        if (path.getListSize() > 1) {
            if ((playerX == path.getStartX()) && (playerY == path.getStartY())) {
                pathX = path.getPathXPoints();
                pathY = path.getPathYPoints();
                nextPoint = 1;
                pastPoint = 0;
                xDif = pathX.get(nextPoint) - pathX.get(pastPoint);
                yDif = pathY.get(nextPoint) - pathY.get(pastPoint);
                xVel = xDif / 20;
                yVel = yDif / 20;
            }
            this.setXDir();
            this.setYDir();

            tm.start();
        }
    }
    
    public void actionPerformed(ActionEvent e) {
        
        // If the player is equal to or past the last point in its path, set its x and y to that point and stop incrementing.
        if ((nextPoint == path.getListSize() - 1) && this.playerReachEndPoint()) {
            playerY = pathY.get(path.getListSize() - 1);
            playerX = pathX.get(path.getListSize() - 1);
            tm.stop();
        
        // Else if the player is equal to or past its next point, set its x and y to that point,
        // calculate new velocities, and update next point and past point.
        } else if (this.playerReachNextPoint()) {
            playerY = pathY.get(nextPoint);
            playerX = pathX.get(nextPoint);
            nextPoint++;
            pastPoint++;
            xDif = pathX.get(nextPoint) - pathX.get(pastPoint);
            yDif = pathY.get(nextPoint) - pathY.get(pastPoint);
            xVel = xDif / 20;
            yVel = yDif / 20;
            this.setXDir();
            this.setYDir();
            
        // Else if the player isn't to their next point yet then increment their position with the current velocities.
        } else {
            playerX  = playerX + xVel;
            playerY = playerY + yVel;
        }
        
        listener.repaintArea();
    }
    
    private boolean playerReachEndPoint() {
        return (playerY <= pathY.get(path.getListSize() - 1) && yDir == "up") ||
            (playerY >= pathY.get(path.getListSize() - 1) && yDir == "down") || 
            (playerX >= pathX.get(path.getListSize() - 1) && xDir == "right") ||
            (playerX <= pathX.get(path.getListSize() - 1) && xDir == "left");
    }
    
    private boolean playerReachNextPoint() {
        return (playerY <= pathY.get(nextPoint) && yDir == "up") ||
            (playerY >= pathY.get(nextPoint) && yDir == "down") || 
            (playerX >= pathX.get(nextPoint) && xDir == "right") ||
            (playerX <= pathX.get(nextPoint) && xDir == "left");
    }
    
    private void setXDir() {
        if (xVel > 0) {
            xDir = "right";
        } else if (xVel < 0) {
            xDir = "left";
        } else {
            xDir = "zero";
        }
    }
    
    private void setYDir() {
        if (yVel > 0) {
            yDir = "down";
        } else if (yVel < 0) {
            yDir = "up";
        } else {
            yDir = "zero";
        }
    }
    
    public void reset() {
        playerX = path.getStartX();
        playerY = path.getStartY();
    }
    
    public void stop() {
        tm.stop();
    }
    
    public void setColor(Color color) {
        this.color = color;
    }
}
