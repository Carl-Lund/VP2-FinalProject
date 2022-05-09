/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package playmaker.model;

import playmaker.ui.PlayMakerWindow;

/**
 *
 * @author carl
 */
public class Main {
    public static void main(String args[]) {
        // Starts the program by making an instance of the singleton PlayModel.
        // Then it passes that instance to the play maker window which is then shown.
        PlayModel currentPlay = PlayModel.getInstance();
        PlayMakerWindow mainWin = new PlayMakerWindow(currentPlay);
        mainWin.setVisible(true);
    }
}
