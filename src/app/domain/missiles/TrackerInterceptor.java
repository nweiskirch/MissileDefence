/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.domain.missiles;

import app.domain.Launcher;
import utils.Point3D;

/**
 *
 * @author Nate
 */
public class TrackerInterceptor extends Interceptor {
    double fuelTime;
    double detectionRange;
    public TrackerInterceptor(Point3D location, double speed, double maxSpeed, String id, Launcher launcher, double fuelTime, double detectionRange) {
        super(location, speed, maxSpeed, id, 0, launcher);
        symbol = "T";
    }
}
