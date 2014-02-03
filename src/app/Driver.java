package app;

import utils.LoggingManager;
import utils.SoundUtility;

public class Driver
{
    public static void main(String[] args)
    {
        new Thread(SoundUtility.getInstance()).start();

        LoggingManager.setInfo();

        TheApp ta = new TheApp();
        new Thread(ta).start();

        try
        {
            ta.runIntegratedTests();
//			ta.runIntegratedTests();
        }
        catch (Exception e) // Should catch your exceptions
        {
            e.printStackTrace();
        }
    }


}

