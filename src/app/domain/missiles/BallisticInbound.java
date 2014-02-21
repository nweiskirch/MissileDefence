/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.domain.missiles;

import app.managers.DetectorManager;
import app.managers.InboundManager;
import app.managers.LauncherManager;
import app.utils.RandomGenerator;
import display.interfaces.Displayable;
import display.managers.DisplayManager;
import utils.LoggingManager;
import utils.Point3D;
import utils.PropertyManager;
import utils.SoundUtility;

/**
 * The basic inbound missile. It will move from its location to its destination and then detonate
 * @author Nate
 */
public class BallisticInbound implements Displayable {

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
    private String symbol;
    private boolean isDecoy;

    /**
     *  Creates a Ballistic Inbound Missile based on the given parameters
     * @param locationIn The start location
     * @param destinationIn The missiles destination
     * @param speedIn The missiles speed
     * @param id The ID string for the missile
     * @param isDecoy true if the missile is a decoy
     */
    public BallisticInbound(Point3D locationIn, Point3D destinationIn, double speedIn, String id, boolean isDecoy) {
        if(speedIn < 0){
            speed = 0;
            throw new NumberFormatException("Speed less than 0");
        }
        this.id = id;
        location = locationIn;
        destination = destinationIn;
        speed = speedIn;
        this.isDecoy = isDecoy;
        symbol = isDecoy ? "d" : "B";
        maxSpeed = speed;//????

        DisplayManager.getInstance().addContent(this);
        InboundManager.getInstance().addEntry(this);
        SoundUtility.getInstance().playSound("Inbound.wav");
        LoggingManager.logInfo("BallisticInbound Created, ID = " + id);
    }

    /**
     *  
     * @return The location of the missile
     */
    public Point3D getLocation() {
        return location;
    }
    
    public Point3D getDestination(){
        return destination;
    }

    public String getId() {
        return id;
    }

    public double getSpeed() {
        return speed;
    }

    /**
     *
     * @return The missiles symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Updates the location of the missile and detonates if it has reached its destination
     * @param millis the time that has passed since the last call to update()
     */
    public void update(double millis) {
        if(millis < 0){
            millis = 0;
            throw new NumberFormatException("Time less than 0");
        }
        double distTraveled = speed * millis/1000;
        double distToDest = location.distance(destination);
        if(distToDest == 0){
            return;
        }
        if(distTraveled >= distToDest){
            location = destination;
            arrived();
            return;
        }
        double delta = distTraveled / distToDest;
        location.x = location.x + (destination.x-location.x)*delta;
        location.y = location.y + (destination.y-location.y)*delta;
        location.z = location.z + (destination.z-location.z)*delta;
    }

    /**
     *  The missile will self destruct
     */
    public void selfDestruct() {
        symbol = isDecoy ? "o" : "x";
        InboundManager.getInstance().removeEntry(this);
        DisplayManager.getInstance().removeContent(this, PropertyManager.Instance().getIntProperty("REMOVALDELAY"));
        SoundUtility.getInstance().playSound("SelfDestruct.wav");
        LoggingManager.logInfo("BallisticInbound Self Destruct, ID = " + id);
    }

    /**
     * The missile will slightly change its destination
     */
    public void alterCourse() {
        double alterVar = PropertyManager.Instance().getDoubleProperty("DESTALTERVARIATION");
        double xmod = (1 - alterVar) + (2 * alterVar * RandomGenerator.Instance().getRandomNumber());
        double ymod = (1 - alterVar) + (2 * alterVar * RandomGenerator.Instance().getRandomNumber());
        LoggingManager.logInfo("Course altered\nOriginal Destination " + destination.toString());
        destination.x *= xmod;
        destination.y *= ymod;
        LoggingManager.logInfo("New Destination " + destination.toString());
    }

    /**
     * The missile will slightly change its speed
     */
    public void alterSpeed() {
        double alterVar = PropertyManager.Instance().getDoubleProperty("DESTALTERVARIATION");
        double smod = (1 - alterVar) + (2 * alterVar * RandomGenerator.Instance().getRandomNumber());
        speed *= smod;
    }

    /**
     * Called when the missile detects an approaching intercepter
     * @param point the location of the intercepter
     */
    public void lockDetected(Point3D point) {
        
    }

    private void arrived(){
        if(isDecoy){
            symbol = "O";
            SoundUtility.getInstance().playSound("Crunch.wav");
            LoggingManager.logInfo("Decoy " + id + " has reached destination: " + destination.toString());
        }
        else{
            applyGroundDamage();
            symbol = "X";
            SoundUtility.getInstance().playSound("Explosion.wav");
            LoggingManager.logInfo("Ballistic Inbound Misssile" + id + " has reached destination: " + destination.toString());
        }
        InboundManager.getInstance().removeEntry(this);
        DisplayManager.getInstance().removeContent(this, PropertyManager.Instance().getIntProperty("REMOVALDELAY"));
    }
    private void applyGroundDamage(){
        DetectorManager.getInstance().applyBlastDamage(location);
        LauncherManager.getInstance().applyBlastDamage(location);
    }
}
