package edu.byu.cs.superasteroids.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

import edu.byu.cs.superasteroids.model.Asteroids.Asteroid;
import edu.byu.cs.superasteroids.model.Asteroids.GrowingAsteroid;
import edu.byu.cs.superasteroids.model.Asteroids.RegularAsteroid;
import edu.byu.cs.superasteroids.model.Image;
import edu.byu.cs.superasteroids.model.Level;
import edu.byu.cs.superasteroids.model.ShipParts.Cannon;
import edu.byu.cs.superasteroids.model.ShipParts.Engine;
import edu.byu.cs.superasteroids.model.ShipParts.ExtraPart;
import edu.byu.cs.superasteroids.model.ShipParts.MainBody;
import edu.byu.cs.superasteroids.model.ShipParts.MountPoint;
import edu.byu.cs.superasteroids.model.ShipParts.PowerCore;

// To the TA that grades this file:
//
// My DAO is implemented a little different then most.
// Normally, the importer parses the JSON, then it converts that file to the respective objects.
// After that, it passes those objects to the dao to be entered into the database.
// Instead of doing it like that, I cut out the middle steps and had my DAO handle the json.
// This eliminates the need for methods on my dao that add by object. Instead, they add by json.
// Since they add by json, they don't return anything, so I couldn't test them directly.
// I did, however, use the json add methods in the setup methods of this dao, so they are tested like that.
// The following tests test all the methods that return anything. To do that, it first adds objects by json (tests the importer).
public class DAOTest extends AndroidTestCase {

    private DAO dao;
    private SQLiteDatabase db;
    private DbOpenHelper open_helper;

