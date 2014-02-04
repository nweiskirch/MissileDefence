package display.interfaces;

/**
 * The Display interface defines the methods that any object implementing the Display
 * interface must implement in order to be considered a valid Display.
 */
public interface Display
{

    /**
     *
     * @param d
     */
    public void addContent(Displayable d);

    /**
     *
     * @param d
     * @param delay
     */
    public void removeContent(Displayable d, int delay);

    /**
     *
     * @return
     */
    public int contentSize();

    /**
     *
     */
    public void updateDisplay();

    /**
     *
     * @param viewFrameListener
     */
    public void setViewFrameListener(ViewFrameListener viewFrameListener);

    /**
     *
     * @param msg
     */
    public void popUpInfo(String msg);

}
