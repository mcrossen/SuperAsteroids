package edu.byu.cs.superasteroids.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONException;

/**
 * This class is used to manage the database.
 */
public class Database {

    /**
     * This is where the open helper is kept.
     */
    private DbOpenHelper db_open_helper;

    /**
     * this is where the database is kept in memory.
     */
    private SQLiteDatabase database;

    /**
     * The base context
     */
    private Context base_context;

    /**
     * A copy of the DAO for public database access.
     */
    public DAO dao;

    /**
     * the constructor for the database class
     * @param baseContext   the only needed information is the base context
     */
    public Database(Context baseContext)
    {
        base_context = baseContext;
        db_open_helper = new DbOpenHelper(base_context);
        database = db_open_helper.getWritableDatabase();
        dao = new DAO(database);
    }
}
