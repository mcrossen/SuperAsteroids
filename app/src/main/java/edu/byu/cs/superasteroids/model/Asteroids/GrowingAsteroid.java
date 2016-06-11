package edu.byu.cs.superasteroids.model.Asteroids;

import android.graphics.PointF;

import edu.byu.cs.superasteroids.model.Image;

/**
 * this asteroid has the growing special ability. It inherits from the asteroid abstract class.
 */
public class GrowingAsteroid extends Asteroid {

    private static final double SCALE_INCREMENT = 0.05;

    public GrowingAsteroid(Image image)
    {
        super(image);
    }

    public GrowingAsteroid(Image image, PointF _map_coords)
    {
        super(image);
        setScale((float) .5);
        setMapCoords(_map_coords);
    }

    @Override
    public void update(double elapsedTime)
    {
        setScale((float)(getScale() + elapsedTime*SCALE_INCREMENT));
        super.update(elapsedTime);
    }

    @Override
    public int addUponDeletion()
    {
        if (getScale() > 1) {
            return (int) (getScale() * super.addUponDeletion());
        }
        else
        {
            return 0;
        }
    }

    @Override
    public boolean isGrowing()
    {
        return true;
    }
}
