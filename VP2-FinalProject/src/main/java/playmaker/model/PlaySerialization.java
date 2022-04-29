/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package playmaker.model;

import java.io.*;

/**
 *
 * @author carl
 */
public class PlaySerialization {
    private File file = new File("test.txt");
    private FileOutputStream fo;
    private FileInputStream fi;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    
    private PlayDetails currentPlay;
    
    public PlaySerialization() {} 
    
    public void savePlay (PlayDetails playToSave) throws FileNotFoundException, IOException {
        currentPlay = playToSave;
        fo = new FileOutputStream(file);
        output = new ObjectOutputStream(fo);
        output.writeObject(currentPlay);
        output.close();
        fo.close();
    }
    
    public PlayDetails loadPlay() throws FileNotFoundException, IOException, ClassNotFoundException {
        fi = new FileInputStream(file);
        input = new ObjectInputStream(fi);
        
        try {
            while(true) {
                currentPlay = (PlayDetails)input.readObject();
            }
        } catch (EOFException ex) {
        }
        
        input.close();
        fi.close();
        return currentPlay;
    }
    
}
