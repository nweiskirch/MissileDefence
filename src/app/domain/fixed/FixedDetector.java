package app.domain.fixed;

import app.domain.Launcher;
import app.domain.Detector;
import java.util.ArrayList;
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
        super(locationIn, idIn, rangeIn, associatedLaunchers);
    }
}
