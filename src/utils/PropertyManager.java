package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 *
 * @author Nate
 */
public class PropertyManager extends Properties
{

    private static PropertyManager instance;


    private PropertyManager()
    {
        loadProperties("Properties.txt");
    }

    /**
     *
     * @return
     */
    public static PropertyManager Instance()
    {
        if (instance == null)
        {
            instance = new PropertyManager();
        }
        return instance;
    }

    /**
     *
     * @param key
     * @return
     */
    public int getIntProperty(String key)
    {
        if (propertyIsNull(key))
        {
            System.err.println("**** Property: " + key + " is not loaded and has no value.");
            System.exit(0);
        }
        return new Integer(getProperty(key)).intValue();
    }

    /**
     *
     * @param key
     * @return
     */
    public double getDoubleProperty(String key)
    {
        if (propertyIsNull(key))
        {
            System.err.println("**** Property: " + key + " is not loaded and has no value.");
            System.exit(0);
        }
        return new Double(getProperty(key)).doubleValue();
    }

    /**
     *
     * @param key
     * @return
     */
    public boolean getBooleanProperty(String key)
    {
        if (propertyIsNull(key))
        {
            System.err.println("**** Property: " + key + " is not loaded and has no value.");
            System.exit(0);
        }
        return new Boolean(getProperty(key)).booleanValue();
    }

    /**
     *
     * @param key
     * @return
     */
    public String getStringProperty(String key)
    {
        if (propertyIsNull(key))
        {
            System.err.println("**** Property: " + key + " is not loaded and has no value.");
            System.exit(0);
        }
        return new String(getProperty(key));
    }

    /**
     *
     * @param s
     * @return
     */
    public boolean propertyIsNull(String s)
    {
        if (getProperty(s) == null) return true;
        return false;
    }

    /**
     *
     * @param key
     * @param value
     */
    public void addProperty(String key, String value)
    {
        setProperty(key, value);
    }

    /**
     *
     * @param fileName
     */
    public void loadProperties(String fileName)
    {
        BufferedReader inFile = null;
        try
        {
            inFile = new BufferedReader(new FileReader(fileName));
        }
        catch (FileNotFoundException e)
        {
            LoggingManager.logInfo("**** Property file: " + fileName + " not found in " +
                                   new File(".").getAbsolutePath() + " - Exiting.");
            e.printStackTrace();
            System.exit(0);
        }

        try
        {
            String line;
            while ((line = inFile.readLine()) != null)
            {
                StringTokenizer st = new StringTokenizer(line);
                String param = st.nextToken().trim();
                String data = st.nextToken().trim();

                addProperty(param, data);
            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.exit(0);
        }
    }


}
