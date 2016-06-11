package edu.byu.cs.superasteroids.game;

import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

import edu.byu.cs.superasteroids.drawing.DrawingHelper;
import edu.byu.cs.superasteroids.model.ShipParts.Ship;

/**
 * Created by Mark on 2/27/2016.
 */
public class ViewPort {

    Ship ship;

    RectF dimensions;

    RectF level_dimensions;


    public ViewPort(Ship _ship, RectF _level_dimensions)
    {
        ship = _ship;

        level_dimensions = _level_dimensions;

        update();
    }

    public void update()
    {
        dimensions = new RectF(
                ship.getMapCoords().x - DrawingHelper.getGameViewWidth()/2,
                ship.getMapCoords().y - DrawingHelper.getGameViewHeight()/2,
                ship.getMapCoords().x + DrawingHelper.getGameViewWidth()/2,
                ship.getMapCoords().y + DrawingHelper.getGameViewHeight()/2);

        if (dimensions.bottom > level_dimensions.bottom)
        {
            float height = dimensions.height();
            dimensions.bottom = level_dimensions.bottom;
            dimensions.top = level_dimensions.bottom - height;
        }

        if (dimensions.right > level_dimensions.right)
        {
            float width = dimensions.width();
            dimensions.right = level_dimensions.right;
            dimensions.left = dimensions.right - width;
        }

        if (dimensions.top < level_dimensions.top)
        {
            float height = dimensions.height();
            dimensions.top = level_dimensions.top;
            dimensions.bottom = dimensions.top + height;
        }

        if (dimensions.left < level_dimensions.left)
        {
            float width = dimensions.width();
            dimensions.left = level_dimensions.left;
            dimensions.right = dimensions.left + width;
        }
    }

    public PointF convertToView(PointF mapCoords)
    {
        return new PointF(mapCoords.x - dimensions.left, mapCoords.y - dimensions.top);
    }

    public Rect convertToView(RectF bounds)
    {
        return new Rect((int)(bounds.left - dimensions.left),
                (int)(bounds.top - dimensions.top),
                (int)(bounds.right - dimensions.left),
                (int)(bounds.bottom - dimensions.top));
    }
}
