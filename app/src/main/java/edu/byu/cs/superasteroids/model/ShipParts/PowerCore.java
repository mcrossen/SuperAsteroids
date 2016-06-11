package edu.byu.cs.superasteroids.model.ShipParts;

import edu.byu.cs.superasteroids.model.Image;

/**
 * The power core is a ship element that boosts the cannon and engine of a ship.
 * This attaches to a ship, so it extends the ShipPart class.
 */
public class PowerCore extends ShipPart {

    /**
     * the constructor for the power core
     * @param image         how the power core looks
     * @param _cannon_boost how much the core boosts the cannon by
     * @param _engine_boost how much the core boosts the engine by
     */
    public PowerCore(Image image, int _cannon_boost, int _engine_boost)
    {
        super(image, null);
        cannon_boost = _cannon_boost;
        engine_boost = _engine_boost;
    }

    @Override
    public MountPoint getBodyAttachPoint(MainBody main_body)
    {
        return null;
    }

    public int getCannonBoost() {
        return cannon_boost;
    }

    public void setCannonBoost(int cannon_boost) {
        this.cannon_boost = cannon_boost;
    }

    public int getEngineBoost() {
        return engine_boost;
    }

    public void setEngineBoost(int engine_boost) {
        this.engine_boost = engine_boost;
    }

    /**
     * the data regarding how much the cannon is strengthened by is recorded as an integer
     */
    private int cannon_boost;

    /**
     * the data regarding how much the engine is strengthened by is recorded as an integer
     */
    private int engine_boost;
}
