package edu.byu.cs.superasteroids.model;

import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.RectF;

import edu.byu.cs.superasteroids.core.GraphicsUtils;
import edu.byu.cs.superasteroids.drawing.DrawingHelper;

/**
 * the Moving Object class is an abstract class that describes the qualities that each object has that inherits from the class.
 * For instance, each moving object has a speed and direction. In addition, moving objects can be fired upon.
 */
public abstract class MovingObject extends PositionedObject {

    private static float ROT_DRIFT_SPEED = (float)20; //the drift speed of objects in degrees per second

    enum ROTATION_DRIFT {CLOCKW, COUNTERCLOCKW, NONE};

    /**
     * the constructor for a moving object needs the following:
     * @param image         what the moving object looks like
     * @param _speed        the initial speed of the object
     */
    public MovingObject(Image image, PointF map_coords, int _speed, float _direction)
    {
        super(image, map_coords);
        speed = _speed;
        direction_deg = _direction;

        resetBounds();
    }

    private boolean isHit = false;

    public boolean needsDeletion()
    {
        if (isHit)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * the update method needs to be overridden so that the objects can detect when they have collided with another object.
     */
    public void update(double elapsedTime)
    {
        GraphicsUtils.MoveObjectResult new_pos = GraphicsUtils.moveObject(getMapCoords(), getBound(), speed, GraphicsUtils.toRadians(direction_deg - 90), elapsedTime);

        if (rot_drift == ROTATION_DRIFT.CLOCKW)
        {
            setRotation((float)(getRotation() + ROT_DRIFT_SPEED * elapsedTime));
        }
        else if (rot_drift == ROTATION_DRIFT.COUNTERCLOCKW)
        {
            setRotation((float)(getRotation() - ROT_DRIFT_SPEED * elapsedTime));
        }

        setMapCoords(new_pos.getNewObjPosition(), new_pos.getNewObjBounds());
    }

    /**
     * the "hit" method is declared here. pretty much, the hit method should be called when a bullet hits this object.
     */
    public void touch()
    {
        isHit = true;
    }

    @Override
    public void setMapCoords(PointF point)
    {
        float width = bound.width();
        float height = bound.height();
        setMapCoords(point, new RectF(point.x - width / 2, point.y - height / 2, point.x + width / 2, point.y + height / 2));
    }

    public void resetBounds()
    {
        float max_dim;
        if (image.getHeight() > image.getWidth())
        {
            max_dim = image.getHeight();
        }
        else
        {
            max_dim = image.getWidth();
        }

        max_dim = max_dim*getScale();

        bound = new RectF(
                getMapCoords().x - max_dim / 2,
                getMapCoords().y - max_dim / 2,
                getMapCoords().x + max_dim / 2,
                getMapCoords().y + max_dim / 2);
    }

    public void setMapCoords(PointF point, RectF _bound)
    {
        super.setMapCoords(point);

        bound = _bound;
    }

    /*@Override
    public void draw()
    {
        //DrawingHelper.drawRectangle(view_port.convertToView(bound), Color.WHITE, 255);
        super.draw();
    }*/

    @Override
    public void setScale(float _scale)
    {
        super.setScale(_scale);

        resetBounds();
    }

    public void enableRotationDrift()
    {
        double temp = Math.random();
        if (temp < .5)
        {
            rot_drift = ROTATION_DRIFT.CLOCKW;
        }
        else
        {
            rot_drift = ROTATION_DRIFT.COUNTERCLOCKW;
        }
    }

    /**
     * the current speed of the object is stored here as an integer.
     */
    protected int speed;

    protected float direction_deg;

    protected RectF bound;

    private ROTATION_DRIFT rot_drift = ROTATION_DRIFT.NONE;

    public RectF getBound()
    {
        return bound;
    }

    public void setDirection(float direction)
    {
        direction_deg = direction;

        while(direction_deg < 0)
        {
            direction_deg += 360;
        }
        while(direction_deg >= 360)
        {
            direction_deg -= 360;
        }
    }
}