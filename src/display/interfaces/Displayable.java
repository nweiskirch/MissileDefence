package display.interfaces;

import utils.Point3D;

/**
 * The Displayable interface defines the methods that any object implementing the Displayable
 * interface must implement in order to be considered a Displayable object.
 */

public interface Displayable
{
	public Point3D getLocation();

	public String getSymbol();
}
