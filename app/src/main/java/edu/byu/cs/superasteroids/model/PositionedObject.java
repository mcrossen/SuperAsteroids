package edu.byu.cs.superasteroids.model;

import android.graphics.PointF;

import edu.byu.cs.superasteroids.drawing.DrawingHelper;
import edu.byu.cs.superasteroids.game.ViewPort;

/**
 * A positioned object is an object that has a position on the map.
 * Unlike space and the minimap, positioned objects have coordinates associated with them.
 */
public abstract class PositionedObject extends VisibleObject {

    public PositionedObject(Image image, PointF _map_coords)
    {
        super(image);

        if (_map_coords != null)
        {
            map_coords = _map_coords;
        }
        else
        {
            map_coords = new PointF(0, 0);
        }
    }

    /**
     * The draw method is overridden because drawing positioned objects is much different than drawing stationary objects like space or the map.
     * The draw method needs to draw the objects in the proper position on the screen, and only when they are in view.
     */
    @Override
    public void draw()
    {
        PointF view_coords = getViewCoords();
        DrawingHelper.drawImage(image.getContentID(), view_coords.x, view_coords.y, getRotation(), getScale(), getScale(), 255);
    }

    public void setViewPort(ViewPort viewPort)
    {
        view_port = viewPort;
    }

    protected ViewPort view_port;

    public PointF getViewCoords()
    {
        return view_port.convertToView(map_coords);
    }

    public PointF getMapCoords()
    {
        return map_coords;
    }

    public void setMapCoords(PointF point)
    {
        map_coords = point;
    }

    private PointF map_coords;

}
