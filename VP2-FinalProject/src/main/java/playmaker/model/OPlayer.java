 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package playmaker.model;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.io.Serializable;
import playmaker.ui.RepaintHandler;

/**
 *
 * @author carl
 */
public class OPlayer extends Player implements Serializable {
    
    public OPlayer(int x, int y, RepaintHandler handler) {
        playerX = x;
        playerY = y - (y % 20); // Modded by 20 so that the user can draw straight lines of scrimmage;
        path = new Path();
        side = "offense";
        scoutView = false;
        this.handler = handler;
    }
    
    @Override
    public void paint(Graphics2D g2){
        g2.setColor(color);
        g2.setStroke(new BasicStroke(4));
        g2.drawOval((int)(playerX - halfW), (int)(playerY - halfW), playerW, playerH);
    }
    
}
