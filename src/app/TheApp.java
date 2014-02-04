package app;

import app.domain.BallisticInbound;
import app.domain.DecoyingBallisticInbound;
import app.domain.Launcher;
import app.domain.mobile.MobileDetector;
import app.domain.fixed.FixedDetector;
import app.managers.InboundManager;
import app.managers.DetectorManager;
import app.utils.RandomGenerator;
import display.interfaces.ViewFrameListener;
import display.managers.DisplayManager;
import utils.LoggingManager;
import utils.Point3D;

import java.util.ArrayList;

/**
 *  The engine of the application. It contains all of the tests
 * @author Nate
 */
public class TheApp implements Runnable, ViewFrameListener
{
    private boolean paused = false;
    private boolean running = true;

    /**
     *  Constructs a TheApp object
     */
    public TheApp()
    {
        DisplayManager.getInstance().setViewFrameListener(this);
    }

    public void run()
    {
        while (running)
        {
            synchronized (this)
            {
                try
                {
                    Thread.sleep(50);
                    if (!paused)
                    {
                        InboundManager.getInstance().update(1000);
                        DetectorManager.getInstance().update(1000);
                    }
                    DisplayManager.getInstance().updateDisplay();
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                catch (Exception e) // This should change to catch your own exception classes
                {
                    e.printStackTrace();
                }
            }
        }
        LoggingManager.logInfo("Exiting the app");
        System.exit(0);
    }

    /**
     *  Runs the tests for the Fixed Detectors
     * @throws InterruptedException
     */
    public void runFDTests() throws InterruptedException
    {
        String message;
	    acceptPause();

        ArrayList associatedLaunchers = new ArrayList();
        associatedLaunchers.add(new Launcher());
	    associatedLaunchers.add(new Launcher());

        message = "Test 1: Creating a new FixedDetector - Screen Symbol is a Red 'FD' (No Sounds)";
        DisplayManager.getInstance().popUpInfo(message + ". \n\nClick OK to proceed with test......\n");
        LoggingManager.logInfo(message);
        new FixedDetector(new Point3D(300.0, 150.0, 0.0), "Fixed Detector 1", 20000.0, associatedLaunchers);
	    acceptPause();
        Thread.sleep(2000);
	    acceptPause();
        LoggingManager.logInfo("Test 1: Completed");



        message = "Test 2: FixedDetector 'update' Test - No Detections - No Visible Results";
        DisplayManager.getInstance().popUpInfo(message + ". \n\nClick OK to proceed with test......\n");
        LoggingManager.logInfo(message);
	    acceptPause();
        Thread.sleep(3000);
	    acceptPause();
        LoggingManager.logInfo("Test 2: Completed");


        message = "Test 3: FixedDetector Detection Test - BallisticInbound Should Be Detected (detections seen in  printed log)";
        DisplayManager.getInstance().popUpInfo(message + ". \n\nClick OK to proceed with test......\n");
        LoggingManager.logInfo(message);
	    new BallisticInbound(new Point3D(100.0, 350.0, 8000.0),
	                                               new Point3D(300.0, 150.0, 0.0), 150,
	                                               "Ballistic Inbound 1", false);
	    acceptPause();
        Thread.sleep(1000);
        acceptPause();
        LoggingManager.logInfo("Test 3: Completed");


        message = "Test 4: FixedDetector Destruction Test - BallisticInbound Destroys FixedDetector - Sounds Play";
        DisplayManager.getInstance().popUpInfo(message + ". \n\nClick OK to proceed with test......\n");
        LoggingManager.logInfo(message);
        acceptPause();
        waitUntilNoInbounds();
        acceptPause();
        LoggingManager.logInfo("Test 4: Completed");


        message = "Test 5: Creating Another FixedDetector - Screen Symbol is a Red 'FD'";
        DisplayManager.getInstance().popUpInfo(message + ". \n\nClick OK to proceed with test......\n");
        LoggingManager.logInfo(message);
        new FixedDetector(new Point3D(200.0, 250.0, 0.0), "Fixed Detector 2", 20000.0, associatedLaunchers);
	    acceptPause();
        Thread.sleep(3000);
	    acceptPause();
        LoggingManager.logInfo("Test 5: Completed");


	    message = "Test 6: FixedDetector Detection Test - BallisticInbound Should Be Not Detected (no detections seen in printed log).";
	    DisplayManager.getInstance().popUpInfo(message + ". \n\nClick OK to proceed with test......\n");
	    LoggingManager.logInfo(message);
		new BallisticInbound(new Point3D(400.0, 450.0, 25000.0),
		                                           new Point3D(200.0, 250.0, 0.0), 150,
		                                           "Ballistic Inbound 2", false);
		acceptPause();
	    Thread.sleep(2000);
	    acceptPause();
	    LoggingManager.logInfo("Test 6: Completed");


        message = "Test 7: FixedDetector Detection Test - BallisticInbound Moves into Detection Range & Detonates - (detections seen in printed log).";
        DisplayManager.getInstance().popUpInfo(message + ". \n\nClick OK to proceed with test......\n");
        LoggingManager.logInfo(message);
        acceptPause();
        waitUntilNoInbounds();
        acceptPause();
        LoggingManager.logInfo("Test 7: Completed");

    }

    /**
     *  Runs tests for the mobile detectors
     * @throws InterruptedException
     */
    public void runMDTests() throws InterruptedException
    {
        String message;

        ArrayList associatedLaunchers = new ArrayList();
        associatedLaunchers.add(new Launcher());
	    associatedLaunchers.add(new Launcher());

        message = "Test 8: Creating a new MobileDetector - Screen Symbol is a Red 'MD'";
        DisplayManager.getInstance().popUpInfo(message + ". \n\nClick OK to proceed with test......\n");
        LoggingManager.logInfo(message);
        MobileDetector md = new MobileDetector(new Point3D(250.0, 200.0, 0.0), new Point3D(100.0, 350.0, 0.0),
                                               3.0, "Mobile Detector 1", 20000.0, associatedLaunchers);
        Thread.sleep(3000);
        LoggingManager.logInfo("Test 8: Completed");



        message = "Test 9: MobileDetector Update Test - No Detections - Detector Moves";
        DisplayManager.getInstance().popUpInfo(message + ". \n\nClick OK to proceed with test......\n");
        LoggingManager.logInfo(message);
	    acceptPause();
        Thread.sleep(2000);
	    acceptPause();
        LoggingManager.logInfo("Test 9: Completed");

        message = "Test 10: MobileDetector Movement Test - Destination Reached - Moves to New Destination";
        DisplayManager.getInstance().popUpInfo(message + ". \n\nClick OK to proceed with test......\n");
        LoggingManager.logInfo(message);
	    acceptPause();
        Thread.sleep(4000);
	    acceptPause();
        LoggingManager.logInfo("Test 10: Completed");

        message = "Test 11: MobileDetector Detection - Destroy";
        DisplayManager.getInstance().popUpInfo(message + ". \n\nClick OK to proceed with test......\n");
        LoggingManager.logInfo(message);
	    acceptPause();
	    md.destroy();
        Thread.sleep(1000);
        acceptPause();
        LoggingManager.logInfo("Test 11: Completed");

        message = "Test 12: Creating a new MobileDetector - Set speed to 0.0 (No movement seen).";
        DisplayManager.getInstance().popUpInfo(message + ". \n\nClick OK to proceed with test......\n");
        LoggingManager.logInfo(message);
        md = new MobileDetector(new Point3D(250.0, 550.0, 0.0), new Point3D(70.0, 405.0, 0.0),
                                               0.0, "Mobile Detector 2", 20000.0, associatedLaunchers);
	    acceptPause();
        Thread.sleep(2000);
	    acceptPause();
        LoggingManager.logInfo("Test 13: Completed");


	    message = "Test 13: MobileDetector Detection Test - New BallisticInbound Should Be Detected (detections seen in printed log).";
	    DisplayManager.getInstance().popUpInfo(message + ". \n\nClick OK to proceed with test......\n");
	    LoggingManager.logInfo(message);
		new BallisticInbound(new Point3D(75.0, 300.0, 5000.0),
		                                           new Point3D(250.0, 550.0, 0.0), 200,
		                                           "Ballistic Inbound 3", false);
		acceptPause();
	    Thread.sleep(1000);
	    acceptPause();
	    LoggingManager.logInfo("Test 13: Completed");


        message = "Test 14: MobileDetector Destruction Test - BallisticInbound Destroys MobileDetector";
        DisplayManager.getInstance().popUpInfo(message + ". \n\nClick OK to proceed with test......\n");
        LoggingManager.logInfo(message);
        acceptPause();
        waitUntilNoInbounds();
        acceptPause();
        LoggingManager.logInfo("Test 14: Completed");

        message = "Test 15: Creating Another MobileDetector - Set Speed to 0.0 (No movement seen).";
        DisplayManager.getInstance().popUpInfo(message + ". \n\nClick OK to proceed with test......\n");
        LoggingManager.logInfo(message);
	    md = new MobileDetector(new Point3D(450.0, 450.0, 0.0), new Point3D(70.0, 405.0, 0.0),
                                               0.0, "Mobile Detector 3", 20000.0, associatedLaunchers);
        acceptPause();
        Thread.sleep(1000);
        acceptPause();
        LoggingManager.logInfo("Test 15: Completed");

        message = "Test 16: MobileDetector Detection Test - BallisticInbound Should Not Be Detected (no detection seen in printed log)";
        DisplayManager.getInstance().popUpInfo(message + ". \n\nClick OK to proceed with test......\n");
        LoggingManager.logInfo(message);
	    		new BallisticInbound(new Point3D(75.0, 75, 25000.0),
		                                           new Point3D(450.0, 450.0, 0.0), 200,
		                                           "Ballistic Inbound 4", false);
        acceptPause();
        Thread.sleep(1000);
        acceptPause();
        LoggingManager.logInfo("Test 16: Completed");

	    message = "Test 17: MobileDetector Detection Test - BallisticInbound Moves Into Detection Range & Detonates (detection seen in printed log)";
	    DisplayManager.getInstance().popUpInfo(message + ". \n\nClick OK to proceed with test......\n");
	    LoggingManager.logInfo(message);
	    acceptPause();
	    waitUntilNoInbounds();
	    acceptPause();
	    LoggingManager.logInfo("Test 17: Completed");


    }


    private void waitUntilNoInbounds() throws InterruptedException
    {
        int cnt = 0;
        while (InboundManager.getInstance().getNumEntries() > 0)
        {
            if ((cnt++ % 5) == 0)
            {
                LoggingManager.logInfo("Waiting for Test Completion...");
            }
            Thread.sleep(1000);
        }
    }

    /**
     *
     */
    public void acceptPause()
    {

        if (paused)
        {
	        LoggingManager.logInfo("App. Resumed!");
            paused = false;
        }
        else
        {
	        LoggingManager.logInfo("App. Paused!");
            paused = true;
        }
    }

    /**
     *  Stop the app
     */
    public synchronized void acceptStop()
    {
        LoggingManager.logInfo("App. Stopped!");
        running = false;
        notify();
    }

    /**
     *  Close the app
     */
    public void acceptClose()
    {
        LoggingManager.logInfo("App. Closed!");
        acceptStop();
    }

    /**
     *  Executed when the left mouse button is clicked. Will create a Ballistic Inbound Missile
     * @param pressX the starting x position
     * @param pressY the starting y position
     * @param releaseX the ending x position
     * @param releaseY the ending y position
     */
    public void acceptLeftButtonMouseCommand(int pressX, int pressY, int releaseX, int releaseY)
    {
        LoggingManager.logInfo("Left Mouse Button [" + pressX + ", " + pressY + " " + releaseX + ", " + releaseY + "]");
        try
        {
            int id = (int) (RandomGenerator.Instance().getRandomNumber() * 10000.0);
            new BallisticInbound(new Point3D(pressX, pressY, 50000), new Point3D(releaseX, releaseY, 0),
                                 150.0, "Ballistic Inbound " + id, false);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     *  Executed when the right mouse button is clicked. Will Create a Decoying Ballistic Inbound Missile
     * @param pressX the starting x position
     * @param pressY the starting y position
     * @param releaseX the ending x position
     * @param releaseY the ending y position
     */
    public void acceptRightButtonMouseCommand(int pressX, int pressY, int releaseX, int releaseY)
    {
        LoggingManager.logInfo("Left Mouse Button [" + pressX + ", " + pressY + " " + releaseX + ", " + releaseY + "]");
        try
        {
            int id = (int) (RandomGenerator.Instance().getRandomNumber() * 10000.0);
            new DecoyingBallisticInbound(new Point3D(pressX, pressY, 50000),
                                         new Point3D(releaseX, releaseY, 0), 150.0,
                                         "Decoying Ballistic Inbound " + id, 5, false);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