    @Override
    public void setUp() throws Exception
    {
        final String json_string =
                "{\n" +
                "\t\"asteroidsGame\": {\n" +
                "\t\t\"objects\": [\n" +
                "\t\t\t\"background image path 1\",\n" +
                "\t\t\t\"background image path 2\"\n" +
                "\t\t],\n" +
                "\t\t\"asteroids\": [\n" +
                "\t\t\t{\n" +
                "\t\t\t\"name\": \"regular\",\n" +
                "\t\t\t\"image\": \"asteroid1 image path\",\n" +
                "\t\t\t\"imageWidth\": 150,\n" +
                "\t\t\t\"imageHeight\": 150,\n" +
                "\t\t\t\"type\": \"regular\"\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\"name\": \"growing\",\n" +
                "\t\t\t\"image\": \"asteroid2 image path\",\n" +
                "\t\t\t\"imageWidth\": 150,\n" +
                "\t\t\t\"imageHeight\": 150,\n" +
                "\t\t\t\"type\": \"growing\"\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\"name\": \"octeroid\",\n" +
                "\t\t\t\"image\": \"asteroid3 image path\",\n" +
                "\t\t\t\"imageWidth\": 150,\n" +
                "\t\t\t\"imageHeight\": 150,\n" +
                "\t\t\t\"type\": \"octeroid\"\n" +
                "\t\t\t}\n" +
                "\t\t],\n" +
                "\t\t\"levels\": [\n" +
                "\t\t\t{\n" +
                "\t\t\t\"number\": 1,\n" +
                "\t\t\t\"title\": \"Level 1\",\n" +
                "\t\t\t\"hint\": \"level hint\",\n" +
                "\t\t\t\"width\": 3000,\n" +
                "\t\t\t\"height\": 3000,\n" +
                "\t\t\t\"music\": \"sound file path\",\n" +
                "\t\t\t\"levelObjects\": [\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\"position\": \"1000,1000\",\n" +
                "\t\t\t\t\"objectId\": 1,\n" +
                "\t\t\t\t\"scale\": 1.0\n" +
                "\t\t\t\t}\n" +
                "\t\t\t],\n" +
                "\t\t\t\"levelAsteroids\": [\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\"number\": 1,\n" +
                "\t\t\t\t\"asteroidId\": 1\n" +
                "\t\t\t\t}\n" +
                "\t\t\t]\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\"number\": 2,\n" +
                "\t\t\t\"title\": \"Level 2\",\n" +
                "\t\t\t\"hint\": \"level hint\",\n" +
                "\t\t\t\"width\": 3000,\n" +
                "\t\t\t\"height\": 3000,\n" +
                "\t\t\t\"music\": \"sound file path\",\n" +
                "\t\t\t\"levelObjects\": [\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\"position\": \"1000,1000\",\n" +
                "\t\t\t\t\"objectId\": 2,\n" +
                "\t\t\t\t\"scale\": 1.0\n" +
                "\t\t\t\t}\n" +
                "\t\t\t],\n" +
                "\t\t\t\"levelAsteroids\": [\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\"number\": 1,\n" +
                "\t\t\t\t\"asteroidId\": 2\n" +
                "\t\t\t\t}\n" +
                "\t\t\t]\n" +
                "\t\t\t}\n" +
                "\t\t],\n" +
                "\t\t\"mainBodies\": [\n" +
                "\t\t\t{\n" +
                "\t\t\t\"cannonAttach\": \"100,100\",\n" +
                "\t\t\t\"engineAttach\": \"100,100\",\n" +
                "\t\t\t\"extraAttach\": \"100,100\",\n" +
                "\t\t\t\"image\": \"image path\",\n" +
                "\t\t\t\"imageWidth\": 150,\n" +
                "\t\t\t\"imageHeight\": 150\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\"cannonAttach\": \"200,200\",\n" +
                "\t\t\t\"engineAttach\": \"200,200\",\n" +
                "\t\t\t\"extraAttach\": \"200,200\",\n" +
                "\t\t\t\"image\": \"image path\",\n" +
                "\t\t\t\"imageWidth\": 150,\n" +
                "\t\t\t\"imageHeight\": 150\n" +
                "\t\t\t}\n" +
                "\t\t],\n" +
                "\t\t\"cannons\": [\n" +
                "\t\t\t{\n" +
                "\t\t\t\"attachPoint\": \"100,100\",\n" +
                "\t\t\t\"emitPoint\": \"100,100\",\n" +
                "\t\t\t\"image\": \"image path\",\n" +
                "\t\t\t\"imageWidth\": 150,\n" +
                "\t\t\t\"imageHeight\": 150,\n" +
                "\t\t\t\"attackImage\": \"attack image path\",\n" +
                "\t\t\t\"attackImageWidth\": 50,\n" +
                "\t\t\t\"attackImageHeight\": 50,\n" +
                "\t\t\t\"attackSound\": \"sound path\",\n" +
                "\t\t\t\"damage\": 1\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\"attachPoint\": \"200,200\",\n" +
                "\t\t\t\"emitPoint\": \"200,200\",\n" +
                "\t\t\t\"image\": \"image path\",\n" +
                "\t\t\t\"imageWidth\": 150,\n" +
                "\t\t\t\"imageHeight\": 150,\n" +
                "\t\t\t\"attackImage\": \"attack image path\",\n" +
                "\t\t\t\"attackImageWidth\": 50,\n" +
                "\t\t\t\"attackImageHeight\": 50,\n" +
                "\t\t\t\"attackSound\": \"sound path\",\n" +
                "\t\t\t\"damage\": 1\n" +
                "\t\t\t}\n" +
                "\t\t],\n" +
                "\t\t\"extraParts\": [\n" +
                "\t\t\t{\n" +
                "\t\t\t\"attachPoint\": \"100,100\",\n" +
                "\t\t\t\"image\": \"image path\",\n" +
                "\t\t\t\"imageWidth\": 150,\n" +
                "\t\t\t\"imageHeight\": 150\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\"attachPoint\": \"200,200\",\n" +
                "\t\t\t\"image\": \"image path\",\n" +
                "\t\t\t\"imageWidth\": 150,\n" +
                "\t\t\t\"imageHeight\": 150\n" +
                "\t\t\t}\n" +
                "\t\t],\n" +
                "\t\t\"engines\": [\n" +
                "\t\t\t{\n" +
                "\t\t\t\"baseSpeed\": 100,\n" +
                "\t\t\t\"baseTurnRate\": 100,\n" +
                "\t\t\t\"attachPoint\": \"100,100\",\n" +
                "\t\t\t\"image\": \"image path\",\n" +
                "\t\t\t\"imageWidth\": 150,\n" +
                "\t\t\t\"imageHeight\": 150\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\"baseSpeed\": 100,\n" +
                "\t\t\t\"baseTurnRate\": 100,\n" +
                "\t\t\t\"attachPoint\": \"200,200\",\n" +
                "\t\t\t\"image\": \"image path\",\n" +
                "\t\t\t\"imageWidth\": 150,\n" +
                "\t\t\t\"imageHeight\": 150\n" +
                "\t\t\t}\n" +
                "\t\t],\n" +
                "\t\t\"powerCores\": [\n" +
                "\t\t\t{\n" +
                "\t\t\t\"cannonBoost\": 100,\n" +
                "\t\t\t\"engineBoost\": 100,\n" +
                "\t\t\t\"image\": \"image1 path\"\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "\t\t\t\"cannonBoost\": 100,\n" +
                "\t\t\t\"engineBoost\": 100,\n" +
                "\t\t\t\"image\": \"image2 path\"\n" +
                "\t\t\t}\n" +
                "\t\t]\n" +
                "\t}\n" +
                "}";

        super.setUp();

        open_helper = new DbOpenHelper(getContext());
        db = open_helper.getWritableDatabase();
        dao = new DAO(db);

        open_helper.dropAndCreate(db);

        dao.importJSON(new JSONObject(json_string));
    }

    @Override
    public void tearDown() throws Exception
    {
        super.tearDown();

        open_helper.dropAndCreate(db);
    }

