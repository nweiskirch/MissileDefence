/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.interfaces;

import app.domain.Launcher;
import app.managers.DetectorManager;
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
 * @author Nate
 */
public abstract class Detector implements Displayable {

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
     * Updates the state of the detector
     * @param millis the time that has passed since the last call to update()
     */
    public abstract void update(double millis);

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
    protected void addSelf() {
        DisplayManager.getInstance().addContent(this);
        DetectorManager.getInstance().addEntry(this);
    }
    
    /**
     * Removes itself from the display and detector manager
     */
    protected void removeSelf(){
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
}
