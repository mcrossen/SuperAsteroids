package edu.byu.cs.superasteroids.model.ShipParts;

import android.graphics.PointF;

import edu.byu.cs.superasteroids.core.GraphicsUtils;
import edu.byu.cs.superasteroids.drawing.DrawingHelper;
import edu.byu.cs.superasteroids.model.Image;

/**
 * The shippart class is a useful struct that keeps track of where parts are on ships.
 */
public abstract class ShipPart {

    /**
     * the constructor for a Ship Part
     * @param _image        what the ship part looks like
     * @param _mount_point  where the ship part mounts to the main body
     */
    public ShipPart(Image _image, MountPoint _mount_point)
    {
        image = _image;
        mount_point = _mount_point;
    }

    Image image;

    public Image getImage()
    {
        return image;
    }

    public void draw(PointF ship_location, MainBody main_body, float rotation, float scale)
    {
        //find the center of the image
        PointF pic_center = new PointF(
                scale*(getBodyAttachPoint(main_body).x - getMountPoint().x),
                scale*(getBodyAttachPoint(main_body).y - getMountPoint().y));

        //rotate the point.
        PointF rotated = GraphicsUtils.rotate(pic_center, rotation);

        //draw the rotated image
        DrawingHelper.drawImage(
                image.getContentID(),
                rotated.x + ship_location.x,
                rotated.y + ship_location.y,
                rotation,
                scale,
                scale,
                255);
    }

    //This method is mainly used for polymorphism and connecting parts to ship
    abstract public MountPoint getBodyAttachPoint(MainBody main_body);

    public MountPoint getMountPoint() {
        return mount_point;
    }

    public void setMountPoint(MountPoint mount_point) {
        this.mount_point = mount_point;
    }

    /**
     * each ship part has a unique point that connects to the mount point on the main body
     */
    protected MountPoint mount_point;
}
