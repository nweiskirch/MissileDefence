package display.managers;

import display.interfaces.Display;
import display.interfaces.Displayable;
import display.interfaces.ViewFrameListener;
import utils.PropertyManager;


/**
 * The DisplayManager is a Singleton that acts as a Facade between the
 * application and the specifics of the display window.<br>
 * The DisplayManager uses the property "DISPLAYIMPL" to determine which type of object will be used as the actual display.
 * This is abstracted so that any subsystem that implements the Display interface can act as the application's display.
 */
public class DisplayManager implements Display
{
	private volatile static DisplayManager instance;
	private Display display;

    /**
     *
     * @return
     */
    public static DisplayManager getInstance()
	{
		if (instance == null)
		{
			synchronized (DisplayManager.class)
			{
				if (instance == null) // Double-Check!
					instance = new DisplayManager();
			}
		}
		return instance;
	}

	private DisplayManager()
	{
		try
		{
			Class theClass =
			        (Class) Class.forName(PropertyManager.Instance().getProperty("DISPLAYIMPL"));
			display = (Display) theClass.newInstance();
		}
		catch (InstantiationException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * This method sends the "Displayable" object passed in to the current display object for addition to the
	 * list of elements that are drawn in the display window.
	 *
	 * @param d the "Displayable" object to be added to the list of elements that are drawn in the display window.
	 */
	public void addContent(Displayable d)
	{
		display.addContent(d);
	}

	/**
	 * This method returns the number of elements in the display
	 *
	 * @return int
	 */

	public int contentSize()
	{
		return display.contentSize();
	}

	/**
	 * This method sends the "Displayable" object passed in to the current display object for removal from the
	 * list of elements that are drawn in the display window.
	 *
	 * @param d the "Displayable" object to be added to the list of elements that are drawn in the display window.
     * @param delay
	 */
	public void removeContent(Displayable d, int delay)
	{
		display.removeContent(d, delay);
	}

	/**
	 * This method sends the "ViewFrameListener" object passed in to the current display object in order to set the display's ViewFrameListener
	 * The ViewFrameListener is usually an object in your application.
	 *
	 * @param viewFrameListener the ViewFrame's ViewFrameListener object (listens for the mouse & button events).
	 */
	public void setViewFrameListener(ViewFrameListener viewFrameListener)
	{
		display.setViewFrameListener(viewFrameListener);
	}

	/**
	 * This method will cause a pop-up information window to be displayed with the message provided in the method's parameter.
	 *
	 * @param msg the message
	 */
	public void popUpInfo(String msg)
	{
		display.popUpInfo(msg);
	}

	/**
	 * This method instructs the display object to update itself.  This is usually done when you
	 * want to update the drawn elements after their locations have been updated or when an
	 * element has been added or removed from the list of elements drawn on the display.
	 */
	public void updateDisplay()
	{
		display.updateDisplay();
	}
}
