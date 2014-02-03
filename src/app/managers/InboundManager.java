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
 *
 * @author Nate
 */
public class InboundManager {

    private static InboundManager instance;
    private ArrayList<BallisticInbound> entries;

    public static InboundManager getInstance() {
        if (instance == null) {
            instance = new InboundManager();
        }
        return instance;
    }
    
    private InboundManager(){
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
    
    public void addEntry(BallisticInbound bi){
        entries.add(bi);
    }
    
    public void removeEntry(BallisticInbound bi){
        entries.remove(bi);
    }
    
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
