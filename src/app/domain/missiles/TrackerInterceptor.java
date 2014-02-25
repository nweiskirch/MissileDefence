/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.domain.missiles;

import app.domain.Launcher;
import app.managers.InboundManager;
import app.managers.InterceptorManager;
import app.utils.RandomGenerator;
import utils.LoggingManager;
import utils.Point3D;
import utils.PropertyManager;

/**
 *
 * @author Nate
 */
public class TrackerInterceptor extends Interceptor {

    double fuelTime;
    double detectionRange;

    public TrackerInterceptor(Point3D location, double speed, double maxSpeed, String id,
            Launcher launcher, double fuelTime, double detectionRange) {
        super(location, speed, maxSpeed, id, 0, launcher);
        symbol = "T";
        this.fuelTime = fuelTime;
        this.detectionRange = detectionRange;
    }

    public void update(double millis) {
        if (InboundManager.getInstance().contains(target) && InterceptorManager.getInstance().contains(this)) {
            fuelTime -= millis / 1000;
            if (fuelTime < 0) {
                selfDestruct();
                return;
            }
            speed += maxSpeed * ((millis/1000)/fuelTime);
            if (verifyAcquire()) {
                destination = target.getLocation();
                move(millis);
            }
        } else {
            LoggingManager.logInfo("Interceptor has no target. Acquiring new target");
            if (InboundManager.getInstance().getNumEntries() > 0) {
                acquire();
            } else {
                selfDestruct();
            }
        }
    }

    private boolean acquire() {
        BallisticInbound bi = InboundManager.getInstance().getClosest(location);
        if (bi.getLocation().distance(location) > detectionRange) {
            selfDestruct();
            return false;
        }
        if (bi != target) {
            target = bi;
            if (PropertyManager.Instance().getDoubleProperty("LOCKDETECTED") < RandomGenerator.Instance().getRandomNumber()) {
                bi.lockDetected(location);
            }
        }
        return true;
    }

    private boolean verifyAcquire() {
        if (RandomGenerator.Instance().getRandomNumber() < PropertyManager.Instance().getDoubleProperty("REVERIFYACQUIRE")) {
            return acquire();
        }
        return true;
    }
}
