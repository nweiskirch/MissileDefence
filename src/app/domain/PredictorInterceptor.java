/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.domain;

import app.managers.LauncherManager;
import utils.LoggingManager;
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

    public void launch(BallisticInbound bi) throws Exception {
        target = bi;
        LauncherManager.getInstance().addToTarget(bi, null);
    }

    public void destroy() {
        LoggingManager.logInfo("Interceptor Detroyed");
    }

    public BallisticInbound getTarget() {
        return target;
    }
}
