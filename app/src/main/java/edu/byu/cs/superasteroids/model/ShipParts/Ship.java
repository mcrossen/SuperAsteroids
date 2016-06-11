package edu.byu.cs.superasteroids.model.ShipParts;

import android.graphics.PointF;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import edu.byu.cs.superasteroids.content.ContentManager;
import edu.byu.cs.superasteroids.core.GraphicsUtils;
import edu.byu.cs.superasteroids.game.InputManager;
import edu.byu.cs.superasteroids.model.Image;
import edu.byu.cs.superasteroids.model.MovingObject;

/**
 * The ship is created in the ship editor.
 * It is comprised of many parts.
 * This is a singleton.
 */
public class Ship extends MovingObject {

    private static Ship ourInstance = new Ship();

    private static final double FIRE_PERIOD = .5; //the rate of fire between bullets (bullets per second)

    //private because its a singleton
    private Ship()
    {
        super(new Image(null, 650, 650), new PointF(0, 0), 0, 0);
    }

    public static Ship getInstance()
    {
        return ourInstance;
    }

    public static void createNew()
    {
        ourInstance = new Ship();
    }

    //a helper function that keeps track of if all the parts have been assigned (usually used in the ship builder)
    public boolean isComplete()
    {
        if (main_body != null &&
                extrapart != null &&
                cannon != null &&
                powercore != null &&
                engine != null)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public void update(double elapsedTime)
    {
        //don't fire constantly when the user presses finger. Keep track of the time between bullets
        time_until_fire -= elapsedTime;

        //check if the user pressed the screen
        if (InputManager.movePoint != null)
        {
            //set the direction to where the user touched.
            setDirection((float) GraphicsUtils.radiansToDegrees(Math.atan2((InputManager.movePoint.y - getViewCoords().y), (InputManager.movePoint.x - getViewCoords().x))));
            //rotation and direction should be the same
            setRotation(direction_deg);

            //since the user is touching the screen, move the ship (speed > 0)
            speed = engine.getBaseSpeed();

            //since the user is touching the screen, fire some bullets.
            //check if its time to fire.
            if (time_until_fire <= 0)
            {
                //find emit point
                PointF nozzle = new PointF(
                        getScale()*(main_body.getCannonAttach().x - cannon.getMountPoint().x + cannon.getNozzle().x),
                        getScale()*(main_body.getCannonAttach().y - cannon.getMountPoint().y + cannon.getNozzle().y));

                //rotate the emit point
                PointF rotated = GraphicsUtils.rotate(nozzle, getRotation());

                //create a new rotated bullet
                Projectile bullet = new Projectile(
                        cannon.getAttackImage(),
                        new PointF(rotated.x + getMapCoords().x, rotated.y + getMapCoords().y),
                                //getMapCoords().x + main_body.getCannonAttach().adjustAngleScale(main_body, rotation_deg).x - cannon.getMountPoint().adjustAngleScale(cannon, rotation_deg).x + cannon.getNozzle().adjustAngleScale(cannon, rotation_deg).x,
                                //getMapCoords().y + main_body.getCannonAttach().adjustAngleScale(main_body, rotation_deg).y - cannon.getMountPoint().adjustAngleScale(cannon, rotation_deg).y + cannon.getNozzle().adjustAngleScale(cannon, rotation_deg).y),
                        getRotation(),
                        getScale());

                //play a sound
                if (cannon.getAttackSoundID() != -1) {
                    ContentManager.getInstance().playSound(cannon.getAttackSoundID(), (float) .5, (float) 1);
                }

                //tell the bullet where it is on the screen
                bullet.setViewPort(view_port);

                //keep track of the bullets.
                bullets.add(bullet);

                //reset the timer.
                time_until_fire = FIRE_PERIOD;
            }
        }
        else
        {
            //the user isn't touching, so don't move the ship.
            speed = 0;
        }

        super.update(elapsedTime);

        //iterate over all the bullets
        Iterator<Projectile> bullet_index = bullets.iterator();

        while (bullet_index.hasNext())
        {
            Projectile current_bullet = bullet_index.next();

            //update all the bullets
            current_bullet.update(elapsedTime);
            if (current_bullet.needsDeletion())
            {
                bullet_index.remove();
            }
        }
    }

    public List<Projectile> getProjectiles()
    {
        return bullets;
    }

    /**
     * the draw method needs to be overridden to draw all the subparts as well
     * additionally, the ship can rotate. The normal draw method doesn't do rotates.
     */
    @Override
    public void draw()
    {
        draw(getViewCoords());
        //DrawingHelper.drawRectangle(view_port.convertToView(bound), Color.WHITE, 255);

        Iterator<Projectile> bullet_index = bullets.iterator();

        while (bullet_index.hasNext())
        {
            bullet_index.next().draw();
        }
    }

    //this method draws a ship in either the map or the ship builder.
    private void draw(PointF view_location)
    {
        draw(view_location, getScale());
    }

    public void draw(PointF view_location, float scale)
    {
        //draw each part.
        if (main_body != null)
        {
            main_body.draw(view_location, main_body, getRotation(), scale);

            if (extrapart != null)
            {
                extrapart.draw(view_location, main_body, getRotation(), scale);
            }
            if (cannon != null)
            {
                cannon.draw(view_location, main_body, getRotation(), scale);
            }
            if (engine != null)
            {
                engine.draw(view_location, main_body, getRotation(), scale);
            }
        }
    }

    public MainBody getMainBody() {
        return main_body;
    }

    public Cannon getCannon() {
        return cannon;
    }

    public ExtraPart getExtraPart() {
        return extrapart;
    }

    public Engine getEngine() {
        return engine;
    }

    public PowerCore getPowerCore() {
        return powercore;
    }

    public void setMainBody(MainBody main_body) {
        this.main_body = main_body;
    }

    public void setCannon(Cannon cannon) {
        this.cannon = cannon;
    }

    public void setExtraPart(ExtraPart extrapart) {
        this.extrapart = extrapart;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public void setPowerCore(PowerCore powercore) {
        this.powercore = powercore;
    }

    private double time_until_fire = 0;

    private MainBody main_body;

    /**
     * This cannon gets attached in the constructor.
     */
    private Cannon cannon;

    /**
     * This extra part gets attached in the constructor.
     */
    private ExtraPart extrapart;

    /**
     * This engine gets attached in the constructor.
     */
    private Engine engine;

    /**
     * This powercore gets attached in the constructor.
     */
    private PowerCore powercore;

    private static List<Projectile> bullets = new LinkedList<>();
}
