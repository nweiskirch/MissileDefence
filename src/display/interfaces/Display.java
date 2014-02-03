package display.interfaces;

/**
 * The Display interface defines the methods that any object implementing the Display
 * interface must implement in order to be considered a valid Display.
 */
public interface Display
{


	public void addContent(Displayable d);

	public void removeContent(Displayable d, int delay);

	public int contentSize();

	public void updateDisplay();

	public void setViewFrameListener(ViewFrameListener viewFrameListener);

	public void popUpInfo(String msg);

}
