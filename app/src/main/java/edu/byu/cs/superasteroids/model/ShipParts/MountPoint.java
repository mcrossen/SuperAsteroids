package edu.byu.cs.superasteroids.model.ShipParts;

import android.graphics.Point;
import android.graphics.PointF;

import edu.byu.cs.superasteroids.core.GraphicsUtils;
import edu.byu.cs.superasteroids.model.Image;

/**
 * The class definition of the MountPoint struct.
 * This is a helper class because the relative coordinates of ship parts are used a lot.
 */
public class MountPoint {
    /**
     * the constructor for the MountPoint class
     * @param _x    the relative location of the ship part on the main body
     * @param _y    the relative location of the ship part on the main body
     */
    public MountPoint(int _x, int _y, Image image)
    {
        x = _x - image.getWidth()/2;
        y = _y - image.getHeight()/2;
    }

    private MountPoint(int _x, int _y)
    {
        x = _x;
        y = _y;
    }

    /*public MountPoint adjustAngleScale(ShipPart part, float scale)
    {
        PointF center = GraphicsUtils.rotate(new PointF(scale*x, scale*y), GraphicsUtils.toRadians(part.getRotation()));
        return new MountPoint((int)center.x, (int)center.y);
    }*/

    /**
     * the mount point consists of an x coordinate on the ship body
     */
    public int x;

    /**
     * the mount point also consists of a y coordinate on the ship body
     */
    public int y;

    @Override
    public boolean equals(Object o)
    {
        if (o == null)
        {
            return false;
        }
        else if (this == o)
        {
            return true;
        }
        else if (getClass() != o.getClass())
        {
            return false;
        }
        else
        {
            MountPoint other = (MountPoint)o;

            if (x != other.x)
            {
                return false;
            }
            else if (y != other.y)
            {
                return false;
            }
            else
            {
                return true;
            }
        }
    }
}