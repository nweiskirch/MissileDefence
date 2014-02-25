package display.gui;

import display.interfaces.Display;
import display.interfaces.Displayable;
import display.interfaces.ViewFrameListener;
import utils.LoggingManager;
import utils.PropertyManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The ViewFrame class is the GUI window/interface that displays and displays
 * our objects as the move around within our application. The components of the
 * ViewFrame are:<br>
 * The canvas display - a DisplayWindow object<br>
 * A Pause Button - signals the application that a pause event has been
 * requested<br>
 * A Stop Button - signals the application that a stop event has been
 * requested<br>
 * <br>
 * Additionally, the ViewFrame catches the x,y location of each mouse press and
 * release and sends that data to the application
 */
public class ViewFrame extends JFrame implements ViewFrameListener, Display {

    private DisplayWindow displayArea; // display canvas
    private JButton pauseButton = new JButton("Pause");
    private JButton stopButton = new JButton("Stop");
    private static int pixelsX;
    private static int pixelsY;
    private ViewFrameListener viewFrameListener;
    private boolean paused = false;

    /**
     * This constructor creates a ViewFrame object with no ViewFrameListener
     * (listens for the mouse & button events).
     */
    public ViewFrame() {
        this(null);
    }

    /**
     * This constructor creates a ViewFrame object with the specified
     * ViewFrameListener (listens for the mouse & button events). The
     * ViewFrameListener is usually an object in your application.
     *
     * @param vfl the associated ViewFrameListener (listens for the mouse &
     * button events).
     */
    public ViewFrame(ViewFrameListener vfl) {
        super("View Frame");
        setPixelsX(PropertyManager.Instance().getIntProperty("PIXELSX"));
        setPixelsY(PropertyManager.Instance().getIntProperty("PIXELSY"));
        setViewFrameListener(vfl);

        setDisplayArea(new DisplayWindow(this, getPixelsX(), getPixelsY()));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                acceptClose();
            }
        ;
        });
		getContentPane().setLayout(new BorderLayout());
        getContentPane().add(getDisplayArea(), BorderLayout.CENTER);
        getPauseButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!isPaused()) {
                    getPauseButton().setText("Continue");
                    setPaused(true);
                } else {
                    getPauseButton().setText("Pause");
                    setPaused(false);
                }
                acceptPause();
            }
        });
        getStopButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                acceptStop();
            }
        });


        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2));
        buttonPanel.add(getPauseButton());
        buttonPanel.add(getStopButton());

        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setSize(getPixelsX() + 18, getPixelsY() + 65);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    /**
     * This method send the "Displayable" object passed in to the DisplayWindow
     * object for addition to the list of elements that are drawn in the display
     * window.
     *
     * @param d the "Displayable" object to be added to the list of elements
     * that are drawn in the display window.
     */
    public void addContent(Displayable d) {
        getDisplayArea().addContent(d);
    }

    /**
     * This method send the "Displayable" object passed in to the DisplayWindow
     * object for removal from the list of elements that are drawn in the
     * display window.
     *
     * @param d the "Displayable" object to be added to the list of elements
     * that are drawn in the display window.
     * @param delay
     */
    public void removeContent(Displayable d, int delay) {
        getDisplayArea().removeContent(d, delay);
    }

    /**
     * This method returns the number of elements in the display
     *
     * @return int
     */
    public int contentSize() {
        return getDisplayArea().contentSize();
    }

    /**
     * This method instructs the DisplayWindow object to re-draw the screen.
     * This is usually called when you want to update the drawn elements after
     * their locations have been updated or when an element has been added or
     * removed from the list of elements drawn on the DisplayWindow.
     */
    public void updateDisplay() {
        getDisplayArea().repaint();
    }

    private ViewFrameListener getViewFrameListener() {
        return viewFrameListener;
    }

    /**
     * This method accepts and then sets the ViewFrame's ViewFrameListener
     * object (listens for the mouse & button events). The ViewFrameListener is
     * usually an object in your application.
     *
     * @param viewFrameListenerIn the ViewFrame's ViewFrameListener object
     * (listens for the mouse & button events).
     */
    public void setViewFrameListener(ViewFrameListener viewFrameListenerIn) {
        viewFrameListener = viewFrameListenerIn;
    }

    /**
     * This method receives the "pause" request from the DisplayWindow and
     * forwards it to the ViewFrameListener (if any). The ViewFrameListener is
     * usually an object in your application.
     */
    public void acceptPause() {
        LoggingManager.logInfo("Pause Pressed!");
        if (getViewFrameListener() != null) {
            getViewFrameListener().acceptPause();
        }
    }

    /**
     * This method receives the "stop" request from the DisplayWindow and
     * forwards it to the ViewFrameListener (if any). The ViewFrameListener is
     * usually an object in your application.
     */
    public void acceptStop() {
        LoggingManager.logInfo("Stop Pressed!");
        if (getViewFrameListener() != null) {
            getViewFrameListener().acceptStop();
        }
    }

    /**
     * This method receives the "close" event (i.e., the ViewFrame window was
     * closed) from the DisplayWindow and forwards it to the ViewFrameListener
     * (if any). The ViewFrameListener is usually an object in your application.
     */
    public void acceptClose() {
        if (getViewFrameListener() != null) {
            getViewFrameListener().acceptClose();
        }
        System.exit(0);
    }

    /**
     * This method receives the left mouse button press & release event from the
     * DisplayWindow and forwards it to the ViewFrameListener (if any). The
     * ViewFrameListener is usually an object in your application.
     *
     * @param pressX
     * @param pressY
     * @param releaseX
     * @param releaseY
     */
    public void acceptLeftButtonMouseCommand(int pressX, int pressY, int releaseX, int releaseY) {
        if (getViewFrameListener() != null) {
            getViewFrameListener().acceptLeftButtonMouseCommand(pressX, pressY, releaseX, releaseY);
        }
    }

    /**
     * This method receives the right mouse button press & release event from
     * the DisplayWindow and forwards it to the ViewFrameListener (if any). The
     * ViewFrameListener is usually an object in your application.
     *
     * @param pressX
     * @param pressY
     * @param releaseX
     * @param releaseY
     */
    public void acceptRightButtonMouseCommand(int pressX, int pressY, int releaseX, int releaseY) {
        if (getViewFrameListener() != null) {
            getViewFrameListener().acceptRightButtonMouseCommand(pressX, pressY, releaseX, releaseY);
        }
    }

    private DisplayWindow getDisplayArea() {
        return displayArea;
    }

    private void setDisplayArea(DisplayWindow displayAreaIn) {
        displayArea = displayAreaIn;
    }

    private JButton getPauseButton() {
        return pauseButton;
    }

    private JButton getStopButton() {
        return stopButton;
    }

    private boolean isPaused() {
        return paused;
    }

    private void setPaused(boolean pausedIn) {
        paused = pausedIn;
    }

    /**
     * This method will cause a pop-up information window to be displayed with
     * the message provided in the method's parameter.
     *
     * @param msg the message
     */
    public void popUpInfo(String msg) {
        JOptionPane.showMessageDialog(null, msg, "Report", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * This method is a simple accessor that returns the x-size of the
     * DisplayWindow in pixels
     *
     * @return the x-size of the DisplayWindow in pixels
     */
    public static int getPixelsX() {
        return pixelsX;
    }

    /**
     * This method is a simple modifier that sets the x-size of the
     * DisplayWindow in pixels
     *
     * @param pixelsXIn the x-size of the DisplayWindow in pixels
     */
    public static void setPixelsX(int pixelsXIn) {
        ViewFrame.pixelsX = pixelsXIn;
    }

    /**
     * This method is a simple accessor that returns the y-size of the
     * DisplayWindow in pixels
     *
     * @return the y-size of the DisplayWindow in pixels
     */
    public static int getPixelsY() {
        return pixelsY;
    }

    /**
     * This method is a simple modifier that sets the x-size of the
     * DisplayWindow in pixels
     *
     * @param pixelsYIn the y-size of the DisplayWindow in pixels
     */
    public static void setPixelsY(int pixelsYIn) {
        ViewFrame.pixelsY = pixelsYIn;
    }
}
