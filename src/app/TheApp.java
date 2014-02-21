package app;

import app.domain.missiles.BallisticInbound;
import app.domain.missiles.DecoyingBallisticInbound;
import app.domain.fixed.FixedDetector;
import app.domain.fixed.FixedLauncher;
import app.domain.mobile.MobileDetector;
import app.domain.mobile.MobileLauncher;
import app.managers.DetectorManager;
import app.managers.InboundManager;
import app.utils.RandomGenerator;
import display.interfaces.ViewFrameListener;
import display.managers.DisplayManager;
import utils.LoggingManager;
import utils.Point3D;

import java.util.ArrayList;

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


	public void runFLTests() throws InterruptedException
	{
		String message;
		acceptPause();

		ArrayList associatedLaunchers = new ArrayList();
		FixedLauncher fl1, fl2, fl3;
		MobileLauncher ml1, ml2, ml3;

		message = "Test 1: Creating a Fixed and Mobile Launcher (and associated FixedDetector) - Screen Symbol is a Red 'fl', 'ml' & 'FD'";
		DisplayManager.getInstance().popUpInfo(message + ". \n\nClick OK to proceed with test......\n");
		LoggingManager.logInfo(message);

		fl1 = new FixedLauncher(new Point3D(430.0, 50.0, 0.0), "FixedLauncher 1", 5, 5);
		associatedLaunchers.add(fl1);
		ml1 = new MobileLauncher(new Point3D(300.0, 350.0, 0.0), new Point3D(430.0, 50.0, 0.0), 2, "MobileLauncher 1", 5, 5);
		associatedLaunchers.add(ml1);

		FixedDetector fd =
		        new FixedDetector(new Point3D(300.0, 150.0, 0.0), "Fixed Detector 1", 20000.0, associatedLaunchers);
		acceptPause();
		Thread.sleep(2000);
		acceptPause();
		LoggingManager.logInfo("Test 1: Completed");


		message = "Test 2: Launcher Control Test - FixedDetector is destroyed, Launchers stop updating";
		DisplayManager.getInstance().popUpInfo(message + ". \n\nClick OK to proceed with test......\n");
		LoggingManager.logInfo(message);
		acceptPause();
		Thread.sleep(1000);
		fd.destroy();
		Thread.sleep(2000);
		acceptPause();
		LoggingManager.logInfo("Test 2: Completed");


		message = "Test 3: Launcher Destruction Test - Destroy the FixedLauncher & MobileLauncher ";
		DisplayManager.getInstance().popUpInfo(message + ". \n\nClick OK to proceed with test......\n");
		LoggingManager.logInfo(message);
		acceptPause();
		Thread.sleep(1000);
		fl1.destroy();
		Thread.sleep(2000);
		ml1.destroy();
		Thread.sleep(2000);
		acceptPause();
		LoggingManager.logInfo("Test 3: Completed");

		message = "Test 4: Creating a Fixed and Mobile Launcher (and associated MobileDetector)";
		DisplayManager.getInstance().popUpInfo(message + ". \n\nClick OK to proceed with test......\n");
		LoggingManager.logInfo(message);

		associatedLaunchers.clear();
		fl1 = new FixedLauncher(new Point3D(130.0, 250.0, 0.0), "FixedLauncher 2", 5, 5);
		associatedLaunchers.add(fl1);
		ml1 = new MobileLauncher(new Point3D(300.0, 350.0, 0.0), new Point3D(30.0, 150.0, 0.0), 2, "MobileLauncher 2", 5, 5);
		associatedLaunchers.add(ml1);

		MobileDetector md =
		        new MobileDetector(new Point3D(300.0, 150.0, 0.0), new Point3D(150.0, 350.0, 0.0),
		                   3.0, "Mobile Detector 1", 20000.0, associatedLaunchers);
		acceptPause();
		Thread.sleep(2000);
		acceptPause();
		LoggingManager.logInfo("Test 4: Completed");

		message = "Test 5: Mobile Detector & Launcher Move and Change Direction - Fixed Launcher Does Not Move";
		DisplayManager.getInstance().popUpInfo(message + ". \n\nClick OK to proceed with test......\n");
		LoggingManager.logInfo(message);
		acceptPause();
		Thread.sleep(4000);
		acceptPause();
		LoggingManager.logInfo("Test 5: Completed");


		message = "Test 6: BallisticInbound Appears Within Detector Range - MobileDetector Signals FixedLauncher to Launch - BallisticInbound Detonates";
		DisplayManager.getInstance().popUpInfo(message + ". \n\nClick OK to proceed with test......\n");
		LoggingManager.logInfo(message);
		new BallisticInbound(new Point3D(50.0, 250.0, 8000.0),new Point3D(100.0, 350.0, 0.0), 150,
		                     "Ballistic Inbound 1", false);
		acceptPause();
		waitUntilNoInbounds();
		acceptPause();
		LoggingManager.logInfo("Test 6: Completed");

		message = "Test 7: BallisticInbound Appears Outside Detector Range - No Detection or Launch Signal Occurs";
		DisplayManager.getInstance().popUpInfo(message + ". \n\nClick OK to proceed with test......\n");
		LoggingManager.logInfo(message);
		new BallisticInbound(new Point3D(550.0, 30.0, 25000.0),new Point3D(130.0, 250.0, 0.0), 150,
		                     "Ballistic Inbound 2", false);
		acceptPause();
		Thread.sleep(2000);
		acceptPause();
		LoggingManager.logInfo("Test 7: Completed");

		message = "Test 8: BallisticInbound Continues Towards Destination - Detection is Made and Launch Signal Occurs";
		DisplayManager.getInstance().popUpInfo(message + ". \n\nClick OK to proceed with test......\n");
		LoggingManager.logInfo(message);
		acceptPause();
		Thread.sleep(3000);
		acceptPause();
		LoggingManager.logInfo("Test 8: Completed");

		message = "Test 9: BallisticInbound Continues Towards Destination - Destroys FixedLauncher";
		DisplayManager.getInstance().popUpInfo(message + ". \n\nClick OK to proceed with test......\n");
		LoggingManager.logInfo(message);
		acceptPause();
		waitUntilNoInbounds();
		acceptPause();
		LoggingManager.logInfo("Test 9: Completed");

		message = "Test 10: Destroy the Mobile Launcher";
		DisplayManager.getInstance().popUpInfo(message + ". \n\nClick OK to proceed with test......\n");
		LoggingManager.logInfo(message);
		acceptPause();
		Thread.sleep(1000);
		ml1.destroy();
		Thread.sleep(3000);
		acceptPause();
		LoggingManager.logInfo("Test 10: Completed");

		message = "Test 11: Destroy the Mobile Detector";
		DisplayManager.getInstance().popUpInfo(message + ". \n\nClick OK to proceed with test......\n");
		LoggingManager.logInfo(message);
		acceptPause();
		Thread.sleep(1000);
		md.destroy();
		Thread.sleep(3000);
		acceptPause();
		LoggingManager.logInfo("Test 11: Completed");


		message = "Test 12: Creating 3 Fixed and 3 Mobile Launchers (and associated FixedDetector)";
		DisplayManager.getInstance().popUpInfo(message + ". \n\nClick OK to proceed with test......\n");
		LoggingManager.logInfo(message);

		associatedLaunchers.clear();
		fl1 = new FixedLauncher(new Point3D(130.0, 250.0, 0.0), "FixedLauncher 2", 5, 5);
		associatedLaunchers.add(fl1);
		ml1 = new MobileLauncher(new Point3D(300.0, 350.0, 0.0), new Point3D(480.0, 150.0, 0.0), 2, "MobileLauncher 2", 5, 5);
		associatedLaunchers.add(ml1);
		fl2 = new FixedLauncher(new Point3D(480.0, 150.0, 0.0), "FixedLauncher 2", 5, 5);
		associatedLaunchers.add(fl2);
		ml2 = new MobileLauncher(new Point3D(220.0, 450.0, 0.0), new Point3D(30.0, 300.0, 0.0), 2, "MobileLauncher 2", 5, 5);
		associatedLaunchers.add(ml2);
		fl3 = new FixedLauncher(new Point3D(30.0, 300.0, 0.0), "FixedLauncher 2", 5, 5);
		associatedLaunchers.add(fl3);
		ml3 = new MobileLauncher(new Point3D(520.0, 520.0, 0.0), new Point3D(130.0, 250.0, 0.0), 2, "MobileLauncher 2", 5, 5);
		associatedLaunchers.add(ml3);

		fd = new FixedDetector(new Point3D(180.0, 450.0, 0.0), "Fixed Detector 2", 20000.0, associatedLaunchers);

		acceptPause();
		Thread.sleep(4000);
		acceptPause();
		LoggingManager.logInfo("Test 12: Completed");

		message = "Test 13: BallisticInbound Appears Outside Detector Range - No Detection or Launch Signal Occurs";
		DisplayManager.getInstance().popUpInfo(message + ". \n\nClick OK to proceed with test......\n");
		LoggingManager.logInfo(message);
		new BallisticInbound(new Point3D(550.0, 30.0, 25000.0),new Point3D(180.0, 450.0, 0.0), 150,
		                     "Ballistic Inbound 3", false);
		acceptPause();
		Thread.sleep(2000);
		acceptPause();
		LoggingManager.logInfo("Test 13: Completed");

		message = "Test 14: BallisticInbound Continues Towards Destination - Detection is Made and Launch Signal Occurs";
		DisplayManager.getInstance().popUpInfo(message + ". \n\nClick OK to proceed with test......\n");
		LoggingManager.logInfo(message);
		acceptPause();
		Thread.sleep(3000);
		acceptPause();
		LoggingManager.logInfo("Test 14: Completed");

		message = "Test 15: BallisticInbound Continues Towards Destination - Destroys FixedDetector - Launchers Stop Updating";
		DisplayManager.getInstance().popUpInfo(message + ". \n\nClick OK to proceed with test......\n");
		LoggingManager.logInfo(message);
		acceptPause();
		waitUntilNoInbounds();
		acceptPause();
		LoggingManager.logInfo("Test 15: Completed");

		message = "Test 16: Launch BallisticInbounds at the Launchers - Launchers Destroyed";
		DisplayManager.getInstance().popUpInfo(message + ". \n\nClick OK to proceed with test......\n");
		LoggingManager.logInfo(message);

		new BallisticInbound(new Point3D(0.0, 0.0, 18000.0),fl1.getLocation(), 150, "Ballistic Inbound 4", false);
		new BallisticInbound(new Point3D(600.0, 0.0, 18000.0),ml1.getLocation(), 150, "Ballistic Inbound 5", false);
		new BallisticInbound(new Point3D(0.0, 600.0, 18000.0),fl2.getLocation(), 150, "Ballistic Inbound 6", false);
		new BallisticInbound(new Point3D(600.0, 600.0, 18000.0),ml2.getLocation(), 150, "Ballistic Inbound 7", false);
		new BallisticInbound(new Point3D(300.0, 0.0, 18000.0),fl3.getLocation(), 150, "Ballistic Inbound 8", false);
		new BallisticInbound(new Point3D(0.0, 300.0, 18000.0),ml3.getLocation(), 150, "Ballistic Inbound 9", false);

		acceptPause();
		waitUntilNoInbounds();
		Thread.sleep(6000);
		acceptPause();
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
