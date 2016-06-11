package edu.byu.cs.superasteroids.model.Asteroids;

import edu.byu.cs.superasteroids.model.Image;

/**
 * The class description of the octeroid.
 * The octeroid breaks into several pieces when blown up
 */
public class Octeroid extends Asteroid {

    public Octeroid(Image image)
    {
        super(image);
    }

    @Override
    public int addUponDeletion()
    {
        return 8;
    }
}
