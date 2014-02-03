package display.gui;

import display.interfaces.Displayable;
import utils.PropertyManager;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This class is the visual canvas upon which our elements are drawn and moved
 * around. The DisplayWindow itself is part of the ViewFrame object.
 */
public class DisplayWindow extends Canvas implements MouseListener {

    private Image offScrImg;
    private boolean unpacked = false;
    private int pressRightX;
    private int pressRightY;
    private int releaseRightX;
    private int releaseRightY;
    private int pressLeftX;
    private int pressLeftY;
    private int releaseLeftX;
    private int releaseLeftY;
    private int pixelsX;
    private int pixelsY;
    private ViewFrame parentFrame;
    private ArrayList contents = new ArrayList();
    private HashMap toBeRemoved = new HashMap();

    /**
     * The DisplayWindow is the visual canvas upon which our elements are drawn.
     * The constructor requires a ViewFrame object (the parent ViewFrame) and an
     * X & Y size in pixels.
     *
     * @param parent the parent ViewFrame (that owns this DisplayWindow)
     * @param xPixels the x-size of the DisplayWindow in pixels
     * @param yPixels the y-size of the DisplayWindow in pixels
     */
    public DisplayWindow(ViewFrame parent, int xPixels, int yPixels) {
        setParentFrame(parent);

        setPixelsX(xPixels); // For scaling use
        setPixelsY(yPixels);  // For scaling use

        setSize(xPixels, yPixels);
        addMouseListener(this);
    }

    /**
     * This method, declared in the Canvas class, re-draws the screen using
     * double-buffered graphics for smooth movement. This method does not need
     * to be (and should not be) called manually, it is invoked in the process
     * of a window refresh/update
     *
     * @param graphicsIn the java "Graphics" object passed in by the caller
     */
    public void update(Graphics graphicsIn) {
        if (getOffScrImg() == null || getOffScrImg().getWidth(this) != getPreferredSize().getWidth()
                || getOffScrImg().getHeight(this) != getPreferredSize().getHeight()) {
            setOffScrImg(createImage((int) getPreferredSize().getWidth(), (int) getPreferredSize().getHeight()));
        }
        Graphics og = getOffScrImg().getGraphics();
        paint(og);
        graphicsIn.drawImage(getOffScrImg(), 0, 0, this);
        og.dispose();
    }

    /**
     * This method will add the "Displayable" object passed in to the list of
     * elements that are drawn in the display window.
     *
     * @param d the "Displayable" object to be added to the list of elements
     * that are drawn in the display window.
     */
    public synchronized void addContent(Displayable d) {
        getContents().add(d);
        repaint();
    }

    /**
     * This method returns the number of elements in the display
     *
     * @return int
     */
    public int contentSize() {
        return getContents().size();
    }

    /**
     * This method will remove the "Displayable" object passed in from the list
     * of elements that are drawn in the display window. The int delay parameter
     * indicates how many screen updates much be made before the removed object
     * will actually be taken off of the screen.
     *
     * @param d the "Displayable" object to be removed from the list of elements
     * that are drawn in the display window.
     */
    public synchronized void removeContent(Displayable d, int delay) {
        getToBeRemoved().put(d, new Integer(delay));
        repaint();
    }

    /**
     * This method, declared in the Canvas class, is called by the "update"
     * method to refresh the screen and redraw each of the elements on the
     * display window.
     *
     * @param graphicsIn the java "Graphics" object passed in by the "update"
     * method
     */
    public void paint(Graphics graphicsIn) {
        if (!isUnpacked()) {
            setUnpacked(true);
        }

        graphicsIn.setColor(Color.black);
        graphicsIn.fillRect(0, 0, (int) getPreferredSize().getWidth(), (int) getPreferredSize().getHeight()); // draw  background
        graphicsIn.setColor(Color.white); // set font and write applet parameters
        graphicsIn.setFont(new Font("Dialog", Font.PLAIN, 10));

        paintContents(graphicsIn);

    }

    private void clearRemoved() {
        HashMap temphash = getToBeRemoved();
        Set tempset = temphash.entrySet();
        ArrayList a = null;
        try {
            a = new ArrayList(tempset);
        } catch (ConcurrentModificationException e) {
            a = new ArrayList();
        }
        for (int i = 0; i < a.size(); i++) {
            int value = ((Integer) ((Map.Entry) a.get(i)).getValue()).intValue();
            value--;
            if (value <= 0) {
                getToBeRemoved().remove(((Map.Entry) a.get(i)).getKey());
                getContents().remove(((Map.Entry) a.get(i)).getKey());
            } else {
                getToBeRemoved().put(((Map.Entry) a.get(i)).getKey(), new Integer(value));
            }
        }
    }

    /**
     * This method, declared in the Canvas class, is called by the "update"
     * method to refresh the screen and redraw each of the elements on the
     * display window.
     *
     * @param graphicsIn the java "Graphics" object passed in by the "paint"
     * method
     */
    private void paintContents(Graphics graphicsIn) {
        clearRemoved();
        ArrayList purge = new ArrayList();
        int size = getContents().size();
        for (int i = 0; i < size; i++) {

            graphicsIn.setFont(new Font("SansSerif", Font.BOLD, 12));
            graphicsIn.setColor(getColor(getContents(i).getLocation().getZ() / PropertyManager.Instance().getDoubleProperty("ZMAX")));

//	            LoggingManager.logInfo("Drawing(" + i + ") " + getContents(i).getSymbol() + " at: " + getContents(i).getLocation().getX() + ", " +
//	                               getContents(i).getLocation().getY() + ", " + getContents(i).getLocation().getZ() + ": " +
//                                   getColor(getContents(i).getLocation().getZ() / PropertyManager.Instance().getDoubleProperty("ZMAX")));

            graphicsIn.drawString(getContents(i).getSymbol(),
                    (int) (getContents(i).getLocation().getX()),
                    (int) (getContents(i).getLocation().getY()));

        }
        if (purge.size() > 0) {
            for (int i = 0; i < purge.size(); i++) {
                removeContent((Displayable) purge.get(i), PropertyManager.Instance().getIntProperty("REMOVEDELAY"));
            }
            purge.clear();
        }
    }

