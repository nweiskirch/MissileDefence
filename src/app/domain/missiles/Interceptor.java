/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.domain.missiles;

import app.domain.Launcher;
import app.managers.InboundManager;
import app.managers.InterceptorManager;
import app.managers.LauncherManager;
import app.utils.RandomGenerator;
import display.interfaces.Displayable;
import display.managers.DisplayManager;
import utils.LoggingManager;
import utils.Point3D;
import utils.PropertyManager;
import utils.SoundUtility;

/**
 *
 * @author Nate
 */
public abstract class Interceptor implements Displayable {

    /**
     * The ID for the missile
     */
    protected final String id;
    /**
     * The current location
     */
    protected Point3D location;
    /**
     * The missiles destination
     */
    protected Point3D destination;
    /**
     * The speed at which it travels
     */
    protected double speed;
    protected double maxSpeed;
    protected double minSpeed;
    protected String symbol;
    private Launcher launcher;
    protected BallisticInbound target;

    public Interceptor(Point3D location, double speed, double maxSpeed, String id, double minSpeed, Launcher launcher) {
        this.id = id;
        this.location = location;
        this.speed = speed;
        this.maxSpeed = maxSpeed;
        this.minSpeed = minSpeed;
        this.launcher = launcher;
        target = null;
        destination = location;
    }

    public void update(double millis) {
        move(millis);
    }

    protected void move(double millis) {
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
        SoundUtility.getInstance().playSound("Strike.wav");
        symbol = "*";
        InboundManager.getInstance().interceptorStrike(this);
        LauncherManager.getInstance().removeFromTarget(target, this);
        InterceptorManager.getInstance().removeEntry(this);
        DisplayManager.getInstance().removeContent(this, PropertyManager.Instance().getIntProperty("REMOVALDELAY"));
    }

    public void launch(BallisticInbound bi) throws Exception {
        LauncherManager.getInstance().addToTarget(bi, this);
        InterceptorManager.getInstance().addEntry(this);
        DisplayManager.getInstance().addContent(this);
        SoundUtility.getInstance().playSound("Launch.wav");
        location = new Point3D(launcher.getLocation());
        target = bi;
        if (PropertyManager.Instance().getDoubleProperty("LOCKDETECTED") < RandomGenerator.Instance().getRandomNumber()) {
            bi.lockDetected(location);
        }
    }

    public String getId() {
        return id;
    }

    public Point3D getDestination() {
        return destination;
    }

    public double getSpeed() {
        return speed;
    }

    public String getSymbol() {
        return symbol;
    }

    protected void selfDestruct() {
        LoggingManager.logInfo("Interceptor " + id + " self destructed.");
        SoundUtility.getInstance().playSound("SelfDestruct.wav");
        target = null;
        destination = null;
        InterceptorManager.getInstance().removeEntry(this);
        symbol = "^";
        DisplayManager.getInstance().removeContent(this, PropertyManager.Instance().getIntProperty("REMOVALDELAY"));
    }

    public void destroy() {
        LoggingManager.logInfo("Interceptor " + id + " destroyed.");
    }

    public BallisticInbound getTarget() {
        return target;
    }

    public Point3D getLocation() {
        return location;
    }
}
