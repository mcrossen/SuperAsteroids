package edu.byu.cs.superasteroids.model.Asteroids;

import android.graphics.PointF;

import java.util.Iterator;

import edu.byu.cs.superasteroids.core.GraphicsUtils;
import edu.byu.cs.superasteroids.model.Image;
import edu.byu.cs.superasteroids.model.MovingObject;
import edu.byu.cs.superasteroids.model.ShipParts.Projectile;
import edu.byu.cs.superasteroids.model.ShipParts.Ship;

/**
 * Most of the behavior of the asteroid class is described by its parents.
 * This class is abstract. It's not meant to be instantiated, rather its subtypes can be instantiated.
 * It defines the the behaviors common to all asteroids.
 */
public abstract class Asteroid extends MovingObject {

    private static final int MAX_SPEED = 400;

    private static final int MIN_SPEED = 200;

    private static final double SCALE_INCREMENT = 1.1;

    public Asteroid(Image image)
    {
        super(image, new PointF(0, 0), -1, -1); //the start values are not important until the init function is ran
        setScale(1);
        enableRotationDrift();
    }

    @Override
    public void update(double elapsedTime)
    {
        GraphicsUtils.RicochetObjectResult result = GraphicsUtils.ricochetObject(getMapCoords(), getBound(), GraphicsUtils.toRadians(direction_deg - 90), level_width, level_height);
        setMapCoords(result.getNewObjPosition(), result.getNewObjBounds());
        direction_deg = (float)GraphicsUtils.radiansToDegrees(result.getNewAngleRadians());
        super.update(elapsedTime);
        checkCollisions(Ship.getInstance());
    }

    public int addUponDeletion()
    {
        return 2;
    }

    public void checkCollisions(Ship ship)
    {
        if (getBound().intersect(ship.getBound()))
        {
            ship.touch();
            touch();
        }

        Iterator<Projectile> bullet_index = ship.getProjectiles().iterator();

        while(bullet_index.hasNext())
        {
            Projectile current_bullet = bullet_index.next();
            if (getBound().intersect(current_bullet.getBound()))
            {
                current_bullet.touch();
                touch();
            }
        }
    }

    public void init(float level_width, float level_height)
    {
        setMapCoords(new PointF((float)Math.random() * level_width, (float)Math.random() * level_width));
        initVector(level_width, level_height);
    }

    public void initVector(float level_width, float level_height)
    {
        this.level_width = level_width;
        this.level_height = level_height;
        speed = (int)((Math.random() * (MAX_SPEED - MIN_SPEED)) + MIN_SPEED);
        direction_deg = (int)(Math.random() * 360);
    }

    private float level_width;

    private float level_height;

    public boolean isGrowing()
    {
        return false;
    }
}