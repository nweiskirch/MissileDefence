package app;

import app.domain.fixed.FixedDetector;
import app.domain.fixed.FixedLauncher;
import app.domain.missiles.BallisticInbound;
import app.domain.missiles.DecoyingBallisticInbound;
import app.domain.mobile.MobileDetector;
import app.domain.mobile.MobileLauncher;
import app.managers.DetectorManager;
import app.managers.InboundManager;
import app.managers.InterceptorManager;
import app.utils.RandomGenerator;
import display.interfaces.ViewFrameListener;
import display.managers.DisplayManager;
import java.util.ArrayList;
import utils.LoggingManager;
import utils.Point3D;

public class TheApp implements Runnable, ViewFrameListener {

    private boolean paused = false;
    private boolean running = true;

    public TheApp() {
        DisplayManager.getInstance().setViewFrameListener(this);
    }

    public void run() {
        while (running) {
            synchronized (this) {
                try {
                    Thread.sleep(20);
                    if (!paused) {
                        InterceptorManager.getInstance().update(1000);
                        InboundManager.getInstance().update(1000);
                        DetectorManager.getInstance().update(1000);
                    }
                    DisplayManager.getInstance().updateDisplay();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (Exception e) // This should change to catch your own exception classes
                {
                    e.printStackTrace();
                }
            }
        }
        LoggingManager.logInfo("Exiting the app");
        System.exit(0);
    }

    public void runFLTests() throws InterruptedException {
        String message;
        acceptPause();

        ArrayList associatedLaunchers = new ArrayList();
        FixedDetector fd = null;

        message = "Test 1";
        DisplayManager.getInstance().popUpInfo(message + ". \n\nClick OK to proceed with test......\n");
        LoggingManager.logInfo(message);
        associatedLaunchers.clear();
        FixedLauncher fl = new FixedLauncher(new Point3D(180.0, 350.0, 0.0), "FixedLauncher 2", 3, 0);
        associatedLaunchers.add(fl);
        MobileLauncher ml = new MobileLauncher(new Point3D(120.0, 150.0, 0.0), new Point3D(30.0, 150.0, 0.0), 0.5, "MobileLauncher", 3, 0);
        associatedLaunchers.add(ml);
        fd = new FixedDetector(new Point3D(280.0, 250.0, 0.0), "Fixed Detector 2", 20000.0, associatedLaunchers);
        acceptPause();
        Thread.sleep(3000);
        acceptPause();
        LoggingManager.logInfo("Test 1: Completed");

        message = "Test 2";
        DisplayManager.getInstance().popUpInfo(message + ". \n\nClick OK to proceed with test......\n");
        LoggingManager.logInfo(message);
        new BallisticInbound(new Point3D(50.0, 150.0, 30000.0), new Point3D(280.0, 250.0, 0.0), 150.0, "Ballistic Inbound", false);
        acceptPause();
        waitUntilNoAction();
        Thread.sleep(3000);
        acceptPause();
        LoggingManager.logInfo("Test 2: Completed");

        message = "Test 3";
        DisplayManager.getInstance().popUpInfo(message + ". \n\nClick OK to proceed with test......\n");
        LoggingManager.logInfo(message);
        acceptPause();
        fd.destroy();
        fl.destroy();
        ml.destroy();
        Thread.sleep(5000);
        acceptPause();
        LoggingManager.logInfo("Test 3: Completed");

        message = "Test 4";
        DisplayManager.getInstance().popUpInfo(message + ". \n\nClick OK to proceed with test......\n");
        LoggingManager.logInfo(message);
        associatedLaunchers.clear();
        fl = new FixedLauncher(new Point3D(330.0, 50.0, 0.0), "FixedLauncher", 0, 3);
        associatedLaunchers.add(fl);
        ml = new MobileLauncher(new Point3D(160.0, 450.0, 0.0), new Point3D(30.0, 150.0, 0.0), 0.5, "MobileLauncher", 0, 3);
        associatedLaunchers.add(ml);
        fd = new FixedDetector(new Point3D(380.0, 350.0, 0.0), "Fixed Detector 2", 20000.0, associatedLaunchers);
        acceptPause();
        Thread.sleep(3000);
        acceptPause();
        LoggingManager.logInfo("Test 4: Completed");

        message = "Test 5";
        DisplayManager.getInstance().popUpInfo(message + ". \n\nClick OK to proceed with test......\n");
        LoggingManager.logInfo(message);
        new BallisticInbound(new Point3D(50.0, 150.0, 30000.0), new Point3D(380.0, 350.0, 0.0), 150.0, "Ballistic Inbound", false);
        acceptPause();
        waitUntilNoAction();
        Thread.sleep(3000);
        acceptPause();
        LoggingManager.logInfo("Test 5: Completed");

        message = "Test 6";
        DisplayManager.getInstance().popUpInfo(message + ". \n\nClick OK to proceed with test......\n");
        LoggingManager.logInfo(message);
        acceptPause();
        fd.destroy();
        fl.destroy();
        ml.destroy();
        Thread.sleep(3000);
        acceptPause();
        LoggingManager.logInfo("Test 6: Completed");

        message = "Test 7";
        DisplayManager.getInstance().popUpInfo(message + ". \n\nClick OK to proceed with test......\n");
        LoggingManager.logInfo(message);
        associatedLaunchers.clear();
        fl = new FixedLauncher(new Point3D(130.0, 250.0, 0.0), "FixedLauncher", 6, 16);
        associatedLaunchers.add(fl);
        ml = new MobileLauncher(new Point3D(300.0, 350.0, 0.0), new Point3D(30.0, 150.0, 0.0), 0.5, "MobileLauncher", 18, 4);
        associatedLaunchers.add(ml);
        new FixedDetector(new Point3D(180.0, 450.0, 0.0), "Fixed Detector 2", 20000.0, associatedLaunchers);
        acceptPause();
        Thread.sleep(3000);
        acceptPause();
        LoggingManager.logInfo("Test 7: Completed");

        message = "Test 8";
        DisplayManager.getInstance().popUpInfo(message + ". \n\nClick OK to proceed with test......\n");
        LoggingManager.logInfo(message);
        new DecoyingBallisticInbound(new Point3D(50.0, 150.0, 30000.0), new Point3D(180.0, 450.0, 0.0), 150.0, "Ballistic Inbound", 6, false);
        acceptPause();
        waitUntilNoAction();
        Thread.sleep(3000);
        acceptPause();
        LoggingManager.logInfo("Test 8: Completed");


        message = "Test 9";
        DisplayManager.getInstance().popUpInfo(message + ". \n\nClick OK to proceed with test......\n");
        LoggingManager.logInfo(message);
        associatedLaunchers = new ArrayList();
        associatedLaunchers.add(new FixedLauncher(new Point3D(500.0, 150.0, 0.0), "FixedLauncher", 6, 16));
        associatedLaunchers.add(new MobileLauncher(new Point3D(400.0, 550.0, 0.0), new Point3D(100.0, 50.0, 0.0), 0.5, "MobileLauncher", 18, 4));
        associatedLaunchers.add(new FixedLauncher(new Point3D(330, 150.0, 0.0), "FixedLauncher", 6, 16));
        associatedLaunchers.add(new MobileLauncher(new Point3D(125, 550.0, 0.0), new Point3D(410.0, 250.0, 0.0), 0.5, "MobileLauncher", 18, 4));
        new MobileDetector(new Point3D(480.0, 250.0, 0.0), new Point3D(380.0, 250.0, 0.0), 0.5, "Mobile Detector", 20000.0, associatedLaunchers);
        new DecoyingBallisticInbound(new Point3D(350.0, 50.0, 30000.0), new Point3D(180.0, 450.0, 0.0), 150.0, "Ballistic Inbound", 5, false);
        acceptPause();
        waitUntilNoAction();
        Thread.sleep(3000);
        acceptPause();
        LoggingManager.logInfo("Test 9: Completed");


        message = "Test 10";
        DisplayManager.getInstance().popUpInfo(message + ". \n\nClick OK to proceed with test......\n");
        LoggingManager.logInfo(message);
        double x = 150.0;
        double y = 150.0;
        for (int i = 0; i < 3; i++) {
            new DecoyingBallisticInbound(new Point3D(x, y, 30000.0), new Point3D(180.0, 450.0, 0.0), 150.0, "Ballistic Inbound", 7, false);
            x += 150;
            y += 150;
        }
        acceptPause();
        waitUntilNoAction();
        Thread.sleep(3000);
        acceptPause();
        LoggingManager.logInfo("Test 4: Completed");


        message = "Test 11";
        DisplayManager.getInstance().popUpInfo(message + ". \n\nClick OK to proceed with test......\n");
        LoggingManager.logInfo(message);
        x = 50.0;
        y = 50.0;
        for (int i = 0; i < 10; i++) {
            new BallisticInbound(new Point3D(x, y, 30000.0), new Point3D(180.0, 450.0, 0.0), 150.0, "Ballistic Inbound", false);
            x += 50;
            y += 50;
        }
        acceptPause();
        waitUntilNoAction();
        Thread.sleep(3000);
        acceptPause();
        LoggingManager.logInfo("Test 11: Completed");


        message = "Test 12";
        DisplayManager.getInstance().popUpInfo(message + ". \n\nClick OK to proceed with test......\n");
        LoggingManager.logInfo(message);
        x = 50.0;
        y = 50.0;
        for (int i = 0; i < 6; i++) {
            new BallisticInbound(new Point3D(x, y, 30000.0), new Point3D(180.0, 450.0, 0.0), 150.0, "Ballistic Inbound", false);
            x += 100;
        }
        x = 50.0;
        y = 50.0;
        for (int i = 0; i < 6; i++) {
            new BallisticInbound(new Point3D(x, y, 30000.0), new Point3D(180.0, 450.0, 0.0), 150.0, "Ballistic Inbound", false);
            y += 100;
        }
        x = 550.0;
        y = 550.0;
        for (int i = 0; i < 6; i++) {
            new BallisticInbound(new Point3D(x, y, 30000.0), new Point3D(180.0, 450.0, 0.0), 150.0, "Ballistic Inbound", false);
            x -= 100;
        }
        x = 550.0;
        y = 550.0;
        for (int i = 0; i < 6; i++) {
            new BallisticInbound(new Point3D(x, y, 30000.0), new Point3D(180.0, 450.0, 0.0), 150.0, "Ballistic Inbound", false);
            y -= 100;
        }
        acceptPause();
        waitUntilNoAction();
        Thread.sleep(2000);
        acceptPause();
        LoggingManager.logInfo("Test 12: Completed");

        message = "Test 13";
        DisplayManager.getInstance().popUpInfo(message + ". \n\nClick OK to proceed with test......\n");
        LoggingManager.logInfo(message);
        acceptPause();
        for (int i = 0; i < 15; i++) {
            new DecoyingBallisticInbound(new Point3D(600.0 * RandomGenerator.Instance().getRandomNumber(),
                    600.0 * RandomGenerator.Instance().getRandomNumber(),
                    30000.0),
                    new Point3D(180.0, 450.0, 0.0), 150.0, "Ballistic Inbound", 7, false);
            Thread.sleep(200);
        }
        waitUntilNoAction();
        Thread.sleep(2000);
        acceptPause();
        LoggingManager.logInfo("Test 13: Completed");

    }

    private void waitUntilNoAction() throws InterruptedException {
        int cnt = 0;
        while ((InboundManager.getInstance().getNumEntries() > 0)
                || (InterceptorManager.getInstance().getNumEntries() > 0)) {
            if ((cnt++ % 5) == 0) {
                LoggingManager.logInfo("Waiting for Test Completion...");
            }
            Thread.sleep(1000);
        }
    }

    public void acceptPause() {

        if (paused) {
            LoggingManager.logInfo("App. Resumed!");
            paused = false;
        } else {
            LoggingManager.logInfo("App. Paused!");
            paused = true;
        }
    }

    public synchronized void acceptStop() {
        LoggingManager.logInfo("App. Stopped!");
        running = false;
        notify();
    }

    public void acceptClose() {
        LoggingManager.logInfo("App. Closed!");
        acceptStop();
    }

    public void acceptLeftButtonMouseCommand(int pressX, int pressY, int releaseX, int releaseY) {
        LoggingManager.logInfo("Left Mouse Button [" + pressX + ", " + pressY + " " + releaseX + ", " + releaseY + "]");
        try {
            int id = (int) (RandomGenerator.Instance().getRandomNumber() * 10000.0);
            new BallisticInbound(new Point3D(pressX, pressY, 50000), new Point3D(releaseX, releaseY, 0),
                    200.0, "Ballistic Inbound " + id, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void acceptRightButtonMouseCommand(int pressX, int pressY, int releaseX, int releaseY) {
        LoggingManager.logInfo("Left Mouse Button [" + pressX + ", " + pressY + " " + releaseX + ", " + releaseY + "]");
        try {
            int id = (int) (RandomGenerator.Instance().getRandomNumber() * 10000.0);
            new DecoyingBallisticInbound(new Point3D(pressX, pressY, 50000),
                    new Point3D(releaseX, releaseY, 0), 150.0,
                    "Decoying Ballistic Inbound " + id, 5, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
