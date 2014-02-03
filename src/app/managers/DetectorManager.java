/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package app.managers;

import app.interfaces.Detector;
import java.util.ArrayList;
import utils.Point3D;
import utils.PropertyManager;

/**
 *
 * @author Nate
 */
public class DetectorManager {
    private static DetectorManager instance;
    private ArrayList<Detector> entries;

    public static DetectorManager getInstance() {
        if (instance == null) {
            instance = new DetectorManager();
        }
        return instance;
    }
    
    private DetectorManager(){
        entries = new ArrayList<>();
    }
    
    public int getNumEntries(){
        return entries.size();
    }
    
    public void update(int millis){
        for(int i = 0; i<entries.size(); i++){
            entries.get(i).update(millis);
        }
    }
    
    public void addEntry(Detector d){
        entries.add(d);
    }
    
    public void removeEntry(Detector d){
        entries.remove(d);
    }
    
    public void applyBlastDamage(Point3D location){
        int blastRange = PropertyManager.Instance().getIntProperty("BLASTRANGE");
        for(int i = 0; i<entries.size(); i++){
            if(location.distance(entries.get(i).getLocation())<blastRange){
                entries.get(i).destroy();
                i--;
            }
        }
    }
}
