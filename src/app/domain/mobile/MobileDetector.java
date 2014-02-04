/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package app.domain.mobile;

import app.domain.BallisticInbound;
import app.domain.Launcher;
import app.interfaces.Detector;
import app.managers.InboundManager;
import app.utils.RandomGenerator;
import java.util.ArrayList;
import utils.LoggingManager;
import utils.Point3D;
import utils.PropertyManager;

/**
 * A mobile detector is a detector that can move.
 * @author Nate
 */
public class MobileDetector extends Detector{
    
    private Point3D destination;
    private double speed;
    private double maxSpeed;
    
    /**
     * Creates the Mobile Detector
     * @param locationIn
     * @param destinationIn
     * @param speedIn
     * @param idIn
     * @param rangeIn
     * @param associatedLaunchers
     */
    public MobileDetector(Point3D locationIn, Point3D destinationIn, double speedIn, String idIn, double rangeIn, ArrayList<Launcher> associatedLaunchers) {
        location = locationIn;
        destination = destinationIn;
        speed = speedIn;
        maxSpeed = speedIn;
        id = idIn;
        range = rangeIn;
        launchers = associatedLaunchers;
        symbol = "MD";
        addSelf();
        LoggingManager.logInfo("Mobile Detector Created, ID = " + id);
    }
    
    /**
     * Updates the state of the detector. Will move towards the destination and then pick 
     * a new random destination when reached. It will also detect inbound missiles
     * @param millis the time that has passed since the last call to update()
     */
    public void update(double millis){
        move(millis);
        ArrayList<BallisticInbound> detected = InboundManager.getInstance().detect(this);
        for(int i = 0; i<detected.size(); i++){
            LoggingManager.logInfo("BallisticInbound Detected, Initiating Launch");
        }
        for(int i = 0; i<launchers.size(); i++){
            launchers.get(i).update(millis);
        }
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
        double newx = RandomGenerator.Instance().getRandomNumber()*PropertyManager.Instance().getIntProperty("PIXELSX");
        double newy = RandomGenerator.Instance().getRandomNumber()*PropertyManager.Instance().getIntProperty("PIXELSY");
        destination.x = newx;
        destination.y = newy;
        LoggingManager.logInfo("Mobile Detector " + id + " has reached its destination: " 
                + location.toString() + "\nNew destination: ("
                + destination.toString());
    }
}
