package utils;

import java.awt.*;

/**
 *
 * @author Nate
 */
public class Point3D extends Point.Double
{

    /**
     *
     */
    public double z;

    /**
     *
     * @param xIn
     * @param yIn
     * @param zIn
     */
    public Point3D(double xIn, double yIn, double zIn)
	{
		setCoordinates(xIn, yIn, zIn);
	}

    /**
     *
     * @param xIn
     * @param yIn
     */
    public Point3D(double xIn, double yIn)
	{
		setCoordinates(xIn, yIn, 0.0);
	}

    /**
     *
     * @param aPoint
     */
    public Point3D(Point3D aPoint)
	{
		setCoordinates(aPoint.getX(), aPoint.getY(), aPoint.getZ());
	}

    /**
     *
     */
    public Point3D()
	{
	}

    /**
     *
     * @return
     */
    public double getZ()
	{
		return z;
	}

    /**
     *
     * @param xIn
     * @param yIn
     * @param zIn
     */
    public void setCoordinates(double xIn, double yIn, double zIn)
	{
		x = xIn;
		y = yIn;
		z = zIn;

	}

    /**
     *
     * @param aPoint
     */
    public void setLocation(Point3D aPoint)
	{
		setCoordinates(aPoint);
	}

    /**
     *
     * @param xIn
     * @param yIn
     * @param zIn
     */
    public void setLocation(double xIn, double yIn, double zIn)
	{
		setCoordinates(xIn, yIn, zIn);
	}

    /**
     *
     * @param aPoint
     */
    public void setCoordinates(Point3D aPoint)
	{
		setCoordinates(aPoint.getX(), aPoint.getY(), aPoint.getZ());
	}

	public String toString()
	{
		return "[" + x + ", " + y + ", " + z + "]";
	}

    /**
     *
     * @param xIn
     * @param yIn
     * @param zIn
     * @return
     */
    public double distance(double xIn, double yIn, double zIn)
	{
		xIn -= getX();
		yIn -= getY();
		zIn -= getZ();

		return Math.sqrt(xIn * xIn + yIn * yIn + zIn * zIn);
	}

    /**
     *
     * @param aPoint
     * @return
     */
    public double distance(Point3D aPoint)
	{
		return distance(aPoint.getX(), aPoint.getY(), aPoint.getZ());
	}

    /**
     *
     * @param aPoint3D
     * @return
     */
    public boolean equals(Point3D aPoint3D)
	{
		if ((getX() == aPoint3D.getX()) && (getY() == aPoint3D.getY()) && (getZ() == aPoint3D.getZ()))
			return true;
		else
			return false;
	}

}
