package app.domain.fixed;

import app.domain.BallisticInbound;
import app.interfaces.Detector;
import app.managers.InboundManager;
import java.util.ArrayList;
import utils.LoggingManager;
import utils.Point3D;

/**
 *
 * @author Nate
 */
public class FixedDetector extends Detector{

    public FixedDetector(Point3D locationIn, String idIn, double rangeIn, ArrayList associatedLaunchers) {
        location = locationIn;
        id = idIn;
        range = rangeIn;
        launchers = associatedLaunchers;
        symbol = "FD";
        addSelf();
    }

    public void update(double millis) {
        ArrayList<BallisticInbound> detected = InboundManager.getInstance().detect(this);
        for(int i = 0; i<detected.size(); i++){
            LoggingManager.logInfo("BallisticInbound Detected, Initiating Launch");
        }
        for(int i = 0; i<launchers.size(); i++){
            launchers.get(i).update(millis);
        }
    }
}
