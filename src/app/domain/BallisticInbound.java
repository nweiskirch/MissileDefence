/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.domain;

import app.managers.DetectorManager;
import app.managers.InboundManager;
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
public class BallisticInbound implements Displayable {

    protected final String id;
    protected Point3D location;
    protected Point3D destination;
    protected double speed;
    private double maxSpeed;
    private String symbol;
    private boolean isDecoy;

    public BallisticInbound(Point3D locationIn, Point3D destinationIn, double speedIn, String id, boolean isDecoy) {
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

    public Point3D getLocation() {
        return location;
    }

    public String getSymbol() {
        return symbol;
    }

    public void update(double millis) {
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

    public void selfDestruct() {
        symbol = isDecoy ? "o" : "x";
        InboundManager.getInstance().removeEntry(this);
        DisplayManager.getInstance().removeContent(this, PropertyManager.Instance().getIntProperty("REMOVALDELAY"));
        SoundUtility.getInstance().playSound("SelfDestruct.wav");
        LoggingManager.logInfo("BallisticInbound Self Destruct, ID = " + id);
    }

    public void alterCourse() {
        double alterVar = PropertyManager.Instance().getDoubleProperty("DESTALTERVARIATION");
        double xmod = (1 - alterVar) + (2 * alterVar * RandomGenerator.Instance().getRandomNumber());
        double ymod = (1 - alterVar) + (2 * alterVar * RandomGenerator.Instance().getRandomNumber());
        LoggingManager.logInfo("Original Destination (" + destination.x + ", " + destination.y + ", " + destination.z + ")");
        destination.x *= xmod;
        destination.y *= ymod;
        LoggingManager.logInfo("New Destination (" + destination.x + ", " + destination.y + ", " + destination.z + ")");
    }

    public void alterSpeed() {
        double alterVar = PropertyManager.Instance().getDoubleProperty("DESTALTERVARIATION");
        double smod = (1 - alterVar) + (2 * alterVar * RandomGenerator.Instance().getRandomNumber());
        speed *= smod;
    }

    public void lockDetected(Point3D point) {
        
    }

    private void arrived(){
        if(isDecoy){
            symbol = "O";
            SoundUtility.getInstance().playSound("Crunch.wav");
            LoggingManager.logInfo("Decoy has reached destination, ID = " + id);
        }
        else{
            applyGroundDamage();
            symbol = "X";
            SoundUtility.getInstance().playSound("Explosion.wav");
            LoggingManager.logInfo("Ballistic Inbound Misssile has reached destination, ID = " + id);
        }
        InboundManager.getInstance().removeEntry(this);
        DisplayManager.getInstance().removeContent(this, PropertyManager.Instance().getIntProperty("REMOVALDELAY"));
    }
    private void applyGroundDamage(){
        DetectorManager.getInstance().applyBlastDamage(location);
    }
}
