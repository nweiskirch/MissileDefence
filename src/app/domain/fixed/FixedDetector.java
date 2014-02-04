package app.domain.fixed;

import app.domain.BallisticInbound;
import app.domain.Launcher;
import app.interfaces.Detector;
import app.managers.InboundManager;
import java.util.ArrayList;
import utils.LoggingManager;
import utils.Point3D;

/**
 * A Fixed Detector is a detector that cannot move
 * @author Nate
 */
public class FixedDetector extends Detector{

    /**
     * Creates a Fixed Detector
     * @param locationIn the location
     * @param idIn the ID string
     * @param rangeIn the detection range
     * @param associatedLaunchers its associated launchers
     */
    public FixedDetector(Point3D locationIn, String idIn, double rangeIn, ArrayList<Launcher> associatedLaunchers) {
        if(rangeIn < 0){
            range = 0;
            throw new NumberFormatException("Range less than 0");
        }
        location = locationIn;
        id = idIn;
        range = rangeIn;
        launchers = associatedLaunchers;
        symbol = "FD";
        addSelf();
    }

    /**
     * Updates the state of the detector. Will search for missiles and notify launchers
     * @param millis the time that has passed since the last call to update()
     */
    public void update(double millis) {
        if(millis < 0){
            millis = 0;
            throw new NumberFormatException("Time less than 0");
        }
        ArrayList<BallisticInbound> detected = InboundManager.getInstance().detect(this);
        for(int i = 0; i<detected.size(); i++){
            LoggingManager.logInfo("BallisticInbound Detected, Initiating Launch");
        }
        for(int i = 0; i<launchers.size(); i++){
            launchers.get(i).update(millis);
        }
    }
}
