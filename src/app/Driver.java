package app;

import utils.LoggingManager;
import utils.SoundUtility;
import display.managers.DisplayManager;

/**
 * The Driver begins the program
 * @author Nate
 */
public class Driver
{

    /**
     *  Main method of the entire application
     * @param args
     */
    public static void main(String args[])
    {
        new Thread(SoundUtility.getInstance()).start();

        LoggingManager.setInfo();

        TheApp ta = new TheApp();
        new Thread(ta).start();

        try
        {
	        ta.runFDTests();
	        ta.runMDTests();
            DisplayManager.getInstance().popUpInfo("Tests Complete. \n\nPlease press the 'Stop' Button to Exit\n\n");
            LoggingManager.logInfo("Tests Complete.");
        }
        catch (Exception e) // Should catch your exceptions
        {
            e.printStackTrace();
        }
	    System.exit(0);
    }


}

