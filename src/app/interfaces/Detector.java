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
 *
 * @author Nate
 */
public abstract class Detector implements Displayable {

    protected Point3D location;
    protected String id;
    protected String symbol;
    protected double range;
    protected ArrayList<Launcher> launchers;

    public abstract void update(double millis);

    public Point3D getLocation() {
        return location;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getRange() {
        return range;
    }

    protected void addSelf() {
        DisplayManager.getInstance().addContent(this);
        DetectorManager.getInstance().addEntry(this);
    }
    
    protected void removeSelf(){
        DetectorManager.getInstance().removeEntry(this);
        DisplayManager.getInstance().removeContent(this, PropertyManager.Instance().getIntProperty("REMOVALDELAY"));
    }

    public void destroy() {
        LoggingManager.logInfo("Detector Destroyed, ID = " + id);
        symbol = "%";
        SoundUtility.getInstance().playSound("Argh.wav");
        removeSelf();
    }
}
