/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.managers;

import app.domain.missiles.BallisticInbound;
import app.domain.Detector;
import app.domain.missiles.Interceptor;
import java.util.ArrayList;
import utils.Point3D;
import utils.PropertyManager;

/**
 * Manages all inbound ballistic missiles
 *
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

    private InboundManager() {
        entries = new ArrayList<>();
    }

    /**
     *
     * @return The number of ballistic missiles being managed
     */
    public int getNumEntries() {
        return entries.size();
    }

    /**
     * Updates all of the current missiles
     *
     * @param millis the time that has passed since the last call to update()
     */
    public void update(int millis) {
        for (int i = 0; i < entries.size(); i++) {
            entries.get(i).update(millis);
        }
    }

    /**
     * Adds a ballistic missile to the manager
     *
     * @param bi the missile to be added
     */
    public void addEntry(BallisticInbound bi) {
        entries.add(bi);
    }

    /**
     * Removes a ballistic missile from the manager
     *
     * @param bi the missile to be removed
     */
    public void removeEntry(BallisticInbound bi) {
        entries.remove(bi);
    }

    public boolean contains(BallisticInbound bi) {
        return entries.contains(bi);
    }

    public void lockDetected(BallisticInbound bi, Point3D location) {
        bi.lockDetected(location);
    }

    public BallisticInbound getClosest(Point3D p) {
        BallisticInbound closest = entries.get(0);
        for (BallisticInbound bi : entries) {
            if (bi.getLocation().distance(p) < closest.getLocation().distance(p)) {
                closest = bi;
            }
        }
        return closest;
    }

    public void interceptorStrike(Interceptor in) {
        double interceptRange = PropertyManager.Instance().getDoubleProperty("INTERCEPTORBLASTRANGE");
        for (int i = 0; i < entries.size(); i++) {
            if (in.getLocation().distance(entries.get(i).getLocation()) < interceptRange) {
                if (in.getLocation().distance(entries.get(i).getLocation()) < interceptRange * .75) {
                    entries.get(i).selfDestruct();
                    i--;
                } else {
                    entries.get(i).alterCourse();
                }
            }
        }
    }

    /**
     * Asks the manager to detect all of the missiles in a given detectors range
     *
     * @param d the detector that is looking
     * @return all of the missiles it can pick up
     */
    public ArrayList<BallisticInbound> detect(Detector d) {
        ArrayList<BallisticInbound> detected = new ArrayList<>();
        double minAlt = PropertyManager.Instance().getDoubleProperty("MINALTITUDE");
        for (BallisticInbound bi : entries) {
            if (d.getLocation().distance(bi.getLocation()) < d.getRange() && bi.getLocation().getZ() > minAlt) {
                detected.add(bi);
            }
        }
        return detected;
    }
}
