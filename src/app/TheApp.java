package app;

import app.domain.BallisticInbound;
import app.domain.DecoyingBallisticInbound;
import app.managers.InboundManager;
import app.utils.RandomGenerator;
import display.interfaces.ViewFrameListener;
import display.managers.DisplayManager;
import utils.LoggingManager;
import utils.Point3D;

public class TheApp implements Runnable, ViewFrameListener
{
    private boolean paused = false;
    private boolean running = true;

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

    public void runIntegratedTests() throws Exception
    {
        try
        {
            runBITests();
            runDBITests();
            runMouseTests();

            waitUntilNoInbounds();


            DisplayManager.getInstance().popUpInfo("Tests Complete. \n\nPlease press the 'Stop' Button to Exit\n\n");
            LoggingManager.logInfo("Tests Complete.");

        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    private void runBITests() throws InterruptedException
    {
        String message;

        acceptPause();
        message = "Test 1: Creating a new BallisticInbound (with Sound Effect) - Screen Symbol is a Green 'B'";
        DisplayManager.getInstance().popUpInfo(message + ". \n\nClick OK to proceed with test......\n");
        LoggingManager.logInfo(message);
        BallisticInbound d = new BallisticInbound(new Point3D(300.0, 50.0, 50000.0), new Point3D(300.0, 550.0, 0.0),
                                                  150.0, "Ballistic Inbound", false);
        Thread.sleep(1000);
        LoggingManager.logInfo("Test 1: Completed");


        message = "Test 2: BallisticInbound Movement Test - 3 seconds";
        DisplayManager.getInstance().popUpInfo(message + ". \n\nClick OK to proceed with test......\n");
        LoggingManager.logInfo(message);
        acceptPause();
        Thread.sleep(3000);
        acceptPause();
        LoggingManager.logInfo("Test 2: Completed");


        message = "Test 3: BallisticInbound Alter Course Test (a Small Direction Change will Result)";
        DisplayManager.getInstance().popUpInfo(message + ". \n\nClick OK to proceed with test......\n");
        LoggingManager.logInfo(message);
        acceptPause();
        Thread.sleep(1500);
        d.alterCourse();
        Thread.sleep(1500);
        acceptPause();
        LoggingManager.logInfo("Test 3: Completed");


        message = "Test 4: BallisticInbound Lock Detected Test (No Effect)";
        DisplayManager.getInstance().popUpInfo(message + ". \n\nClick OK to proceed with test......\n");
        LoggingManager.logInfo(message);
        acceptPause();
        Thread.sleep(1500);
        d.lockDetected(d.getLocation());
        Thread.sleep(1500);
        acceptPause();
        LoggingManager.logInfo("Test 4: Completed");


        message = "Test 5: BallisticInbound Self Destruct Test (with Sound Effect)\n" +
                  "Screen Symbol Changed from Yellow 'B' to Yellow 'x'";
        DisplayManager.getInstance().popUpInfo(message + ". \n\nClick OK to proceed with test......\n");
        LoggingManager.logInfo(message);
        acceptPause();
        Thread.sleep(1500);
        d.selfDestruct();
        Thread.sleep(1500);
        acceptPause();
        LoggingManager.logInfo("Test 5: Completed");

        message = "Test 6: BallisticInbound - Full Flight With Detonation Test (Single) - With 2 Sound Effects\n" +
                  "Screen Symbol Shifts from Green to Red";
        DisplayManager.getInstance().popUpInfo(message + ". \n\nClick OK to proceed with test......\n");
        LoggingManager.logInfo(message);
        new BallisticInbound(new Point3D(20.0, 200.0, 50000.0), new Point3D(550.0, 550.0, 0.0),
                             250.0, "Ballistic Inbound", false);
        acceptPause();
        waitUntilNoInbounds();
        acceptPause();
        LoggingManager.logInfo("Test 6: Completed");

        message = "Test 7: BallisticInbound - Full Flight With Detonation Test (Multiple) - With Many Sound Effects\n" +
                  "Screen Symbols Shift from Green to Red";
        DisplayManager.getInstance().popUpInfo(message + ". \n\nClick OK to proceed with test......\n");
        LoggingManager.logInfo(message);
        new BallisticInbound(new Point3D(100.0, 20.0, 50000.0), new Point3D(100.0, 550.0, 0.0),
                             250.0, "Ballistic Inbound 1", false);
        new BallisticInbound(new Point3D(200.0, 120.0, 45000.0), new Point3D(200.0, 550.0, 0.0),
                             250.0, "Ballistic Inbound 2", false);
        new BallisticInbound(new Point3D(300.0, 220.0, 40000.0), new Point3D(300.0, 550.0, 0.0),
                             250.0, "Ballistic Inbound 3", false);
        new BallisticInbound(new Point3D(400.0, 320.0, 35000.0), new Point3D(400.0, 550.0, 0.0),
                             250.0, "Ballistic Inbound 4", false);
        acceptPause();
        waitUntilNoInbounds();
        acceptPause();
        LoggingManager.logInfo("Test 7: Completed");
        acceptPause();

    }

    private void runDBITests() throws InterruptedException
    {
        acceptPause();
        String message;
        message = "Test 8: Creating a new DecoyingBallisticInbound (with Sound Effect) - Screen Symbol is 'B'";
        DisplayManager.getInstance().popUpInfo(message + ". \n\nClick OK to proceed with test......\n");
        LoggingManager.logInfo(message);
        DecoyingBallisticInbound di = new DecoyingBallisticInbound(new Point3D(30.0, 500.0, 50000.0), new Point3D(420.0, 50.0, 0.0),
                                                                   150.0, "Ballistic Inbound", 5, false);
        Thread.sleep(1000);
        LoggingManager.logInfo("Test 8: Completed");


        message = "Test 9: DecoyingBallisticInbound Movement Test - 3 seconds";
        DisplayManager.getInstance().popUpInfo(message + ". \n\nClick OK to proceed with test......\n");
        LoggingManager.logInfo(message);
        acceptPause();
        Thread.sleep(3000);
        acceptPause();
        LoggingManager.logInfo("Test 9: Completed");


        message = "Test 10: DecoyingBallisticInbound Alter Course Test (a Small Direction Change will Result)";
        DisplayManager.getInstance().popUpInfo(message + ". \n\nClick OK to proceed with test......\n");
        LoggingManager.logInfo(message);
        acceptPause();
        Thread.sleep(1500);
        di.alterCourse();
        Thread.sleep(1500);
        acceptPause();
        LoggingManager.logInfo("Test 10: Completed");


        message = "Test 11: DecoyingBallisticInbound Lock Detected Test (5 Decoy's will be Deployed with Sound Effects)";
        DisplayManager.getInstance().popUpInfo(message + ". \n\nClick OK to proceed with test......\n");
        LoggingManager.logInfo(message);
        acceptPause();
        Thread.sleep(1500);
        di.lockDetected(di.getLocation());
        Thread.sleep(1500);
        acceptPause();
        LoggingManager.logInfo("Test 11: Completed");


        message = "Test 12: DecoyingBallisticInbound Self Destruct Test (with Sound Effect)\n" +
                  "Screen Symbol Changed from Yellow 'B' to Yellow 'x'\n" +
                  "Decoys Complete their Flight and shift from Yellow to Red (with Sound Effect on Impact)";
        DisplayManager.getInstance().popUpInfo(message + ". \n\nClick OK to proceed with test......\n");
        LoggingManager.logInfo(message);
        acceptPause();
        Thread.sleep(500);
        di.selfDestruct();
        waitUntilNoInbounds();
        acceptPause();
        LoggingManager.logInfo("Test 12: Completed");

        message = "Test 13: DecoyingBallisticInbound - Full Flight With Detonation Test (Single) - With 2 Sound Effects\n" +
                  "Screen Symbol Shifts from Green to Red";
        DisplayManager.getInstance().popUpInfo(message + ". \n\nClick OK to proceed with test......\n");
        LoggingManager.logInfo(message);
        new DecoyingBallisticInbound(new Point3D(450.0, 400.0, 50000.0), new Point3D(250.0, 150.0, 0.0),
                                     250.0, "Ballistic Inbound", 5, false);
        acceptPause();
        waitUntilNoInbounds();
        acceptPause();
        LoggingManager.logInfo("Test 13: Completed");

        message = "Test 14: BallisticInbound - Full Flight With Detonation Test (Multiple) - With Many Sound Effects\n" +
                  "Screen Symbols Shift from Green to Red";
        DisplayManager.getInstance().popUpInfo(message + ". \n\nClick OK to proceed with test......\n");
        LoggingManager.logInfo(message);
        new DecoyingBallisticInbound(new Point3D(100.0, 550.0, 50000.0), new Point3D(100.0, 50.0, 0.0),
                                     250.0, "Ballistic Inbound 1", 5, false);
        new DecoyingBallisticInbound(new Point3D(200.0, 550.0, 45000.0), new Point3D(200.0, 100.0, 0.0),
                                     250.0, "Ballistic Inbound 2", 5, false);
        new DecoyingBallisticInbound(new Point3D(300.0, 550.0, 40000.0), new Point3D(300.0, 150.0, 0.0),
                                     250.0, "Ballistic Inbound 3", 5, false);
        new DecoyingBallisticInbound(new Point3D(400.0, 550.0, 35000.0), new Point3D(400.0, 200.0, 0.0),
                                     250.0, "Ballistic Inbound 4", 5, false);
        acceptPause();
        waitUntilNoInbounds();
        LoggingManager.logInfo("Test 14: Completed");

    }

    private void runMouseTests() throws InterruptedException
    {
        String message;
        message = "Test 15: Mouse-Click Missile Creation Test\n\n" +
                  "Click (and hold) the LEFT Mouse Button Anywhere in the View Frame,\n" +
                  "Then Drag the Mouse (button still down) to a New Location then Release the Button\n\n" +
                  "A new BallisticInbound will be created using the 'click' as the start-point, and the\n" +
                  "'release' as the destination";
        DisplayManager.getInstance().popUpInfo(message + ". \n\nClick OK to proceed with test......\n");
        LoggingManager.logInfo(message);
        waitforAnyInbounds();
        waitUntilNoInbounds();
        LoggingManager.logInfo("Test 15: Completed");


        message = "Test 16: Mouse-Click Missile Creation Test\n\n" +
                  "Click (and hold) the RIGHT Mouse Button Anywhere in the View Frame,\n" +
                  "Then Drag the Mouse (button still down) to a New Location then Release the Button\n\n" +
                  "A new Decoying BallisticInbound will be created using the 'click' as the start-point,\n" +
                  "and the 'release' as the destination";
        DisplayManager.getInstance().popUpInfo(message + ". \n\nClick OK to proceed with test......\n");
        LoggingManager.logInfo(message);
        waitforAnyInbounds();
        waitUntilNoInbounds();
        LoggingManager.logInfo("Test 16: Completed");
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

    private void waitforAnyInbounds() throws InterruptedException
    {
        int cnt = 0;
        while (InboundManager.getInstance().getNumEntries() == 0)
        {
            if ((cnt++ % 5) == 0)
            {
                LoggingManager.logInfo("Waiting for Test Completion...");
            }
            Thread.sleep(1000);
        }
    }

    public void acceptPause()
    {
        LoggingManager.logInfo("App. Paused!");
        if (paused)
        {
            paused = false;
        }
        else
        {
            paused = true;
        }
    }

    public synchronized void acceptStop()
    {
        LoggingManager.logInfo("App. Stopped!");
        running = false;
        notify();
    }

    public void acceptClose()
    {
        LoggingManager.logInfo("App. Closed!");
        acceptStop();
    }

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
