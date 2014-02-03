package utils;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.HTMLLayout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.WriterAppender;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class LoggingManager
{

	private static Logger logger = null;
	private static boolean initialized = false;

	private static void initialize()
	{
		if (initialized) return;
		try
		{
			logger = Logger.getLogger("MasterLogger");
			HTMLLayout layout = new HTMLLayout();
			layout.setTitle("Application Log File");
			FileOutputStream output = new FileOutputStream("AppLogFile.html");


			// Create the first Appender - for HTML file output
			WriterAppender htmlFileAppender = new WriterAppender(layout, output);
			String pattern = "%d{ISO8601}  %-5p %m %n";
			PatternLayout layout2 = new PatternLayout(pattern);

			// Create the second Appender - for Console file output
			ConsoleAppender consoleAppender = new ConsoleAppender(layout2);


			logger.addAppender(consoleAppender);
			logger.addAppender(htmlFileAppender);

		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
		initialized = true;
	}

	public static void setDebug()
	{
		logger.setLevel(Level.DEBUG);
	}

	public static void setInfo()
	{
		logger.setLevel(Level.INFO);
	}

	public static void setWarn()
	{
		logger.setLevel(Level.WARN);
	}

	public static void setError()
	{
		logger.setLevel(Level.ERROR);
	}

	public static void setFatal()
	{
		logger.setLevel(Level.FATAL);
	}


	public static void logDebug(String msg)
	{
		initialize();
		logger.debug(msg);
	}

	public static void logInfo(String msg)
	{
		initialize();
		logger.info(msg);
	}

	public static void logWarn(String msg)
	{
		initialize();
		logger.warn(msg);
	}

	public static void logError(String msg)
	{
		initialize();
		logger.error(msg);
	}

	public static void logFatal(String msg)
	{
		initialize();
		logger.fatal(msg);
	}

}