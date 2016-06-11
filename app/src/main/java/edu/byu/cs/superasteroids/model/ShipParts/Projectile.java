package edu.byu.cs.superasteroids.model.ShipParts;

import android.graphics.PointF;

import edu.byu.cs.superasteroids.model.Image;
import edu.byu.cs.superasteroids.model.MovingObject;

/**
 * A projectile is fired by a cannon. It moves in a straight line until it expires or hits an object.
 * Thus, it inherits from the MovingObject class.
 */
public class Projectile extends MovingObject {

    private static final int PROJECTILE_SPEED = 450;

    private double time_remaining = 5;
    /**
     * the constructor for a projectile
     * @param image     how the projectile looks
     * @param direction_deg the intiial direction of the projectile
     */
    Projectile(Image image, PointF map_coords, float direction_deg, float scale)
    {
        super(image, map_coords, PROJECTILE_SPEED, direction_deg);
        setRotation(direction_deg);
        setScale(scale);
    }

    @Override
    public void update(double elapsedTime)
    {
        time_remaining -= elapsedTime;
        super.update(elapsedTime);
    }

    public boolean needsDeletion()
    {
        if (time_remaining <= 0)
        {
            return true;
        }
        else
        {
            return super.needsDeletion();
        }
    }
}
