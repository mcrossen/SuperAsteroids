package edu.byu.cs.superasteroids.model.ShipParts;

import edu.byu.cs.superasteroids.model.Image;

/**
 * this attaches to the Ship class
 */
public class Engine extends ShipPart {

    /**
     * the constructor for an engine
     * @param image             how the engine looks
     * @param mount_point       where the engine connects to the ship
     * @param _base_speed       how fast the engine makes the ship move
     * @param _base_turn_rate   how fast the engine makes the ship turn by
     */
    public Engine(Image image, MountPoint mount_point, int _base_speed, int _base_turn_rate)
    {
        super(image, mount_point);
        base_speed = _base_speed;
        base_turn_rate = _base_turn_rate;
    }

    @Override
    public MountPoint getBodyAttachPoint(MainBody main_body)
    {
        return main_body.getEngineAttach();
    }

    public int getBaseSpeed() {
        return base_speed;
    }

    public void setBaseSpeed(int base_speed) {
        this.base_speed = base_speed;
    }

    public int getBaseTurnRate() {
        return base_turn_rate;
    }

    public void setBaseTurnRate(int base_turn_rate) {
        this.base_turn_rate = base_turn_rate;
    }

    /**
     * the speed of the engine is stored as an integer
     */
    private int base_speed;

    /**
     * the turning speed of the engine is also stored as an integer
     */
    private int base_turn_rate;
}
