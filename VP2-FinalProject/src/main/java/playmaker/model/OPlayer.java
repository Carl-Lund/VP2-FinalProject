 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package playmaker.model;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.ArrayList;
import javax.swing.Timer;
import playmaker.ui.PlayAreaHandler;

/**
 *
 * @author carl
 */
public class OPlayer extends Player implements Serializable {
    
    public OPlayer(int x, int y, PlayAreaHandler handler) {
        playerX = x;
        playerY = y - (y % 20);
        path = new Path();
        side = "offense";
        listener = handler;
    }
    
    @Override
    public void paint(Graphics2D g2){
        g2.setColor(color);
        g2.setStroke(new BasicStroke(4));
        g2.drawOval((int)(playerX - halfW), (int)(playerY - halfH), playerW, playerH);
    }
    
}
