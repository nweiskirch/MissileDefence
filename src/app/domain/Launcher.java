/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.domain;

import app.managers.LauncherManager;
import display.interfaces.Displayable;
import display.managers.DisplayManager;
import java.util.LinkedList;
import java.util.Queue;
import utils.LoggingManager;
import utils.Point3D;
import utils.PropertyManager;
import utils.SoundUtility;

/**
 *
 * @author Nate
 */
public class Launcher implements Displayable {

    private Detector parent;
    protected Point3D location;
    protected String id;
    protected String symbol;
    private Queue<PredictorInterceptor> predins;
    private Queue<TrackerInterceptor> trackins;

    public Launcher(Point3D loc, String idIn, int numPredictors, int numTrackers) {
        location = loc;
        id = idIn;
        predins = new LinkedList<>();
        trackins = new LinkedList<>();
        for (int i = 0; i < numPredictors; i++) {
            predins.add(new PredictorInterceptor(new Point3D(0,0,0), 190, 190, id+"_P_"+i, 75, this));
        }
        for (int i = 0; i < numTrackers; i++) {
            trackins.add(new TrackerInterceptor(new Point3D(0,0,0), 0, 100, id+"_P_"+i, 75, this));
        }
    }

    /**
     * Updates the state of the launcher.
     *
     * @param millis the time that has passed since the last call to update
     */
    public void update(double millis) {
    }

    public void setDetector(Detector d) {
        parent = d;
    }

    public Point3D getLocation() {
        return location;
    }

    public String getSymbol() {
        return symbol;
    }
    
    public int getNumInterceptors(){
        return predins.size() + trackins.size();
    }

    public void destroy() {
        LoggingManager.logInfo("Launcher Destroyed, ID = " + id);
        symbol = "#";
        SoundUtility.getInstance().playSound("Argh.wav");
        for (PredictorInterceptor pi : predins) {
            pi.destroy();
        }
        for (TrackerInterceptor ti : trackins) {
            ti.destroy();
        }
        removeSelf();
    }

    public void launch(BallisticInbound bi, int count) throws Exception {
        for (int i = 0; i < count; i++) {
            if (trackins.size() == 0 && predins.size() == 0) {
                throw new Exception("Launcher Empty. Succesfully launched " + count);
            } else if (trackins.size() > predins.size()) {
                trackins.poll().launch(bi);
            } else {
                predins.poll().launch(bi);
            }
        }
    }

    /**
     * Adds itself from the display and detector manager
     */
    protected void addSelf() {
        DisplayManager.getInstance().addContent(this);
        LauncherManager.getInstance().addEntry(this);
    }

    /**
     * Removes itself from the display and detector manager
     */
    protected void removeSelf() {
        LauncherManager.getInstance().removeEntry(this);
        parent.removeAssociatedLauncher(this);
        DisplayManager.getInstance().removeContent(this, PropertyManager.Instance().getIntProperty("REMOVALDELAY"));
    }
}
