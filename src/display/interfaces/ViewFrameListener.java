package display.interfaces;


/**
 * The ViewFrameListener interface defines the methods that any object implementing the Displayable
 * interface must implement in order to be considered a ViewFrameListener object.
 */
public interface ViewFrameListener
{
	public void acceptPause();

	public void acceptStop();

	public void acceptClose();

	public void acceptLeftButtonMouseCommand(int pressX, int pressY, int releaseX, int releaseY);

	public void acceptRightButtonMouseCommand(int pressX, int pressY, int releaseX, int releaseY);

}
