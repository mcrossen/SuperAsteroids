package edu.byu.cs.superasteroids.model.Asteroids;

import android.graphics.PointF;

import edu.byu.cs.superasteroids.model.Image;

/**
 * The asteroid fragment class is for the objects which get created when an asteroid explodes.
 */
public class AsteroidFragment extends Asteroid {

    /**
     * The constructor used to create the octeroid fragments
     * @param image     how the fragment looks
     */
    public AsteroidFragment(Image image, PointF _map_coords)
    {
        super(image);
        setScale((float) .5);
        setMapCoords(_map_coords);
    }

    @Override
    public int addUponDeletion()
    {
        return 0;
    }
}
