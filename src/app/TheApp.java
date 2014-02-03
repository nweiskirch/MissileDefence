package app;

import display.interfaces.Displayable;
import display.interfaces.ViewFrameListener;
import display.managers.DisplayManager;
import utils.LoggingManager;
import utils.Point3D;
import utils.PropertyManager;

import java.util.ArrayList;
import java.util.List;


public class TheApp implements Runnable, ViewFrameListener
{

	private boolean paused = false;
	private boolean running = true;

	public TheApp()
	{
		DisplayManager.getInstance().setViewFrameListener(this);
	}

	// The run method simply invokes the DisplayManager's "update" method in a loop
	// while the application is "running". Clicking "Pause" or "Stop" on the GUI, or
	// otherwise setting the "running" attribute to false will stop loop execution.
	public void run()
	{
		while (running)
		{
			synchronized (this)
			{
				try
				{
					Thread.sleep(10);
					DisplayManager.getInstance().updateDisplay();
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}

		LoggingManager.logInfo("Exiting...");
		System.exit(0);
	}


	public void runBasicTests()
	{
		// Each test below is self-documenting
		// (refer to the "message" Strind for the details of each test).
		try
		{
			// Test Code *************************************************************************

			String message;

			DisplayableTest dt = new DisplayableTest();
			dt.symbol = "X";
			DisplayManager.getInstance().addContent(dt);


			// Test Code *************************************************************************
			while (paused)
			{
			}
			message = "Test 1: Show Displayable ('X') in Upper Left";
			DisplayManager.getInstance().popUpInfo(message + ". Click OK to proceed with test...");

			LoggingManager.logInfo(message);
			dt.location.x = 5;
			dt.location.y = 10.0;
			dt.location.z = PropertyManager.Instance().getDoubleProperty("ZMAX");


			// Test Code *************************************************************************
			while (paused)
			{
			}
			message = "Test 2: Show Displayable ('X') in Upper Right";
			DisplayManager.getInstance().popUpInfo(message + ". Click OK to proceed with test...");

			LoggingManager.logInfo(message);
			dt.location.x = PropertyManager.Instance().getDoubleProperty("PIXELSX") - 8;
			dt.location.y = 10.0;
			dt.location.z = PropertyManager.Instance().getDoubleProperty("ZMAX");


			// Test Code *************************************************************************
			while (paused)
			{
			}
			message = "Test 3: Show Displayable ('X') in Lower Left";
			DisplayManager.getInstance().popUpInfo(message + ". Click OK to proceed with test...");

			LoggingManager.logInfo(message);
			dt.location.x = 0.0;
			dt.location.y = PropertyManager.Instance().getDoubleProperty("PIXELSY");
			dt.location.z = PropertyManager.Instance().getDoubleProperty("ZMAX");


			// Test Code *************************************************************************
			while (paused)
			{
			}
			message = "Test 4: Show Displayable ('X') in Lower Right";
			DisplayManager.getInstance().popUpInfo(message + ". Click OK to proceed with test...");

			LoggingManager.logInfo(message);
			dt.location.x = PropertyManager.Instance().getDoubleProperty("PIXELSX") - 8;
			dt.location.y = PropertyManager.Instance().getDoubleProperty("PIXELSY");
			dt.location.z = PropertyManager.Instance().getDoubleProperty("ZMAX");


			// Test Code *************************************************************************
			while (paused)
			{
			}
			message = "Test 5: Show Displayable ('X') in Center";
			DisplayManager.getInstance().popUpInfo(message + ". Click OK to proceed with test...");

			LoggingManager.logInfo(message);
			dt.location.x = PropertyManager.Instance().getDoubleProperty("PIXELSX") / 2;
			dt.location.y = PropertyManager.Instance().getDoubleProperty("PIXELSY") / 2;
			dt.location.z = PropertyManager.Instance().getDoubleProperty("ZMAX");


			// Test Code *************************************************************************
			while (paused)
			{
			}
			message = "Test 6: Show Displayable ('X') in Random Locations";
			DisplayManager.getInstance().popUpInfo(message + ". Click OK to proceed with test...");

			LoggingManager.logInfo(message);
			for (int i = 0; i < 100; i++)
			{
				if (!paused)
				{
					dt.location.x = Math.random() *
					                PropertyManager.Instance().
					        getDoubleProperty("PIXELSX");
					dt.location.y = Math.random() *
					                PropertyManager.Instance().
					        getDoubleProperty("PIXELSY");
					Thread.sleep(100);
				}
				else
					i--;
			}


			// Test Code *************************************************************************
			while (paused)
			{
			}
			message = "Test 7: Show (2) Displayable Movement";
			DisplayManager.getInstance().popUpInfo(message + ". Click OK to proceed with test...");

			LoggingManager.logInfo(message);
			DisplayableTest dt2 = new DisplayableTest();
			dt2.symbol = "O";
			DisplayManager.getInstance().addContent(dt2);

			for (int i = 0; i < PropertyManager.Instance().getDoubleProperty("PIXELSX"); i++)
			{
				if (!paused)
				{
					dt.location.x = i;
					dt.location.y = i;
					dt.location.z = PropertyManager.Instance().
					        getDoubleProperty("ZMAX");

					dt2.location.x = PropertyManager.Instance().
					        getDoubleProperty("PIXELSX") - i;
					dt2.location.y = i;
					dt.location.z = PropertyManager.Instance().
					        getDoubleProperty("ZMAX");

					Thread.sleep(5);
				}
				else
					i--;
			}
			DisplayManager.getInstance().removeContent(dt, 0);
			DisplayManager.getInstance().removeContent(dt2, 0);


			// Test Code *************************************************************************
			while (paused)
			{
			}
			int cs = DisplayManager.getInstance().contentSize();

			message = "Test 8: Show Displayables in Random Fill/Remove";
			DisplayManager.getInstance().popUpInfo(message + ". Click OK to proceed with test...");

			LoggingManager.logInfo(message);
			List a = new ArrayList();
			for (int i = 0; i < 100; i++)
			{
				if (!paused)
				{
					Displayable d = new DisplayableTest(new Point3D(Math.random() *
					                                                PropertyManager.Instance().getDoubleProperty("PIXELSX"),
					                                                Math.random() *
					                                                PropertyManager.Instance().getDoubleProperty("PIXELSY"),
					                                                Math.random() * PropertyManager.Instance().getDoubleProperty("ZMAX")), "" + i);
					a.add(d);
					DisplayManager.getInstance().addContent(d);
					Thread.sleep(50);
				}
				else
					i--;
			}
			for (int i = 0; i < a.size(); i++)
				DisplayManager.getInstance().
				        removeContent((Displayable) a.get(i), PropertyManager.Instance().getIntProperty("REMOVALDELAY"));

			while (DisplayManager.getInstance().contentSize() > cs)
			{
				Thread.sleep(10);
			}

			// Test Code *************************************************************************
			while (paused)
			{
			}
			LoggingManager.logInfo("\n\nThe Test is Done. Please press the 'Stop' Button to Exit.\n");
			message = "The Test is Done. Please press the 'Stop' Button to Exit.";
			DisplayManager.getInstance().popUpInfo(message + ". Click OK to proceed with test...");
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}


	public void acceptPause() // Invoked when "Pause" is pressed
	{
		LoggingManager.logInfo("Pause Requested!");
		if (paused)
		{
			paused = false;
		}
		else
		{
			paused = true;
			DisplayManager.getInstance().popUpInfo("You just Paused the test!\nPress 'Continue' to resume");
		}
	}

	public synchronized void acceptStop() // Invoked when "Stop" is pressed
	{
		LoggingManager.logInfo("Stop Requested!");
		running = false;
		notify();
	}

	public void acceptClose() // Invoked when the GUI window is closed
	{
		LoggingManager.logInfo("Close Requested!");
		acceptStop();
	}

	// Invoked when the left mouse button is pressed and released
	public void acceptLeftButtonMouseCommand(int pressX, int pressY, int releaseX, int releaseY)
	{
		LoggingManager.logInfo("Left Mouse [" + pressX + ", " + pressY + " " + releaseX + ", " + releaseY + "]");
		DisplayManager.getInstance().popUpInfo("You just pressed & released the LEFT mouse button\n" +
		                                       "Press: [" + pressX + ", " + pressY + "], Release: [" + releaseX + ", " + releaseY + "]");
	}

	// Invoked when the right mouse button is pressed and released
	public void acceptRightButtonMouseCommand(int pressX, int pressY, int releaseX, int releaseY)
	{
		LoggingManager.logInfo("Right Mouse [" + pressX + ", " + pressY + "]");
		DisplayManager.getInstance().popUpInfo("You just pressed & released the RIGHT mouse button\n" +
		                                       "Press: [" + pressX + ", " + pressY + "], Release: [" + releaseX + ", " + releaseY + "]");
	}


	///////////////////////////////////////////////////////////////////////////////////////////
	// This static inner class is temporary only. It is needed to represent
	// an entity that we wish to display on the GUI screen. In subsequent phases,
	// we will use our actual application entities (that implement the Displayable
	// interface) on the GUI screen.
	///////////////////////////////////////////////////////////////////////////////////////////

	public static class DisplayableTest implements Displayable
	{
		private Point3D location;
		private String symbol;

		public DisplayableTest(Point3D loc, String sym)
		{
			location = loc;
			symbol = sym;
		}

		public DisplayableTest()
		{
			location = new Point3D();
			symbol = new String();
		}

		public Point3D getLocation()
		{
			return location;
		}

		public String getSymbol()
		{
			return symbol;
		}
	}

}
