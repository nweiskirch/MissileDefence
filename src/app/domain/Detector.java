/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.domain;

import app.domain.missiles.BallisticInbound;
import app.managers.DetectorManager;
import app.managers.InboundManager;
import app.managers.LauncherManager;
import display.interfaces.Displayable;
import display.managers.DisplayManager;
import java.util.ArrayList;
import utils.LoggingManager;
import utils.Point3D;
import utils.PropertyManager;
import utils.SoundUtility;

/**
 * Detectors will detect Ballistic Inbound Missiles and then notify a launcher
 * to launch an intercepting missile.
 *
 * @author Nate
 */
public abstract class Detector implements Displayable {

    public Detector(Point3D locationIn, String idIn, double rangeIn, ArrayList<Launcher> associatedLaunchers) {
        if (rangeIn < 0) {
            range = 0;
            throw new NumberFormatException("Range less than 0");
        }
        location = locationIn;
        id = idIn;
        range = rangeIn;
        launchers = associatedLaunchers;
        for (Launcher l : associatedLaunchers) {
            l.setDetector(this);
        }
        symbol = "FD";
        addSelf();
        LoggingManager.logInfo("Fixed Detector Created, ID = " + id);
    }
    /**
     * Its current location
     */
    protected Point3D location;
    /**
     * Its id string
     */
    protected String id;
    /**
     * Its display symbol
     */
    protected String symbol;
    /**
     * The detection range
     */
    protected double range;
    /**
     * All of the launchers associated with the detector
     */
    protected ArrayList<Launcher> launchers;

    /**
     *
     * @return The current location
     */
    public Point3D getLocation() {
        return location;
    }

    /**
     *
     * @return The missiles display symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     *
     * @return The missiles range
     */
    public double getRange() {
        return range;
    }

    /**
     * Adds itself from the display and detector manager
     */
    private void addSelf() {
        DisplayManager.getInstance().addContent(this);
        DetectorManager.getInstance().addEntry(this);
    }

    /**
     * Removes itself from the display and detector manager
     */
    protected void removeSelf() {
        DetectorManager.getInstance().removeEntry(this);
        DisplayManager.getInstance().removeContent(this, PropertyManager.Instance().getIntProperty("REMOVALDELAY"));
    }

    /**
     * Destroys the detector
     */
    public void destroy() {
        LoggingManager.logInfo("Detector Destroyed, ID = " + id);
        symbol = "%";
        SoundUtility.getInstance().playSound("Argh.wav");
        removeSelf();
    }

    public void removeAssociatedLauncher(Launcher l) {
        launchers.remove(l);
    }
    
    public void update(double millis) {
        if(millis < 0){
            millis = 0;
            throw new NumberFormatException("Time less than 0");
        }
        ArrayList<BallisticInbound> detected = InboundManager.getInstance().detect(this);
        for(int i = 0; i<detected.size(); i++){
            performLaunch(detected.get(i));
        }
        for(int i = 0; i<launchers.size(); i++){
            launchers.get(i).update(millis);
        }
    }

    protected void performLaunch(BallisticInbound bi){
        if (LauncherManager.getInstance().isFullyTargeted(bi) || launchers.isEmpty()) {
            return;
        }
        int numNeeded = PropertyManager.Instance().getIntProperty("MAXTARGETING")
                - LauncherManager.getInstance().numTargeted(bi);
        int numToLaunch = numNeeded;
        while (numNeeded>0) {            
            LoggingManager.logInfo("BallisticInbound Detected, Initiating Launch");
            Launcher toLaunch = null;
            int n = 0;
            for (Launcher l : launchers) {
                if (l.getNumInterceptors() > n) {
                    toLaunch = l;
                    n = l.getNumInterceptors();
                }
            }
            if (toLaunch.getNumInterceptors() < numToLaunch) {
                numToLaunch = toLaunch.getNumInterceptors();
            }
            try {
                toLaunch.launch(bi, numToLaunch);
            } catch (Exception ex) {
                LoggingManager.logError(ex.getMessage());
                return;
            }
            if (toLaunch.getNumInterceptors() == 0) {
                launchers.remove(toLaunch);
            }
            numNeeded -= numToLaunch;
            if(numNeeded > 0 && launchers.isEmpty()){
                break;
            }
        }
    }
}
