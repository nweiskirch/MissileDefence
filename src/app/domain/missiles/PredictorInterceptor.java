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
public class PredictorInterceptor extends Interceptor {
    public PredictorInterceptor(Point3D location, double speed, double maxSpeed, String id, double minSpeed, Launcher launcher) {
        super(location, speed, maxSpeed, id, minSpeed, launcher);
        symbol = "P";
    }
}
