/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.domain.missiles;

import app.domain.Launcher;
import app.utils.RandomGenerator;
import java.util.ArrayList;
import utils.Point3D;
import utils.PropertyManager;

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
        super.launch(bi);
        plotCourse();
    }

    private void plotCourse() {
        Point3D targetLoc = target.getLocation();
        Point3D targetDest = predictDestination();
        double targetSpd = obfuscate(target.getSpeed());
        ArrayList<Point3D> pts = new ArrayList<>();
        double delta = 1.0 / PropertyManager.Instance().getDoubleProperty("PREDICTORSEGMENTS");
        for (int i = 0; i < PropertyManager.Instance().getDoubleProperty("PREDICTORSEGMENTS"); i++) {
            double newx = targetLoc.getX() + (targetDest.getX() - targetLoc.getX()) * (delta * (i + 1));
            double newy = targetLoc.getY() + (targetDest.getY() - targetLoc.getY()) * (delta * (i + 1));
            double newz = targetLoc.getZ() + (targetDest.getZ() - targetLoc.getZ()) * (delta * (i + 1));
            Point3D p = new Point3D(newx, newy, newz);
            double d = targetLoc.distance(p);
            double travelTime = d / targetSpd;
            if (verifyDest(p, travelTime)) {
                pts.add(p);
            }
        }
        if (!pts.isEmpty()) {
            Point3D closest = pts.get(0);
            for (Point3D p : pts) {
                if (p.distance(location) < closest.distance(location)) {
                    closest = p;
                }
            }
            destination = closest;
            speed = targetSpd * location.distance(destination) / targetLoc.distance(destination);
        } else {
            destination = location;
        }
    }

    private Point3D predictDestination() {
        double destx = obfuscate(target.getDestination().getX());
        double desty = obfuscate(target.getDestination().getY());
        double destz = obfuscate(target.getDestination().getZ());
        return new Point3D(destx, desty, destz);
    }

    private boolean verifyDest(Point3D p, double travelTime) {
        return (p.distance(location) / minSpeed) > travelTime
                && (p.distance(location) / maxSpeed) < travelTime
                && p.getZ() > PropertyManager.Instance().getDoubleProperty("MINALTITUDE");
    }

    private double obfuscate(double d) {
        return d * (1 - (RandomGenerator.Instance().getRandomNumber()
                * PropertyManager.Instance().getDoubleProperty("OBFUSCATIONFACTOR")));
    }
}
