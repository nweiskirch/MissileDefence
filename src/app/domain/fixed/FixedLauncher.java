/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.domain.fixed;

import app.domain.Launcher;
import utils.Point3D;

/**
 *
 * @author Nate
 */
public class FixedLauncher extends Launcher {

    public FixedLauncher(Point3D loc, String idIn, int numPredictors, int numTrackers) {
        super(loc, idIn, numPredictors, numTrackers);
        symbol = "fl";
        addSelf();
    }
}
