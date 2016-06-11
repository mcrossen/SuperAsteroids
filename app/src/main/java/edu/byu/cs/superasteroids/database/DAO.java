package edu.byu.cs.superasteroids.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PointF;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import edu.byu.cs.superasteroids.model.Asteroids.Asteroid;
import edu.byu.cs.superasteroids.model.BackgroundObject;
import edu.byu.cs.superasteroids.model.ShipParts.Cannon;
import edu.byu.cs.superasteroids.model.ShipParts.Engine;
import edu.byu.cs.superasteroids.model.ShipParts.ExtraPart;
import edu.byu.cs.superasteroids.model.Asteroids.GrowingAsteroid;
import edu.byu.cs.superasteroids.model.Image;
import edu.byu.cs.superasteroids.model.Level;
import edu.byu.cs.superasteroids.model.ShipParts.MainBody;
import edu.byu.cs.superasteroids.model.ShipParts.MountPoint;
import edu.byu.cs.superasteroids.model.Asteroids.Octeroid;
import edu.byu.cs.superasteroids.model.ShipParts.PowerCore;
import edu.byu.cs.superasteroids.model.Asteroids.RegularAsteroid;

/**
 * The Database Access Class is used to push and pull information from the SQLite database.
 */
public class DAO {

    /**
     * the constructor of the Database Access Class
     * @param _db   the only information needed is the location of the database to use.
     */
    public DAO(SQLiteDatabase _db)
    {
        db = _db;
    }

    private void addBGobjects(JSONObject root_node) throws org.json.JSONException
    {
        JSONArray background_objects = root_node.getJSONArray("objects");
        for (int i = 0; i < background_objects.length(); ++i)
        {
            ContentValues values = new ContentValues();
            values.put("imagepath", background_objects.getString(i));
            db.insert("BackgroundObjects", null, values);
        }
    }

    private void addAsteroids(JSONObject root_node) throws org.json.JSONException
    {
        JSONArray asteroids = root_node.getJSONArray("asteroids");
        for (int i = 0; i < asteroids.length(); ++i)
        {
            JSONObject current_asteroid = asteroids.getJSONObject(i);

            ContentValues values = new ContentValues();

            values.put("name", current_asteroid.getString("name"));
            values.put("image_path", current_asteroid.getString("image"));
            values.put("image_width", current_asteroid.getInt("imageWidth"));
            values.put("image_height", current_asteroid.getInt("imageHeight"));
            values.put("atype", current_asteroid.getString("type"));

            db.insert("AsteroidTypes", null, values);
        }
    }

    public List<Cannon> getCannons()
    {
        final String SQL =  "select image, image_width, image_height, " +
                                "attach_point_x, attach_point_y, " +
                                "emit_point_x, emit_point_y, " +
                                "attack_image, attack_image_width, attack_image_height, " +
                                "attack_sound, " +
                                "damage " +
                            "from Cannons ";

        List<Cannon> to_return = new LinkedList<>();

        Cursor cursor = db.rawQuery(SQL, new String[]{});
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                Image image = new Image(
                        cursor.getString(0),
                        cursor.getInt(1),
                        cursor.getInt(2));
                Cannon cannon = new Cannon(
                        image,
                        new MountPoint(
                                cursor.getInt(3),
                                cursor.getInt(4),
                                image),
                        new MountPoint(
                                cursor.getInt(5),
                                cursor.getInt(6),
                                image),
                        new Image(
                                cursor.getString(7),
                                cursor.getInt(8),
                                cursor.getInt(9)),
                        cursor.getString(10),
                        cursor.getInt(11));

                to_return.add(cannon);

                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }

