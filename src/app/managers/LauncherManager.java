/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.managers;

import app.domain.BallisticInbound;
import app.domain.Launcher;
import app.domain.Interceptor;
import java.util.ArrayList;
import java.util.HashMap;
import utils.LoggingManager;
import utils.Point3D;
import utils.PropertyManager;

/**
 *
 * @author Nate
 */
public class LauncherManager {

    private ArrayList<Launcher> entries;
    private static LauncherManager instance;
    private HashMap targets;

    public static LauncherManager getInstance() {
        if (instance == null) {
            instance = new LauncherManager();
        }
        return instance;
    }

    public int getNumEntries() {
        return entries.size();
    }

    public void applyBlastDamage(Point3D location) {
        LoggingManager.logInfo("Applying blast damage at location " + location.toString());
        int blastRange = PropertyManager.Instance().getIntProperty("BLASTRANGE");
        for (int i = 0; i < entries.size(); i++) {
            if (location.distance(entries.get(i).getLocation()) < blastRange) {
                entries.get(i).destroy();
                i--;
            }
        }
    }

    public int numTargeted(BallisticInbound bi) {
        if (!targets.containsKey(bi)) {
            return 0;
        }
        return ((ArrayList) targets.get(bi)).size();
    }

    public boolean isFullyTargeted(BallisticInbound bi) {
        return PropertyManager.Instance().getIntProperty("MAXTARGETING") == numTargeted(bi);
    }

    public void addToTarget(BallisticInbound bi, Interceptor i) throws Exception {
        if (isFullyTargeted(bi)) {
            throw new Exception("Missile already fully targeted");
        } else if (targets.containsKey(bi)) {
            ((ArrayList) targets.get(bi)).add(i);
        } else {
            ArrayList<Interceptor> ins = new ArrayList<>();
            ins.add(i);
            targets.put(bi, ins);
        }
    }
    
    public void removeInterceptor(Interceptor i) throws Exception{
        if(!targets.containsKey(i.getTarget())){
            throw new Exception("Target does not exist");
        }
        else{
            ((ArrayList)targets.get(i.getTarget())).remove(i);
            if(((ArrayList)targets.get(i.getTarget())).isEmpty()){
                targets.remove(i.getTarget());
            }
        }
    }

    /**
     * Adds a detector
     *
     * @param d the detector to be added
     */
    public void addEntry(Launcher l) {
        entries.add(l);
    }

    /**
     * Removes a detector
     *
     * @param d the detector to be removed
     */
    public void removeEntry(Launcher l) {
        entries.remove(l);
    }

    private LauncherManager() {
        entries = new ArrayList<>();
        targets = new HashMap();
    }
}
