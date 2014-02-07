/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package app.managers;

import app.domain.Detector;
import java.util.ArrayList;
import utils.LoggingManager;
import utils.Point3D;
import utils.PropertyManager;

/**
 * Manages all of the detectors
 * @author Nate
 */
public class DetectorManager {
    private static DetectorManager instance;
    private ArrayList<Detector> entries;

    /**
     * 
     * @return The current instance 
     */
    public static DetectorManager getInstance() {
        if (instance == null) {
            instance = new DetectorManager();
        }
        return instance;
    }
    
    private DetectorManager(){
        entries = new ArrayList<>();
    }
    
    /**
     *
     * @return The number of detectors
     */
    public int getNumEntries(){
        return entries.size();
    }
    
    /**
     * Updates all of the detectors in entries
     * @param millis the time that has passed since the last call to update()
     */
    public void update(int millis){
        if(millis < 0){
            millis = 0;
            throw new NumberFormatException("Time less than 0");
        }
        for(int i = 0; i<entries.size(); i++){
            entries.get(i).update(millis);
        }
    }
    
    /**
     * Adds a detector
     * @param d the detector to be added
     */
    public void addEntry(Detector d){
        entries.add(d);
    }
    
    /**
     * Removes a detector
     * @param d the detector to be removed
     */
    public void removeEntry(Detector d){
        entries.remove(d);
    }
    
    /**
     * Destroys all detectors within the blast radius of the detonation
     * @param location the location of the detonation
     */
    public void applyBlastDamage(Point3D location){
        LoggingManager.logInfo("Applying blast damage at location " +location.toString());
        int blastRange = PropertyManager.Instance().getIntProperty("BLASTRANGE");
        for(int i = 0; i<entries.size(); i++){
            if(location.distance(entries.get(i).getLocation())<blastRange){
                entries.get(i).destroy();
                i--;
            }
        }
    }
}