    public void testGetCannons() throws Exception {
        List<Cannon> cannons = dao.getCannons();
        Iterator<Cannon> it = cannons.iterator();

        Cannon cannon1 = it.next();
        Cannon cannon2 = it.next();

        Image image = new Image("image path", 150, 150);

        assertEquals(cannon1.getImage(), image);
        assertEquals(cannon1.getMountPoint(), new MountPoint(100, 100, image));
        assertEquals(cannon1.getNozzle(), new MountPoint(100, 100, image));
        assertEquals(cannon1.getAttackImage(), new Image("attack image path", 50, 50));
        assertEquals(cannon1.getAttackSound(), "sound path");
        assertEquals(cannon1.getDamage(), 1);

        assertEquals(cannon2.getImage(), image);
        assertEquals(cannon2.getMountPoint(), new MountPoint(200, 200, image));
        assertEquals(cannon2.getNozzle(), new MountPoint(200, 200, image));
        assertEquals(cannon1.getAttackImage(), new Image("attack image path", 50, 50));
        assertEquals(cannon1.getAttackSound(), "sound path");
        assertEquals(cannon1.getDamage(), 1);
    }

    public void testGetEngines() throws Exception {
        List<Engine> engines = dao.getEngines();
        Iterator<Engine> it = engines.iterator();

        Engine engine1 = it.next();
        Engine engine2 = it.next();

        Image image = new Image("image path", 150, 150);

        assertEquals(engine1.getImage(), image);
        assertEquals(engine1.getMountPoint(), new MountPoint(100, 100, image));
        assertEquals(engine1.getBaseSpeed(), 100);
        assertEquals(engine1.getBaseTurnRate(), 100);

        assertEquals(engine2.getImage(), image);
        assertEquals(engine2.getMountPoint(), new MountPoint(200, 200, image));
        assertEquals(engine2.getBaseSpeed(), 100);
        assertEquals(engine2.getBaseTurnRate(), 100);
    }

    public void testGetExtraParts() throws Exception {
        List<ExtraPart> extra_parts = dao.getExtraParts();
        Iterator<ExtraPart> it = extra_parts.iterator();

        ExtraPart part1 = it.next();
        ExtraPart part2 = it.next();

        Image image = new Image("image path", 150, 150);

        assertEquals(part1.getImage(), image);
        assertEquals(part1.getMountPoint(), new MountPoint(100, 100, image));

        assertEquals(part2.getImage(), image);
        assertEquals(part2.getMountPoint(), new MountPoint(200, 200, image));
    }

    public void testGetLevel() throws Exception {
        Level level_1 = dao.getLevel(1);
        Level level_2 = dao.getLevel(2);
        Level level_3 = dao.getLevel(3);

        assertEquals(level_1.getLevelNumber(), 1);
        assertEquals(level_1.getTitle(), "Level 1");
        assertEquals(level_1.getHint(), "level hint");
        assertEquals(level_1.getWidth(), 3000);
        assertEquals(level_1.getHeight(), 3000);
        assertEquals(level_1.getMusic(), "sound file path");
        Asteroid level1_asteroid = level_1.getLevelAsteroids().iterator().next();
        RegularAsteroid reg_asteroid = new RegularAsteroid(new Image("path", 1, 1));
        assertEquals(level1_asteroid.getClass(), reg_asteroid.getClass());

        assertEquals(level_2.getLevelNumber(), 2);
        assertEquals(level_2.getTitle(), "Level 2");
        assertEquals(level_2.getHint(), "level hint");
        assertEquals(level_2.getWidth(), 3000);
        assertEquals(level_2.getHeight(), 3000);
        assertEquals(level_2.getMusic(), "sound file path");
        Asteroid level2_asteroid = level_2.getLevelAsteroids().iterator().next();
        GrowingAsteroid grow_asteroid = new GrowingAsteroid(new Image("path", 1, 1));
        assertEquals(level2_asteroid.getClass(), grow_asteroid.getClass());

        assertEquals(level_3, null);
    }

    public void testGetMainBodies() throws Exception {
        List<MainBody> main_bodies = dao.getMainBodies();
        Iterator<MainBody> it = main_bodies.iterator();

        MainBody body1 = it.next();
        MainBody body2 = it.next();

        Image image = new Image("image path", 150, 150);

        assertEquals(body1.getImage(), image);
        assertEquals(body1.getCannonAttach(), new MountPoint(100, 100, image));
        assertEquals(body1.getEngineAttach(), new MountPoint(100, 100, image));
        assertEquals(body1.getExtraAttach(), new MountPoint(100, 100, image));

        assertEquals(body2.getImage(), image);
        assertEquals(body2.getCannonAttach(), new MountPoint(200, 200, image));
        assertEquals(body2.getEngineAttach(), new MountPoint(200, 200, image));
        assertEquals(body2.getExtraAttach(), new MountPoint(200, 200, image));
    }

    public void testGetPowerCores() throws Exception {
        List<PowerCore> power_cores = dao.getPowerCores();
        Iterator<PowerCore> it = power_cores.iterator();

        PowerCore core1 = it.next();
        PowerCore core2 = it.next();

        Image image1 = new Image("image1 path", -1, -1);
        Image image2 = new Image("image2 path", -1, -1);

        assertEquals(core1.getImage(), image1);
        assertEquals(core1.getCannonBoost(), 100);
        assertEquals(core1.getEngineBoost(), 100);

        assertEquals(core2.getImage(), image2);
        assertEquals(core2.getCannonBoost(), 100);
        assertEquals(core2.getEngineBoost(), 100);
    }
}