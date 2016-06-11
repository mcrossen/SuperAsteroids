package edu.byu.cs.superasteroids.model;


import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import edu.byu.cs.superasteroids.content.ContentManager;
import edu.byu.cs.superasteroids.drawing.DrawingHelper;
import edu.byu.cs.superasteroids.game.ViewPort;
import edu.byu.cs.superasteroids.model.Asteroids.Asteroid;
import edu.byu.cs.superasteroids.model.Asteroids.AsteroidFragment;
import edu.byu.cs.superasteroids.model.Asteroids.GrowingAsteroid;

/**
 * The level contains all the information that is needed for each level. From asteroids to background objects. It also contains music files and such.
 */
public class Level {

    /**
     * the constructor for the level needs the following information
     * @param _level_number         the corresponding level number
     * @param _title                the title of the level
     * @param _hint                 the level hint
     * @param _width                the width of the level
     * @param _height               the height of the level
     * @param _music                the background music of the level
     * @param _background_objects   the background objects on the level
     * @param _level_asteroids      the asteroids on the level
     */
    public Level(int _level_number, String _title, String _hint, int _width, int _height, String _music, List<BackgroundObject> _background_objects, List<Asteroid> _level_asteroids)
    {
        level_number = _level_number;
        title = _title;
        hint = _hint;
        width = _width;
        height = _height;
        music = _music;
        background_objects = _background_objects;
        level_asteroids = _level_asteroids;

        //randomize the starting locations and speed of the asteroids
        Iterator<Asteroid> index = level_asteroids.iterator();

        while (index.hasNext())
        {
            index.next().init(width, height);
        }
    }

    /**
     * the draw method also needs to be written. The draw method goes through the background objects, asteroids, etc and draws them on the map.
     */
    public void draw()
    {
        //draw the background objects
        Iterator<BackgroundObject> object_index = background_objects.iterator();

        while (object_index.hasNext())
        {
            object_index.next().draw();
        }

        //draw the asteroids
        Iterator<Asteroid> asteroid_index = level_asteroids.iterator();

        while (asteroid_index.hasNext())
        {
            asteroid_index.next().draw();
        }

        //draw the text on the top to inform user of remaining asteroids (my custom thing).
        DrawingHelper.drawText(new Point(0, 50), "Level:" + Integer.toString(level_number) + ", Asteroids Left:" + Integer.toString(level_asteroids.size()), Color.WHITE, 50);
    }

    public void loadContent(ContentManager content)
    {
        //create a history to keep track of loaded content
        Map<String, Integer> used_paths = new HashMap<>();

        //load all used asteroid images into memory.
        Iterator<Asteroid> current_asteroid = level_asteroids.iterator();

        while (current_asteroid.hasNext())
        {
            Image asteroid_pic = current_asteroid.next().getImage();

            //don't load more than once
            if (used_paths.containsKey(asteroid_pic.getPath()))
            {
                asteroid_pic.setContentID(used_paths.get(asteroid_pic.getPath()));
            }
            else
            {
                int content_id = content.loadImage(asteroid_pic.getPath());
                used_paths.put(asteroid_pic.getPath(), content_id);
                asteroid_pic.setContentID(content_id);
            }
        }


        //load all used background images into memory
        Iterator<BackgroundObject> current_object = background_objects.iterator();

        while(current_object.hasNext())
        {
            Image object_pic = current_object.next().getImage();

            //don't load more than once.
            if (used_paths.containsKey(object_pic.getPath()))
            {
                object_pic.setContentID(used_paths.get(object_pic.getPath()));
            }
            else
            {
                int content_id = content.loadImage(object_pic.getPath());
                used_paths.put(object_pic.getPath(), content_id);
                object_pic.setContentID(content_id);
            }
        }

        try {
            music_id = content.loadLoopSound(music);
        } catch (java.io.IOException e) {
            music_id = -1;
        }
    }

