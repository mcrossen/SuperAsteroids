package edu.byu.cs.superasteroids.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * The DbOpenHelper class extends a java class and is called when the database is opened.
 */
public class DbOpenHelper extends SQLiteOpenHelper {


    /**
     * The name of the database file.
     */
    private static final String DB_NAME = "asteroidsdata.sqlite";

    /**
     * The database version (1st version).
     */
    private static final int DB_VERSION = 1;

    /**
     * the constructor for the open helper.
     * @param context   only the context needs to be passed in.
     */
    public DbOpenHelper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * This method is called when the database is created.
     * @param db    the sqlitedatabase that is being created.
     */
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        createTables(db);
    }

    public static void dropAndCreate(SQLiteDatabase db)
    {
        dropTables(db);
        createTables(db);
    }

    public static void dropTables(SQLiteDatabase db)
    {
        db.execSQL(DROP_TABLE_BGOBJECT_SQL);
        db.execSQL(DROP_TABLE_ASTEROIDS_SQL);
        db.execSQL(DROP_TABLE_LEVELS_SQL);
        db.execSQL(DROP_TABLE_LEVEL_OBJS_SQL);
        db.execSQL(DROP_TABLE_LEVEL_ASTEROIDS_SQL);
        db.execSQL(DROP_TABLE_MAINBODIES_SQL);
        db.execSQL(DROP_TABLE_CANNONS_SQL);
        db.execSQL(DROP_TABLE_EXTRAPARTS_SQL);
        db.execSQL(DROP_TABLE_ENGINES_SQL);
        db.execSQL(DROP_TABLE_POWERCORES_SQL);
    }

    public static void createTables(SQLiteDatabase db)
    {
        db.execSQL(CREATE_TABLE_BGOBJECTS_SQL);
        db.execSQL(CREATE_TABLE_ASTEROIDS_SQL);
        db.execSQL(CREATE_TABLE_LEVELS_SQL);
        db.execSQL(CREATE_TABLE_LEVEL_OBJS_SQL);
        db.execSQL(CREATE_TABLE_LEVEL_ASTEROIDS_SQL);
        db.execSQL(CREATE_TABLE_MAINBODIES_SQL);
        db.execSQL(CREATE_TABLE_CANNONS_SQL);
        db.execSQL(CREATE_TABLE_EXTRAPARTS_SQL);
        db.execSQL(CREATE_TABLE_ENGINES_SQL);
        db.execSQL(CREATE_TABLE_POWERCORES_SQL);
    }
    /**
     * This method is called when the database is upgraded. This method probably won't be used for our purposes.
     * It still needs to be overriden because it is abstract in the parent class.
     * @param db            The database.
     * @param old_version   the obsolete database version.
     * @param new_version   the replacement database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int old_version, int new_version)
    {
    }

    private static final String DROP_TABLE_BGOBJECT_SQL = "DROP TABLE IF EXISTS BackgroundObjects;";
    private static final String DROP_TABLE_ASTEROIDS_SQL = "DROP TABLE IF EXISTS AsteroidTypes;";
    private static final String DROP_TABLE_LEVELS_SQL = "DROP TABLE IF EXISTS Levels;";
    private static final String DROP_TABLE_LEVEL_OBJS_SQL = "DROP TABLE IF EXISTS LevelBackgroundObjects;";
    private static final String DROP_TABLE_LEVEL_ASTEROIDS_SQL = "DROP TABLE IF EXISTS LevelAsteroids;";
    private static final String DROP_TABLE_MAINBODIES_SQL = "DROP TABLE IF EXISTS MainBodies;";
    private static final String DROP_TABLE_CANNONS_SQL = "DROP TABLE IF EXISTS Cannons;";
    private static final String DROP_TABLE_EXTRAPARTS_SQL = "DROP TABLE IF EXISTS ExtraParts;";
    private static final String DROP_TABLE_ENGINES_SQL = "DROP TABLE IF EXISTS Engines;";
    private static final String DROP_TABLE_POWERCORES_SQL = "DROP TABLE IF EXISTS PowerCores;";

    private static final String CREATE_TABLE_BGOBJECTS_SQL =
                    "CREATE TABLE BackgroundObjects "+
                    "(" +
                    "   id integer not null primary key autoincrement," +
                    "   imagepath text not null" +
                    ");";
    private static final String CREATE_TABLE_ASTEROIDS_SQL =
                    "CREATE TABLE AsteroidTypes " +
                    "(" +
                    "   id integer not null primary key autoincrement," +
                    "   name text not null," +
                    "   image_path text not null," +
                    "   image_width integer not null," +
                    "   image_height integer not null," +
                    "   atype text not null" +
                    ");";
    private static final String CREATE_TABLE_LEVELS_SQL =
                    "CREATE TABLE Levels " +
                    "(" +
                    "   level_number integer not null primary key," +
                    "   title text not null," +
                    "   hint text," +
                    "   width integer not null," +
                    "   height integer not null," +
                    "   music text" +
                    ");";
    private static final String CREATE_TABLE_LEVEL_OBJS_SQL =
                    "CREATE TABLE LevelBackgroundObjects " +
                    "(" +
                    "   level_number integer not null," +
                    "   position_x integer not null," +
                    "   position_y integer not null," +
                    "   object_id integer not null," +
                    "   scale real" +
                    ");";
    private static final String CREATE_TABLE_LEVEL_ASTEROIDS_SQL =
                    "CREATE TABLE LevelAsteroids " +
                    "(" +
                    "   level_number integer not null," +
                    "   number integer not null," +
                    "   asteroid_id integer not null" +
                    ");";
    private static final String CREATE_TABLE_MAINBODIES_SQL =
                    "CREATE TABLE MainBodies " +
                    "(" +
                    "   id integer not null primary key autoincrement," +
                    "   image text not null," +
                    "   image_width integer not null," +
                    "   image_height integer not null," +
                    "   cannon_attach_x integer not null," +
                    "   cannon_attach_y integer not null," +
                    "   engine_attach_x integer not null," +
                    "   engine_attach_y integer not null," +
                    "   extra_attach_x integer not null," +
                    "   extra_attach_y integer not null" +
                    ");";
    private static final String CREATE_TABLE_CANNONS_SQL =
                    "CREATE TABLE Cannons " +
                    "(" +
                    "   id integer not null primary key autoincrement," +
                    "   image text not null," +
                    "   image_width integer not null," +
                    "   image_height integer not null," +
                    "   attach_point_x integer not null," +
                    "   attach_point_y integer not null," +
                    "   emit_point_x integer not null," +
                    "   emit_point_y integer not null," +
                    "   attack_image text not null," +
                    "   attack_image_width integer not null," +
                    "   attack_image_height integer not null," +
                    "   attack_sound text not null," +
                    "   damage integer not null" +
                    ");";
    private static final String CREATE_TABLE_EXTRAPARTS_SQL =
                    "CREATE TABLE ExtraParts " +
                    "(" +
                    "   id integer not null primary key autoincrement," +
                    "   image text not null," +
                    "   image_width integer not null," +
                    "   image_height integer not null," +
                    "   attach_point_x integer not null," +
                    "   attach_point_y integer not null" +
                    ");";
    private static final String CREATE_TABLE_ENGINES_SQL =
                    "CREATE TABLE Engines " +
                    "(" +
                    "   id integer not null primary key autoincrement," +
                    "   image text not null," +
                    "   image_width integer not null," +
                    "   image_height integer not null," +
                    "   attach_point_x integer not null," +
                    "   attach_point_y integer not null," +
                    "   base_speed integer not null," +
                    "   base_turn_rate integer not null" +
                    ");";
    private static final String CREATE_TABLE_POWERCORES_SQL =
                    "CREATE TABLE PowerCores " +
                    "(" +
                    "   id integer not null primary key autoincrement," +
                    "   image text not null," +
                    "   cannon_boost integer not null," +
                    "   engine_boost integer not null" +
                    "); ";
}
