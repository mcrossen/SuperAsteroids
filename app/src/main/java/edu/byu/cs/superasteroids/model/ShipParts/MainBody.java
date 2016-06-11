package edu.byu.cs.superasteroids.model.ShipParts;


import edu.byu.cs.superasteroids.model.Image;

public class MainBody extends ShipPart {

    public MainBody(Image image, MountPoint _cannon_attach, MountPoint _engine_attach, MountPoint _extra_attach)
    {
        //the mount point is irrelevant here, so set it to the center of the body.
        super(image, new MountPoint(image.getWidth()/2, image.getHeight()/2, image));
        cannon_attach = _cannon_attach;
        engine_attach = _engine_attach;
        extra_attach = _extra_attach;
    }

    @Override
    public MountPoint getBodyAttachPoint(MainBody main_body)
    {
        return mount_point;
    }

    public MountPoint getCannonAttach() {
        return cannon_attach;
    }

    public MountPoint getEngineAttach() {
        return engine_attach;
    }

    public MountPoint getExtraAttach() {
        return extra_attach;
    }

    /**
     * the location to attach the cannon onto is determined from the database and passed into the constructor.
     */
    private MountPoint cannon_attach;

    /**
     * the location to attach the engine onto is determined from the database and passed into the constructor.
     */
    private MountPoint engine_attach;

    /**
     * the location to attach the extra part onto is determined from the database and passed into the constructor.
     */
    private MountPoint extra_attach;
}
