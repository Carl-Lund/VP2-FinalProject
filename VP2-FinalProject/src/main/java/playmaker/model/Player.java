/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package playmaker.model;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 *
 * @author carl
 */
public abstract class Player {
    protected int playerX;
    protected int playerY;
    protected int playerW = 30;
    protected int playerH = 30;
    protected int halfW = playerW/2;
    protected int halfH = playerH/2;
    
    protected Path path;
    protected String position;

    public int getX() {return playerX;}  
    public int getY() {return playerY;}    
    public String getPos() {return position; }    
    public void setX(int x) {this.playerX = x;}   
    public void setY(int y) {this.playerY = y;}   
    public void setPos(String position) {this.position = position; }   
    public abstract void paint(Graphics2D g2);
    
    public boolean pointContainsMe(int mouseX, int mouseY) {
            int a = mouseX - getX();
            int b = mouseY - getY();
            double dist =  Math.sqrt(a*a + b*b);
            return (dist <= halfW);
    }
}
