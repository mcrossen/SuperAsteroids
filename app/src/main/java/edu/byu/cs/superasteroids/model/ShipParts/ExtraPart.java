package edu.byu.cs.superasteroids.model.ShipParts;

import edu.byu.cs.superasteroids.model.Image;

/**
 * it doesn't do much, it just attaches to a ship
 */
public class ExtraPart extends ShipPart {

    /**
     * the constructor needs the following information
     * @param image         what the extra part looks like
     * @param mount_point   where the extra part connects to the ship
     */
    public ExtraPart(Image image, MountPoint mount_point)
    {
        super(image, mount_point);
    }

    @Override
    public MountPoint getBodyAttachPoint(MainBody main_body)
    {
        return main_body.getExtraAttach();
    }
}
