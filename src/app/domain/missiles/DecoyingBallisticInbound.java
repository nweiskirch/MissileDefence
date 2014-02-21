/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.domain.missiles;

import utils.LoggingManager;
import utils.Point3D;

/**
 *
 * @author Nate
 */
public class DecoyingBallisticInbound extends BallisticInbound {

    private int decoyCount;

    /**
     * Constructs a Decoying Ballistic Inbound Missile
     * Decoying Ballistic Inbound Missiles will send out decoys if they detect that an
     * intercepting missile has locked on to them
     * @param locationIn The starting location
     * @param destinationIn The missiles destination
     * @param speedIn The missiles speed
     * @param id The missiles ID string
     * @param numDecoys The number of decoys the missile can deploy
     * @param isDecoy true if the missile is a decoy
     */
    public DecoyingBallisticInbound(Point3D locationIn, Point3D destinationIn, double speedIn, String id, int numDecoys, boolean isDecoy) {
        super(locationIn, destinationIn, speedIn, id, isDecoy);
        if(numDecoys < 0){
            decoyCount = 0;
            throw new NumberFormatException("Number of Decoys less than 0");
        }
        decoyCount = numDecoys;
        LoggingManager.logInfo("DecoyingBallisticInbound Created, ID = " + id);
    }

    /**
     * Called if a lock is detected. The missile will send out decoys
     * @param point the location of the intercepting missile
     */
    public void lockDetected(Point3D point) {
        double lockDistance = location.distance(point);
        double destDistance = location.distance(destination);
        int numDeploy = 0;
        if (lockDistance > .75 * destDistance) {
            if (decoyCount > 0) {
                numDeploy = 1;
            }
        } else if (lockDistance > .50 * destDistance) {
            numDeploy = decoyCount / 2;
        } else if (lockDistance > .25 * destDistance) {
            numDeploy = (decoyCount * 3) / 4;
        } else {
            numDeploy = decoyCount;
        }
        for (int i = 0; i < numDeploy; i++) {
            deployDecoy();
            decoyCount--;
        }
    }

    private void deployDecoy() {
        BallisticInbound decoy = new BallisticInbound((Point3D) location.clone(), (Point3D) destination.clone(), speed, id + "DECOY", true);
        decoy.alterCourse();
        decoy.alterSpeed();
        decoyCount--;
        LoggingManager.logInfo("Decoy Deployed, ID = " + id + "DECOY");
    }
}
