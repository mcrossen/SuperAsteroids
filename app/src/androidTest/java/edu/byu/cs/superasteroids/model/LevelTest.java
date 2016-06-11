package edu.byu.cs.superasteroids.model;

import android.graphics.PointF;
import android.graphics.RectF;

import junit.framework.TestCase;

import java.util.LinkedList;
import java.util.List;

import edu.byu.cs.superasteroids.model.Asteroids.Asteroid;
import edu.byu.cs.superasteroids.model.Asteroids.GrowingAsteroid;

public class LevelTest extends TestCase {
    List<BackgroundObject> background_objects = new LinkedList<>();
    List<Asteroid> asteroids = new LinkedList<>();
    Level level = new Level(1, "my title", "my hint", 2, 3, "music path", background_objects, asteroids);

    @Override
    public void setUp()
    {
        background_objects.add(new BackgroundObject(new Image("background object path", 1, 1), new PointF(0, 0), 1, (float).5));
        asteroids.add(new GrowingAsteroid(new Image("asteroid path", 1, 1)));
    }

    public void testGetLevelNumber() throws Exception {
        assertEquals(1, level.getLevelNumber());
    }

    public void testGetTitle() throws Exception {
        assertEquals("my title", level.getTitle());
    }

    public void testGetHint() throws Exception {
        assertEquals("my hint", level.getHint());
    }

    public void testGetBound() throws Exception {
        assertEquals(new RectF(0, 0, 2, 3), level.getBound());
    }

    public void testGetWidth() throws Exception {
        assertEquals(2, level.getWidth());
    }

    public void testGetHeight() throws Exception {
        assertEquals(3, level.getHeight());
    }

    public void testGetCenter() throws Exception {
        assertEquals(new PointF(1, 3/2), level.getCenter());
    }

    public void testGetMusic() throws Exception {
        assertEquals("music path", level.getMusic());
    }

    public void testIsFinished() throws Exception {
        assertEquals(false, level.isFinished());
    }

    public void testGetLevelAsteroids() throws Exception {
        List<Asteroid> more_asteroids = level.getLevelAsteroids();
        assertEquals(asteroids, more_asteroids);
        assertNotSame(new PointF(-1, -1), more_asteroids.iterator().next().getMapCoords());//tests the asteroid initialization function for new levels.
    }
}