    /**
     * We don't make use of this event (part of the MouseListener interface) -
     * we only use the mousePressed/mouseReleased methods.
     *
     * @param e a MouseEvent
     */
    public void mouseClicked(MouseEvent e) {
        // We don't make use of this event - we only use mousePressed/mouseReleased
    }

    /**
     * This method records the x,y location of the initial right mouse button
     * click (before the release)
     *
     * @param e a MouseEvent
     */
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            setPressLeftX(e.getX());
            setPressLeftY(e.getY());
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            setPressRightX(e.getX());
            setPressRightY(e.getY());
        }

    }

    /**
     * This method records the x,y location where the right mouse button was
     * released (after it was clicked)
     *
     * @param e a MouseEvent
     */
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            setReleaseLeftX(e.getX());
            setReleaseLeftY(e.getY());
            getParentFrame().acceptLeftButtonMouseCommand(getPressLeftX(), getPressLeftY(), getReleaseLeftX(), getReleaseLeftY());
        }
        if (e.getButton() == MouseEvent.BUTTON3) {
            setReleaseRightX(e.getX());
            setReleaseRightY(e.getY());
            getParentFrame().acceptRightButtonMouseCommand(getPressRightX(), getPressRightY(), getReleaseRightX(), getReleaseRightY());
        }
    }

    /**
     * We don't make use of this event (part of the MouseListener interface) -
     * we only use the mousePressed/mouseReleased methods.
     *
     * @param e a MouseEvent
     */
    public void mouseEntered(MouseEvent e) {
        // We don't make use of this event - we only use mousePressed/mouseReleased
    }

    /**
     * We don't make use of this event (part of the MouseListener interface) -
     * we only use the mousePressed/mouseReleased methods.
     *
     * @param e a MouseEvent
     */
    public void mouseExited(MouseEvent e) {
        // We don't make use of this event - we only use mousePressed/mouseReleased
    }

    private Color getColor(double value) {
        if (value > 1.0) {
            value = 1.0;
        }

        int r, g;
        if (value < 0.5) {
            r = 255;
            g = (int) (510 * value);
        } else {
            r = (int) (510 * (1.0 - value));
            g = 255;
        }
        return new Color(r, g, 0);
    }

    private Image getOffScrImg() {
        return offScrImg;
    }

    private void setOffScrImg(Image offScrImgIn) {
        offScrImg = offScrImgIn;
    }

    private boolean isUnpacked() {
        return unpacked;
    }

    private void setUnpacked(boolean unpackedIn) {
        unpacked = unpackedIn;
    }

    private int getPressRightX() {
        return pressRightX;
    }

    private void setPressRightX(int pressXIn) {
        pressRightX = pressXIn;
    }

    private int getPressRightY() {
        return pressRightY;
    }

    private void setPressRightY(int pressYIn) {
        pressRightY = pressYIn;
    }

    private int getReleaseRightX() {
        return releaseRightX;
    }

    private void setReleaseRightX(int releaseXIn) {
        releaseRightX = releaseXIn;
    }

    private int getReleaseRightY() {
        return releaseRightY;
    }

    private void setReleaseRightY(int releaseYIn) {
        releaseRightY = releaseYIn;
    }

    private int getPressLeftX() {
        return pressLeftX;
    }

    private void setPressLeftX(int pressXIn) {
        pressLeftX = pressXIn;
    }

    private int getPressLeftY() {
        return pressLeftY;
    }

    private void setPressLeftY(int pressYIn) {
        pressLeftY = pressYIn;
    }

    private int getReleaseLeftX() {
        return releaseLeftX;
    }

    private void setReleaseLeftX(int releaseXIn) {
        releaseLeftX = releaseXIn;
    }

    private int getReleaseLeftY() {
        return releaseLeftY;
    }

    private void setReleaseLeftY(int releaseYIn) {
        releaseLeftY = releaseYIn;
    }

    private ViewFrame getParentFrame() {
        return parentFrame;
    }

    private void setParentFrame(ViewFrame parentFrameIn) {
        parentFrame = parentFrameIn;
    }

    private ArrayList getContents() {
        return contents;
    }

    private Displayable getContents(int i) {
        return (Displayable) contents.get(i);
    }

    /**
     * This method is a simple accessor that returns the x-size of the
     * DisplayWindow in pixels
     *
     * @return the x-size of the DisplayWindow in pixels
     */
    public int getPixelsX() {
        return pixelsX;
    }

    /**
     * This method is a simple modifier that sets the x-size of the
     * DisplayWindow in pixels
     *
     * @param pixelsXIn the x-size of the DisplayWindow in pixels
     */
    public void setPixelsX(int pixelsXIn) {
        pixelsX = pixelsXIn;
    }

    /**
     * This method is a simple accessor that returns the y-size of the
     * DisplayWindow in pixels
     *
     * @return the y-size of the DisplayWindow in pixels
     */
    public int getPixelsY() {
        return pixelsY;
    }

    /**
     * This method is a simple modifier that sets the y-size of the
     * DisplayWindow in pixels
     *
     * @param pixelsYIn the y-size of the DisplayWindow in pixels
     */
    public void setPixelsY(int pixelsYIn) {
        pixelsY = pixelsYIn;
    }

    private HashMap getToBeRemoved() {
        return toBeRemoved;
    }
}