    public void unloadContent(ContentManager content)
    {
        //create a history, so it doesn't unload things more than twice.
        Map<String, Integer> used_paths = new HashMap<>();

        //unload all the asteroids
        Iterator<Asteroid> current_asteroid = level_asteroids.iterator();

        while (current_asteroid.hasNext())
        {
            Image asteroid_pic = current_asteroid.next().getImage();

            if (!used_paths.containsKey(asteroid_pic.getPath()))
            {
                content.unloadImage(asteroid_pic.getContentID());
            }
        }

        //unload all the background objects
        Iterator<BackgroundObject> current_object = background_objects.iterator();

        while(current_object.hasNext())
        {
            Image object_pic = current_object.next().getImage();

            if (!used_paths.containsKey(object_pic.getPath()))
            {
                content.unloadImage(object_pic.getContentID());
            }
        }

        content.unloadLoopSound(music_id);
    }

    public int getLevelNumber() {
        return level_number;
    }

    public String getTitle() {
        return title;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public RectF getBound()
    {
        return new RectF(0, 0, width, height);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public PointF getCenter()
    {
        return new PointF(width/2, height/2);
    }

    public String getMusic() {
        return music;
    }

    //check if the level has been beat
    public boolean isFinished()
    {
        if (level_asteroids.size() == 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void update(double elapsedTime)
    {
        //new asteroid fragments must be kept in a temporary list before being added to the main list
        List<Asteroid> new_asteroids = new LinkedList<>();

        //iterate over all the asteroids and update them
        Iterator<Asteroid> asteroid_index = level_asteroids.iterator();

        while (asteroid_index.hasNext())
        {
            Asteroid current_asteroid = asteroid_index.next();

            current_asteroid.update(elapsedTime);

            //check if the asteroid was shot
            if (current_asteroid.needsDeletion())
            {
                //add a number of asteroid fragments based on what type of asteroid it is.
                for (int index = 0; index < current_asteroid.addUponDeletion(); index++)
                {
                    //set the new asteroid's coordinates to the old's coordinates.
                    Asteroid temp = new AsteroidFragment(current_asteroid.image, current_asteroid.getMapCoords());

                    if (current_asteroid.isGrowing())
                    {
                        //if the parent was a growing asteroid, set the fragment to grow.
                        temp = new GrowingAsteroid(current_asteroid.image, current_asteroid.getMapCoords());
                    }
                    new_asteroids.add(temp);
                }
                asteroid_index.remove();
            }
        }

        //I have to add the new asteroids into a separate list and then into the new list to avoid a concurrent modification exception
        asteroid_index = new_asteroids.iterator();

        while (asteroid_index.hasNext())
        {
            Asteroid temp = asteroid_index.next();
            temp.setViewPort(viewport);
            temp.initVector(width, height);
            level_asteroids.add(temp);
        }
    }

    private ViewPort viewport;

    //the viewport is needed by all objects to determine where it should be drawn on screen in relation to its world coordinates.
    public void setViewPort(ViewPort _viewport)
    {
        viewport = _viewport;

        Iterator<Asteroid> asteroid_index = level_asteroids.iterator();

        while (asteroid_index.hasNext())
        {
            asteroid_index.next().setViewPort(_viewport);
        }

        Iterator<BackgroundObject> object_index = background_objects.iterator();

        while (object_index.hasNext())
        {
            object_index.next().setViewPort(_viewport);
        }
    }

    public List<Asteroid> getLevelAsteroids()
    {
        return level_asteroids;
    }

    /**
     * the level index of the instantiated level is stored here.
     */
    private int level_number;

    /**
     * the title of the associated level is stored here as a string.
     */
    private String title;

    /**
     * the level hint of the current level is stored here as a string.
     */
    private String hint;

    /**
     * the width of the map of the level is stored here as a string.
     */
    private int width;

    /**
     * the height of the map of the level is stored here as a string.
     */
    private int height;

    /**
     * the path to the music file that plays during this level is stored here as a string.
     */
    private String music;

    private int music_id = -1;

    public void playMusic()
    {
        ContentManager.getInstance().playLoop(music_id);
    }

    /**
     * the collection of background objects on the level is stored here.
     */
    private List<BackgroundObject> background_objects;

    //draw the transition between levels.
    public void drawTransition()
    {
        DrawingHelper.drawCenteredText("Level " + Integer.toString(level_number) + " - " + hint, 100, Color.WHITE);
    }

    /**
     * the collection of asteroids on the level is stored here.
     */
    private List<Asteroid> level_asteroids;// = new HashMap<Integer, Integer>();
}
