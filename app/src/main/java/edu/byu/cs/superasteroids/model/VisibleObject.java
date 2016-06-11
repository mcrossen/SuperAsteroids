package edu.byu.cs.superasteroids.model;

/**
 * A visible object is a higher object in the inheritance tree.
 * In fact, all other model objects inherit from it.
 * These are the objects that are displayed on the screen.
 */
public abstract class VisibleObject extends Object {

    /**
     * the constructor of the visible object
     * @param _image    how the object looks
     */
    public VisibleObject(Image _image)
    {
        image = _image;
    }

    /**
     * the draw method is declared here. All instantiated objects must have it
     */
    public abstract void draw();

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * where the looks of the object is stored
     */
    protected Image image;

    /**
     * the current rotation of the object is stored here.
     */
    private float rotation_deg = 0;

    public float getRotation()
    {
        return rotation_deg;
    }

    public float getScale()
    {
        return scale;
    }

    public void setScale(float _scale)
    {
        scale = _scale;
    }

    private float scale = (float)1;

    public void setRotation(float rotation)
    {
        rotation_deg = rotation;
        while(rotation_deg < 0)
        {
            rotation_deg += 360;
        }
        while(rotation_deg >= 360)
        {
            rotation_deg -= 360;
        }
    }

}
