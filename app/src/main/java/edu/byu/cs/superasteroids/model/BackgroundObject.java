package edu.byu.cs.superasteroids.model;

import android.graphics.PointF;

/**
 * this class describes the behavior of planets, nebulas, stations, and other background objects
 */
public class BackgroundObject extends PositionedObject {

    /**
     * the constructor for a background object
     * @param image     how the object looks
     * @param _scale    the scale to resize the picture by
     */
    public BackgroundObject(Image image, PointF start_coords, int _id, float _scale)
    {
        super(image, start_coords);
        scale = _scale;
        id = _id;
    }

    public float getScale() {
        return scale;
    }

    public int getID() {
        return id;
    }

    /**
     * the scale is the factor to resize the image by
     */
    private float scale;

    private int id;
}
