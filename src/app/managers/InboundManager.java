/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.managers;

import app.domain.BallisticInbound;
import app.interfaces.Detector;
import java.util.ArrayList;
import utils.PropertyManager;

/**
 * Manages all inbound ballistic missiles
 * @author Nate
 */
public class InboundManager {

    private static InboundManager instance;
    private ArrayList<BallisticInbound> entries;

    /**
     * @return the current static instance of the Inbound Manager
     */
    public static InboundManager getInstance() {
        if (instance == null) {
            instance = new InboundManager();
        }
        return instance;
    }
    
    private InboundManager(){
        entries = new ArrayList<>();
    }
    
    /**
     *
     * @return The number of ballistic missiles being managed
     */
    public int getNumEntries(){
        return entries.size();
    }
    
    /**
     * Updates all of the current missiles
     * @param millis the time that has passed since the last call to update()
     */
    public void update(int millis){
        for(int i = 0; i<entries.size(); i++){
            entries.get(i).update(millis);
        }
    }
    
    /**
     * Adds a ballistic missile to the manager
     * @param bi the missile to be added
     */
    public void addEntry(BallisticInbound bi){
        entries.add(bi);
    }
    
    /**
     * Removes a ballistic missile from the manager
     * @param bi the missile to be removed
     */
    public void removeEntry(BallisticInbound bi){
        entries.remove(bi);
    }
    
    /**
     * Asks the manager to detect all of the missiles in a given detectors range
     * @param d the detector that is looking
     * @return all of the missiles it can pick up
     */
    public ArrayList<BallisticInbound> detect(Detector d){
        ArrayList<BallisticInbound> detected = new ArrayList<>();
        double minAlt = PropertyManager.Instance().getDoubleProperty("MINALTITUDE");
        for(BallisticInbound bi: entries){
            if(d.getLocation().distance(bi.getLocation()) < d.getRange() && bi.getLocation().getZ() < minAlt){
                detected.add(bi);
            }
        }
        return detected;
    }
}
