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
import java.util.ArrayList;
import utils.LoggingManager;
import utils.Point3D;
import utils.PropertyManager;
import utils.SoundUtility;

/**
 *
 * @author Nate
 */
public abstract class Interceptor implements Displayable{
    
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
    private double maxSpeed;
    private double minSpeed;
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
    
    public void update(double millis){
        move(millis);
    }
    
    private void move(double millis){
        double distTraveled = speed * millis/1000;
        double distToDest = location.distance(destination);
        if(distToDest == 0){
            return;
        }
        if(distTraveled >= distToDest){
            location = (Point3D) destination.clone();
            arrived();
            return;
        }
        double delta = distTraveled / distToDest;
        location.x = location.x + (destination.x-location.x)*delta;
        location.y = location.y + (destination.y-location.y)*delta;
        location.z = location.z + (destination.z-location.z)*delta;
    }
    
    private void arrived(){
        SoundUtility.getInstance().playSound("Strike.wav");
        symbol = "*";
        InboundManager.getInstance().interceptorStrike(this);
        LauncherManager.getInstance().removeFromTarget(target, this);
        InterceptorManager.getInstance().removeEntry(this);
        DisplayManager.getInstance().removeContent(this, PropertyManager.Instance().getIntProperty("REMOVALDELAY"));
    }
    
    public void launch(BallisticInbound bi) throws Exception{
        LauncherManager.getInstance().addToTarget(bi, this);
        InterceptorManager.getInstance().addEntry(this);
        DisplayManager.getInstance().addContent(this);
        SoundUtility.getInstance().playSound("Launch.wav");
        location = launcher.getLocation();
        target = bi;
        plotCourse();
        if(PropertyManager.Instance().getDoubleProperty("LOCKDETECTED")<RandomGenerator.Instance().getRandomNumber()){
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

    private void plotCourse(){
        Point3D targetLoc = target.getLocation();
        Point3D targetDest = predictDestination();
        double targetSpd = obfuscate(target.getSpeed());
        ArrayList<Point3D> pts = new ArrayList<>();
        double delta = 1.0/PropertyManager.Instance().getDoubleProperty("PREDICTORSEGMENTS");
        for(int i = 0; i<PropertyManager.Instance().getDoubleProperty("PREDICTORSEGMENTS"); i++){
            double newx = targetLoc.getX()+(targetDest.getX() - targetLoc.getX())*(delta * (i + 1));
            double newy = targetLoc.getY()+(targetDest.getY() - targetLoc.getY())*(delta * (i + 1));
            double newz = targetLoc.getZ()+(targetDest.getZ() - targetLoc.getZ())*(delta * (i + 1));
            Point3D p = new Point3D(newx, newy, newz);
            double d = targetLoc.distance(p);
            double travelTime = d/targetSpd;
            if(verifyDest(p, travelTime)){
                pts.add(p);
            }
        }
        if(!pts.isEmpty()){
            Point3D closest = pts.get(0);
            for(Point3D p: pts){
                if(p.distance(location)<closest.distance(location)){
                    closest = p;
                }
            }
            destination = closest;
            speed = targetSpd*location.distance(destination)/targetLoc.distance(destination);
        } else {
            destination = location;
        }
    }
    
    private Point3D predictDestination(){
        double destx = obfuscate(target.getDestination().getX());
        double desty = obfuscate(target.getDestination().getY());
        double destz = obfuscate(target.getDestination().getZ());
        return new Point3D(destx, desty, destz);
    }
    
    private boolean verifyDest(Point3D p, double travelTime){
        return (p.distance(location)/minSpeed) > travelTime && 
                (p.distance(location)/maxSpeed) < travelTime &&
                p.getZ()<PropertyManager.Instance().getDoubleProperty("MINALTITUDE");
    }
    
    private double obfuscate(double d){
        return d*(1 - (RandomGenerator.Instance().getRandomNumber()*
                PropertyManager.Instance().getDoubleProperty("OBFUSCATIONFACTOR")));
    }
    
    public String getSymbol() {
        return symbol;
    }
    
    protected void selfDestruct(){
        LoggingManager.logInfo("Interceptor "+ id + " self destructed.");
        SoundUtility.getInstance().playSound("SelfDestruct.wav");
        target = null;
        destination = null;
        InterceptorManager.getInstance().removeEntry(this);
        symbol = "^";
        DisplayManager.getInstance().removeContent(this, PropertyManager.Instance().getIntProperty("REMOVALDELAY"));
    }   
    
    public  void destroy(){
        LoggingManager.logInfo("Interceptor "+ id + " destroyed.");
    }
    public BallisticInbound getTarget(){
        return target;
    }

    public Point3D getLocation() {
        return location;
    }
    
}
