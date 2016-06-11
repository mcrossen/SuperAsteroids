package edu.byu.cs.superasteroids.model;

import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;

import edu.byu.cs.superasteroids.drawing.DrawingHelper;
import edu.byu.cs.superasteroids.game.ViewPort;

/**
 * This is merely the background of the game. It is a picture of many stars.
 */
public class Space extends PositionedObject {

    public Space(RectF level_bound)
    {
        super(new Image("images/space.bmp", 2048, 2048), null);
        setScale(level_bound.right, level_bound.bottom);
    }

    public void setScale(float width, float height)
    {
        scale_x = width / 2048;
        scale_y = height / 2048;
        setMapCoords(new PointF(width/2, height/2));
    }

    private float scale_x;

    private float scale_y;

    @Override
    public void draw()
    {
        PointF view_coords = view_port.convertToView(getMapCoords());
        DrawingHelper.drawImage(getImage().getContentID(), view_coords.x, view_coords.y, (float)0, scale_x, scale_y, 255);
    }
}