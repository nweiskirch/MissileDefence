/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.domain.mobile;

import app.domain.Launcher;
import app.utils.RandomGenerator;
import utils.LoggingManager;
import utils.Point3D;
import utils.PropertyManager;

/**
 *
 * @author Nate
 */
public class MobileLauncher extends Launcher {

    private double speed;
    private double maxSpeed;
    private Point3D destination;

    public MobileLauncher(Point3D loc, Point3D dest, double spd, String idIn, int numPredictors, int numTrackers) {
        super(loc, idIn, numPredictors, numTrackers);
        symbol = "ml";
        speed = spd;
        maxSpeed = spd;
        destination = dest;
        addSelf();
    }

    /**
     * Updates the state of the launcher.
     *
     * @param millis the time that has passed since the last call to update
     */
    public void update(double millis) {
        move(millis);
    }

    private void move(double millis) {
        double distTraveled = speed * millis / 1000;
        double distToDest = location.distance(destination);
        if (distToDest == 0) {
            return;
        }
        if (distTraveled >= distToDest) {
            location = (Point3D) destination.clone();
            arrived();
            return;
        }
        double delta = distTraveled / distToDest;
        location.x = location.x + (destination.x - location.x) * delta;
        location.y = location.y + (destination.y - location.y) * delta;
        location.z = location.z + (destination.z - location.z) * delta;
    }

    private void arrived() {
        double newx = RandomGenerator.Instance().getRandomNumber() * PropertyManager.Instance().getIntProperty("PIXELSX");
        double newy = RandomGenerator.Instance().getRandomNumber() * PropertyManager.Instance().getIntProperty("PIXELSY");
        destination.x = newx;
        destination.y = newy;
        LoggingManager.logInfo("Mobile Launcher " + id + " has reached its destination: "
                + location.toString() + "\nNew destination: ("
                + destination.toString());
    }
}