        return to_return;
    }

    private void addCannons(JSONObject root_node) throws org.json.JSONException
    {
        JSONArray cannons = root_node.getJSONArray("cannons");
        for (int i = 0; i < cannons.length(); ++i)
        {
            JSONObject current_cannon = cannons.getJSONObject(i);

            Scanner attach_point = new Scanner(current_cannon.getString("attachPoint")).useDelimiter("[^0-9]+");
            Scanner emit_point = new Scanner(current_cannon.getString("emitPoint")).useDelimiter("[^0-9]+");

            ContentValues values = new ContentValues();

            values.put("attach_point_x", attach_point.nextInt());
            values.put("attach_point_y", attach_point.nextInt());
            values.put("emit_point_x", emit_point.nextInt());
            values.put("emit_point_y", emit_point.nextInt());
            values.put("image", current_cannon.getString("image"));
            values.put("image_width", current_cannon.getInt("imageWidth"));
            values.put("image_height", current_cannon.getInt("imageHeight"));
            values.put("attack_image", current_cannon.getString("attackImage"));
            values.put("attack_image_width", current_cannon.getInt("attackImageWidth"));
            values.put("attack_image_height", current_cannon.getInt("attackImageHeight"));
            values.put("attack_sound", current_cannon.getString("attackSound"));
            values.put("damage", current_cannon.getInt("damage"));

            db.insert("Cannons", null, values);
        }
    }

    public List<Engine> getEngines()
    {
        final String SQL =  "select image, image_width, image_height, " +
                                "attach_point_x, attach_point_y, " +
                                "base_speed, " +
                                "base_turn_rate " +
                            "from Engines ";

        List<Engine> to_return = new LinkedList<>();

        Cursor cursor = db.rawQuery(SQL, new String[]{});
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                Image image = new Image(
                        cursor.getString(0),
                        cursor.getInt(1),
                        cursor.getInt(2));
                Engine engine = new Engine(
                        image,
                        new MountPoint(
                                cursor.getInt(3),
                                cursor.getInt(4),
                                image),
                        cursor.getInt(5),
                        cursor.getInt(6));

                to_return.add(engine);

                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }

        return to_return;
    }

    private void addEngines(JSONObject root_node) throws org.json.JSONException
    {
        JSONArray engines = root_node.getJSONArray("engines");

        for (int i = 0; i < engines.length(); ++i)
        {
            JSONObject current_engine = engines.getJSONObject(i);

            ContentValues values = new ContentValues();

            values.put("base_speed", current_engine.getInt("baseSpeed"));
            values.put("base_turn_rate", current_engine.getInt("baseTurnRate"));
            Scanner attach_point = new Scanner(current_engine.getString("attachPoint")).useDelimiter("[^0-9]+");
            values.put("attach_point_x", attach_point.nextInt());
            values.put("attach_point_y", attach_point.nextInt());
            values.put("image", current_engine.getString("image"));
            values.put("image_width", current_engine.getInt("imageWidth"));
            values.put("image_height", current_engine.getInt("imageHeight"));

            db.insert("Engines", null, values);
        }
    }

    public List<ExtraPart> getExtraParts()
    {
        final String SQL =  "select image, image_width, image_height, " +
                                "attach_point_x, attach_point_y " +
                            "from ExtraParts ";

        List<ExtraPart> to_return = new LinkedList<>();

        Cursor cursor = db.rawQuery(SQL, new String[]{});
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                Image image = new Image(
                        cursor.getString(0),
                        cursor.getInt(1),
                        cursor.getInt(2));
                ExtraPart extra_part = new ExtraPart(
                        image,
                        new MountPoint(
                                cursor.getInt(3),
                                cursor.getInt(4),
                                image));

                to_return.add(extra_part);

                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }

        return to_return;
    }

    private void addExtraParts(JSONObject root_node) throws org.json.JSONException
    {
        JSONArray extra_parts = root_node.getJSONArray("extraParts");

        for (int i = 0; i < extra_parts.length(); ++i)
        {
            JSONObject current_extra_part = extra_parts.getJSONObject(i);

            ContentValues values = new ContentValues();

            Scanner attach_point = new Scanner(current_extra_part.getString("attachPoint")).useDelimiter("[^0-9]+");

            values.put("attach_point_x", attach_point.nextInt());
            values.put("attach_point_y", attach_point.nextInt());
            values.put("image", current_extra_part.getString("image"));
            values.put("image_width", current_extra_part.getInt("imageWidth"));
            values.put("image_height", current_extra_part.getInt("imageHeight"));

            db.insert("ExtraParts", null, values);
        }
    }

    private List<BackgroundObject> getLevelObjects(int level_number)
    {
        final String LEVEL_OBJECTS_SQL =
                        "select " +
                            "imagepath, " +
                            "position_x," +
                            "position_y, " +
                            "object_id, " +
                            "scale " +
                        "from " +
                            "BackgroundObjects, " +
                            "LevelBackgroundObjects " +
                        "where " +
                            "BackgroundObjects.id = LevelBackgroundObjects.object_id and " +
                            "level_number = ?";

        List<BackgroundObject> level_objects = new LinkedList<>();

        Cursor cursor = db.rawQuery(LEVEL_OBJECTS_SQL, new String[]{Integer.toString(level_number)});
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                BackgroundObject current_object = new BackgroundObject(
                        new Image(cursor.getString(0), -1, -1),
                        new PointF(
                                cursor.getInt(1),
                                cursor.getInt(2)),
                        cursor.getInt(3),
                        cursor.getFloat(3));

                level_objects.add(current_object);

                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }

        return level_objects;
    }

    private List<Asteroid> getLevelAsteroids(int level_number)
    {
        final String LEVEL_ASTEROIDS_SQL =
                            "select " +
                                    "number, " +
                                    "atype, " +
                                    "image_path, " +
                                    "image_width, " +
                                    "image_height " +
                            "from " +
                                    "LevelAsteroids, " +
                                    "AsteroidTypes "+
                            "where " +
                                    "LevelAsteroids.asteroid_id = AsteroidTypes.id and " +
                                    "level_number = ?";

        List<Asteroid> level_asteroids = new LinkedList<>();

        Cursor cursor = db.rawQuery(LEVEL_ASTEROIDS_SQL, new String[]{Integer.toString(level_number)});

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                int number = cursor.getInt(0);
                String atype = cursor.getString(1);
                String image_path = cursor.getString(2);
                int image_width = cursor.getInt(3);
                int image_height = cursor.getInt(4);

                for (int added_count = 0; added_count < number; added_count++)
                {
                    if (atype.equals("regular"))
                    {
                        level_asteroids.add(new RegularAsteroid(new Image(image_path, image_width, image_height)));
                    }
                    else if (atype.equals("growing"))
                    {
                        level_asteroids.add(new GrowingAsteroid(new Image(image_path, image_width, image_height)));
                    }
                    else if (atype.equals("octeroid"))
                    {
                        level_asteroids.add(new Octeroid(new Image(image_path, image_width, image_height)));
                    }
                    else
                    {
                        //invalid asteroid type.
                        assert false;
                    }
                }

                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }

        return level_asteroids;
    }

    /**
     * this method gets a level from the database based on its ID.
     * @param level_number  the level index
     * @return              the desired level data
     */
    public Level getLevel(int level_number)
    {
        final String LEVEL_SQL =
                            "select " +
                                    "level_number, " +
                                    "title text, " +
                                    "hint, " +
                                    "width, " +
                                    "height, " +
                                    "music " +
                            "from Levels " +
                            "where level_number = ?";

        List<BackgroundObject> level_objects = getLevelObjects(level_number);

        List<Asteroid> level_asteroids = getLevelAsteroids(level_number);

        Cursor cursor = db.rawQuery(LEVEL_SQL, new String[]{Integer.toString(level_number)});

        Level to_return;

        try {
            cursor.moveToFirst();
            if (!cursor.isAfterLast())
            {
                to_return = new Level(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3),
                        cursor.getInt(4),
                        cursor.getString(5),
                        level_objects,
                        level_asteroids);
            }
            else
            {
                to_return = null;
            }
        }
        finally {
            cursor.close();
        }

        return to_return;
    }

    private void addLevels(JSONObject root_node) throws org.json.JSONException
    {
        JSONArray levels = root_node.getJSONArray("levels");

        for (int i = 0; i < levels.length(); ++i)
        {
            JSONObject current_level = levels.getJSONObject(i);

            ContentValues level_values = new ContentValues();

            int level_number = current_level.getInt("number");

            level_values.put("level_number", level_number);
            level_values.put("title", current_level.getString("title"));
            level_values.put("hint", current_level.getString("hint"));
            level_values.put("width", current_level.getInt("width"));
            level_values.put("height", current_level.getInt("height"));
            level_values.put("music", current_level.getString("music"));

            db.insert("Levels", null, level_values);

            JSONArray level_object_json = current_level.getJSONArray("levelObjects");
            for (int object_index = 0; object_index < level_object_json.length(); ++object_index)
            {
                JSONObject current_object = level_object_json.getJSONObject(object_index);
                current_object.getString("position");
                Scanner position = new Scanner(current_object.getString("position")).useDelimiter("[^0-9]+");

                ContentValues object_values = new ContentValues();

                object_values.put("level_number", level_number);
                object_values.put("position_x", position.nextInt());
                object_values.put("position_y", position.nextInt());
                object_values.put("object_id", current_object.getInt("objectId"));
                object_values.put("scale", current_object.getDouble("scale"));

                db.insert("LevelBackgroundObjects", null, object_values);
            }

            JSONArray level_asteroids_json = current_level.getJSONArray("levelAsteroids");
            for (int asteroid_index = 0; asteroid_index < level_asteroids_json.length(); ++asteroid_index)
            {
                JSONObject current_asteroid = level_asteroids_json.getJSONObject(asteroid_index);

                ContentValues asteroid_values = new ContentValues();

                asteroid_values.put("level_number", level_number);
                asteroid_values.put("number", current_asteroid.getInt("number"));
                asteroid_values.put("asteroid_id", current_asteroid.getInt("asteroidId"));

                db.insert("LevelAsteroids", null, asteroid_values);
            }
        }
    }

    public List<MainBody> getMainBodies()
    {
        final String SQL =  "select image, image_width, image_height, " +
                                "cannon_attach_x, cannon_attach_y, " +
                                "engine_attach_x, engine_attach_y, " +
                                "extra_attach_x, extra_attach_y " +
                            "from MainBodies ";

        List<MainBody> to_return = new LinkedList<>();

        Cursor cursor = db.rawQuery(SQL, new String[]{});

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                Image image = new Image(
                        cursor.getString(0),
                        cursor.getInt(1),
                        cursor.getInt(2));
                MainBody main_body = new MainBody(
                        image,
                        new MountPoint(
                                cursor.getInt(3),
                                cursor.getInt(4),
                                image),
                        new MountPoint(
                                cursor.getInt(5),
                                cursor.getInt(6),
                                image),
                        new MountPoint(
                                cursor.getInt(7),
                                cursor.getInt(8),
                                image));

                to_return.add(main_body);

                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }

        return to_return;
    }

    private void addMainBodies(JSONObject root_node) throws org.json.JSONException
    {
        JSONArray main_bodies = root_node.getJSONArray("mainBodies");
        for (int i = 0; i < main_bodies.length(); ++i) {
            JSONObject current_body = main_bodies.getJSONObject(i);

            Scanner cannon_attach = new Scanner(current_body.getString("cannonAttach")).useDelimiter("[^0-9]+");
            Scanner engine_attach = new Scanner(current_body.getString("engineAttach")).useDelimiter("[^0-9]+");
            Scanner extra_attach = new Scanner(current_body.getString("extraAttach")).useDelimiter("[^0-9]+");

            ContentValues values = new ContentValues();

            values.put("image", current_body.getString("image"));
            values.put("image_width", current_body.getInt("imageWidth"));
            values.put("image_height", current_body.getInt("imageHeight"));
            values.put("cannon_attach_x", cannon_attach.nextInt());
            values.put("cannon_attach_y", cannon_attach.nextInt());
            values.put("engine_attach_x", engine_attach.nextInt());
            values.put("engine_attach_y", engine_attach.nextInt());
            values.put("extra_attach_x", extra_attach.nextInt());
            values.put("extra_attach_y", extra_attach.nextInt());

            db.insert("MainBodies", null, values);
        }
    }

    public List<PowerCore> getPowerCores()
    {
        final String SQL =  "select image, " +
                                "cannon_boost, " +
                                "engine_boost " +
                            "from PowerCores ";

        List<PowerCore> to_return = new LinkedList<>();

        Cursor cursor = db.rawQuery(SQL, new String[]{});
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                PowerCore power_core = new PowerCore(
                        new Image(cursor.getString(0), -1, -1),
                        cursor.getInt(1),
                        cursor.getInt(2));

                to_return.add(power_core);

                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }

        return to_return;
    }

    private void addPowerCores(JSONObject root_node) throws org.json.JSONException
    {
        JSONArray power_cores = root_node.getJSONArray("powerCores");

        for (int i = 0; i < power_cores.length(); ++i)
        {
            JSONObject current_power_core = power_cores.getJSONObject(i);

            ContentValues values = new ContentValues();

            values.put("cannon_boost", current_power_core.getInt("cannonBoost"));
            values.put("engine_boost", current_power_core.getInt("engineBoost"));
            values.put("image", current_power_core.getString("image"));

            db.insert("PowerCores", null, values);
        }
    }

    public void importJSON(JSONObject root_object) throws org.json.JSONException
    {
        JSONObject root_node = root_object.getJSONObject("asteroidsGame");
        addBGobjects(root_node);
        addAsteroids(root_node);
        addLevels(root_node);
        addMainBodies(root_node);
        addCannons(root_node);
        addExtraParts(root_node);
        addEngines(root_node);
        addPowerCores(root_node);
    }

    /**
     * this method clears all the information in the table (but doesn't delete the tables).
     */
    public void clearAll()
    {
        DbOpenHelper.dropAndCreate(db);
    }

    /**
     * this is where the SQLiteDatabase is kept.
     */
    private SQLiteDatabase db;
}
