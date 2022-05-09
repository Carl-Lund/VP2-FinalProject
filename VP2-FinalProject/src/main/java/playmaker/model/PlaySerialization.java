/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package playmaker.model;

import com.google.gson.Gson;
import java.io.*;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import playmaker.ui.PlayListener;

/**
 *
 * @author carl
 */
public class PlaySerialization {
    private File file;
    private File selectedFile;
    private BufferedReader reader;
    private JFileChooser jfc;
    private String json;
    private Gson gson = new Gson();
    private ArrayList<PlayListener> observers;
    
    private PlayModel currentPlay;
    private PlayModel tmpPlay;
    
    public PlaySerialization(ArrayList<PlayListener> observers) {
        this.observers = observers;
    } 
    
    // Instantiates PlayModel variable which is turned into a json string. Then the user
    // is asked to enter a file name that will automatically be made a json file of which the string is wrote into.
    public void savePlay () {
        currentPlay = PlayModel.getInstance();
        json = gson.toJson(currentPlay);
        file = new File(JOptionPane.showInputDialog("What would you like to name the file?") + ".json");
        if (file.getName() != "null.json") {
            PrintWriter output;
            try {
                output = new PrintWriter(file);
                output.println(json);
                output.close();
            } catch (FileNotFoundException ex) {
                System.out.println("File not found exception in saving play.");
            }
        }
    }
    
    // Instantiates a temporary PlayModel variable, then the user is asked to select a play file.
    // That play file is converted to a string and then into a PlayModel object that is returned.
    public PlayModel loadPlay() throws FileNotFoundException, IOException, ClassNotFoundException {
        tmpPlay = PlayModel.getInstance();
        jfc = new JFileChooser();
        jfc.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = jfc.showOpenDialog(jfc);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = jfc.getSelectedFile();
        }
        if (selectedFile == null) {
            return null;
        }
        reader = new BufferedReader(new FileReader(selectedFile));
        json = reader.readLine();
        
        tmpPlay = gson.fromJson(json, PlayModel.class);
        System.out.println(json);
        tmpPlay.setListeners(observers);
        
        return tmpPlay;
    }
    
}
