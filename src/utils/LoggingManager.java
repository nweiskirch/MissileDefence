package utils;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.HTMLLayout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.WriterAppender;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 *
 * @author Nate
 */
public class LoggingManager
{

    private static Logger logger = null;
    private static boolean initialized = false;

    private static void initialize()
    {
        if (initialized) return;
        try
        {
            logger = Logger.getLogger("SE450");
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

    /**
     *
     */
    public static void setDebug()
    {
        initialize();
        logger.setLevel(Level.DEBUG);
    }

    /**
     *
     */
    public static void setInfo()
    {
        initialize();
        logger.setLevel(Level.INFO);
    }

    /**
     *
     */
    public static void setWarn()
    {
        initialize();
        logger.setLevel(Level.WARN);
    }

    /**
     *
     */
    public static void setError()
    {
        initialize();
        logger.setLevel(Level.ERROR);
    }

    /**
     *
     */
    public static void setFatal()
    {
        initialize();
        logger.setLevel(Level.FATAL);
    }

    /**
     *
     * @param msg
     */
    public static void logDebug(String msg)
    {
        initialize();
        logger.debug(msg);
    }

    /**
     *
     * @param msg
     */
    public static void logInfo(String msg)
    {
        initialize();
        logger.info(msg);
    }

    /**
     *
     * @param msg
     */
    public static void logWarn(String msg)
    {
        initialize();
        logger.warn(msg);
    }

    /**
     *
     * @param msg
     */
    public static void logError(String msg)
    {
        initialize();
        logger.error(msg);
    }

    /**
     *
     * @param msg
     */
    public static void logFatal(String msg)
    {
        initialize();
        logger.fatal(msg);
    }

}