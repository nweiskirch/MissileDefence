package app;

import utils.SoundUtility;


public class Driver
{

	// This is the application's "driver" - it contains the "main" method.

	// This "main" method starts the SoundUtility thread and calls the
	// SoundUtility test method "runBasicTests".

	// Afterwards, the "application" is created, then ir is started running
	// in it's own thread. Finally, the TheApp test method "runBasicTests"
	// is called.

	public static void main(String args[])
	{
		new Thread(SoundUtility.getInstance()).start();
		SoundUtility.getInstance().runBasicTests();

		TheApp ta = new TheApp();
		new Thread(ta).start();
		ta.runBasicTests();

	}


}

