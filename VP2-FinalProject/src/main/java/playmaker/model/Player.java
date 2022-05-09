/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package playmaker.model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.ArrayList;
import javax.swing.Timer;
import playmaker.ui.RepaintHandler;

/**
 *
 * @author carl
 */
public abstract class Player implements ActionListener, Serializable {
    protected String side;
    protected String pos = "<Select>";
    protected String name = "Enter";
    protected String habit = "Enter";
    protected String job;
    protected String num = "Enter";
    protected transient Color color;
    protected boolean scoutView;
    
    protected double playerX, playerY;
    protected int playerW = 30;
    protected int playerH = 30;
    protected int halfW = playerW/2;
    protected int halfH = playerH/2;
    protected double xDif, yDif, xVel, yVel;
    protected String xDir, yDir;
    
    protected Path path;
    protected ArrayList<Integer> pathX;
    protected ArrayList<Integer> pathY;
    
    protected transient RepaintHandler handler;
    
    int nextPoint = 1;
    int pastPoint = 0;
    
    private transient Timer tm = new Timer(33, this); // 33 for ~30 frames a second. 
                                            // The purpose for this timer is to update the position of the player
                                            // using the x velocity and y velocity and call redraw when the position is updated.   
    
    public double getX() {return playerX;}  
    public double getY() {return playerY;}    
    public String getPos() {return pos;} 
    public String getJob() {return job;}
    public String getName() {return name;}
    public String getNum() {return num;}
    public String getHabit() {return habit;}
    
    public void setJob(String tmp) {job = tmp;}
    public void setName(String tmp) {name = tmp;}
    public void setNum(String tmp) {num = tmp;}
    public void setHabit(String tmp) {habit = tmp;}
    public void setX(int x) {this.playerX = x;}   
    public void setY(int y) {this.playerY = y;}   
    public void setPos(String pos) {this.pos = pos;}
    public void setNewTimer() {tm = new Timer(33, this);}
    public void setScoutView(boolean scoutView) {this.scoutView = scoutView;}
    
    public boolean isScoutView() {return scoutView;}
    
    public abstract void paint(Graphics2D g2);
    
    // Calulates the distance from the click and the center position of this player,
    // then if that distance is less than or equal to half the width of the player then the click
    // must be within the player and returns true if it is and false if it isn't.
    public boolean pointContainsMe(int mouseX, int mouseY) {
            int a = mouseX - (int)getX();
            int b = mouseY - (int)getY();
            double dist =  Math.sqrt(a*a + b*b);
            return (dist <= halfW);
    }
    
    // If the player has a path then check if it is at its starting position.
    // If it is, then get the points in that path, make sure nextPoint and 
    // pastPoint are set correctly, calculate the x and y velocities for that 
    // portion of the path, find the direction the player is moving in, 
    // and then start the timer.
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
    
    // This function is called by the timer every 33ms. Essentially, it handles
    // updating the player's positioning based on where it is in its path.
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
            xVel = xDif / 20.0;
            yVel = yDif / 20.0;
            this.setXDir();
            this.setYDir();
            
        // Else if the player isn't to their next point yet then increment their position with the current velocities.
        } else {
            playerX  = playerX + xVel;
            playerY = playerY + yVel;
        }
        
        handler.repaintArea();
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
        handler.repaintArea();
    }
    
    public void stop() {
        tm.stop();
    }
    
    public void setColor(Color color) {
        this.color = color;
    }
}
