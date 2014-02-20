/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.domain;

import app.managers.InterceptorManager;
import app.managers.LauncherManager;
import app.utils.RandomGenerator;
import display.interfaces.Displayable;
import display.managers.DisplayManager;
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
        //TODO
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

    private void plotCourse(){
        
    }
    
    public String getSymbol() {
        return symbol;
    }
    
    protected void selfDestruct(){
        
    }   
    
    public  void destroy(){
        
    }
    public BallisticInbound getTarget(){
        return target;
    }

    public Point3D getLocation() {
        return location;
    }
    
}
