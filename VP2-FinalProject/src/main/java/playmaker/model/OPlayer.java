/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package playmaker.model;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 *
 * @author carl
 */
public class OPlayer extends Player{
    
    public OPlayer(int x, int y) {
        this.playerX = x;
        this.playerY = y;
        path = new Path();
    }
    
    @Override
    public void paint(Graphics2D g2){
        g2.setStroke(new BasicStroke(4));
        g2.drawOval(playerX - halfW, playerY - halfH, playerW, playerH);
    }
    
}
