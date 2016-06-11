package edu.byu.cs.superasteroids.game;

import android.content.Context;
import android.graphics.Color;

import edu.byu.cs.superasteroids.base.IGameDelegate;
import edu.byu.cs.superasteroids.content.ContentManager;
import edu.byu.cs.superasteroids.database.Database;
import edu.byu.cs.superasteroids.drawing.DrawingHelper;
import edu.byu.cs.superasteroids.model.Level;
import edu.byu.cs.superasteroids.model.ShipParts.Ship;
import edu.byu.cs.superasteroids.model.Space;


public class GameDelegate implements IGameDelegate {

    private static final double TRANSITION_TIME = 5;

    private Ship ship = Ship.getInstance();
    private Level current_level;
    private Database db;
    private ViewPort viewport;
    private ContentManager content;
    private Space space;
    private MiniMap minimap;
    private boolean game_over = false;
    private boolean game_win = false;
    private double transition_time_left = TRANSITION_TIME;

    public GameDelegate(Context context)
    {
        db = new Database(context);
        ship.setScale((float) .25);
        ship.enableRotationDrift();
        newLevel();
    }

    /**
     * this method is called every time a new level is created.
     */
    private void newLevel()
    {
        //check if this is the first level creation
        if (current_level == null)
        {
            current_level = db.dao.getLevel(1);
        }
        else
        {
            //unload content from the last level
            unloadLevelContent();
            //load the next level
            int next_level = current_level.getLevelNumber() + 1;
            current_level = db.dao.getLevel(next_level);
        }

        //if the dao returned null, that means there are no more levels left and the game is over
        if (current_level == null)
        {
            game_win = true;
        }
        else
        {
            //create the minimap, background, ship, and viewport based on the level
            minimap = new MiniMap(current_level, ship);
            space = new Space(current_level.getBound());
            ship.setMapCoords(current_level.getCenter());
            viewport = new ViewPort(ship, current_level.getBound());

            //pass the viewport to the objects, so they can be drawn in relation to the viewport
            current_level.setViewPort(viewport);
            ship.setViewPort(viewport);
            space.setViewPort(viewport);
            space.setScale(current_level.getWidth(), current_level.getHeight());

            //load the level into memory
            if (content != null)
            {
                loadLevelContent();
            }

            //begin level transition scene
            transition_time_left = TRANSITION_TIME;
        }
    }

    @Override
    public void update(double elapsedTime)
    {
        //check if the player has lost or won yet
        if (game_over || game_win)
        {

        }
        //check if it is a transition between levels
        else if (transition_time_left > 0)
        {
            transition_time_left -= elapsedTime;
        }
        else//if it isn't a transition, do a normal update
        {
            //update the level and asteroids
            current_level.update(elapsedTime);
            //check if the player beat the level yet
            if (current_level.isFinished())
            {
                newLevel();
            }

            //update the ship position, bullets, rotation, etc
            ship.update(elapsedTime);

            //upate the location of the viewport
            viewport.update();

            //check if the game is over (ship died)
            if (ship.needsDeletion())
            {
                unloadContent(content);
                game_over = true;
            }
        }
    }

    @Override
    public void loadContent(ContentManager content) {
        //load the ship part images:
        //load the main body image
        ship.getMainBody().getImage().setContentID(content.loadImage(ship.getMainBody().getImage().getPath()));
        //load the cannon image
        ship.getCannon().getImage().setContentID(content.loadImage(ship.getCannon().getImage().getPath()));
        //load the engine image
        ship.getEngine().getImage().setContentID(content.loadImage(ship.getEngine().getImage().getPath()));
        //load the extra part image
        ship.getExtraPart().getImage().setContentID(content.loadImage(ship.getExtraPart().getImage().getPath()));
        //load the projectile image
        ship.getCannon().getAttackImage().setContentID(content.loadImage(ship.getCannon().getAttackImage().getPath()));
        //load the projectile sound
        try {
            ship.getCannon().setAttackSoundID(content.loadSound(ship.getCannon().getAttackSound()));
        } catch (java.io.IOException e) {
            ship.getCannon().setAttackSoundID(-1);
        }

        this.content = content;
        loadLevelContent();
    }

    private void loadLevelContent()
    {
        current_level.loadContent(content);

        space.getImage().setContentID(content.loadImage(space.getImage().getPath()));

        current_level.playMusic();
    }

    private void unloadLevelContent()
    {
        current_level.unloadContent(content);

        content.unloadImage(space.getImage().getContentID());
    }

    @Override
    public void unloadContent(ContentManager content) {
        content.unloadImage(ship.getMainBody().getImage().getContentID());
        content.unloadImage(ship.getCannon().getImage().getContentID());
        content.unloadImage(ship.getEngine().getImage().getContentID());
        content.unloadImage(ship.getExtraPart().getImage().getContentID());
        content.unloadImage(ship.getCannon().getAttackImage().getContentID());
        content.unloadSound(ship.getCannon().getAttackSoundID());
        unloadLevelContent();
    }

    @Override
    public void draw()
    {
        //check if the game is over and the player won
        if (game_win)
        {
            //if so, display the winning message
            DrawingHelper.drawCenteredText("Yay! You beat all the levels!", 100, Color.WHITE);
        }
        //check if the game is over and the player lost
        else if (game_over)
        {
            //if so, display the losing message
            DrawingHelper.drawCenteredText("Oops! You died. Push back to get to the menu", 100, Color.WHITE);
        }
        //check if the game is currently transitioning between levels
        else if (transition_time_left > 0)
        {
            //display the transition message
            current_level.drawTransition();
        }
        else
        {
            //normal game drawing behavior

            space.draw();

            current_level.draw();

            ship.draw();

            minimap.draw();
        }
    }
}