package edu.byu.cs.superasteroids.importer;

import android.content.Context; //Needed?

import org.json.JSONObject;

import java.io.InputStreamReader;
import java.io.IOException;

import edu.byu.cs.superasteroids.database.Database;

public class GameDataImporter implements IGameDataImporter {

    Database db;

    Context context;

    public GameDataImporter(Context _context)
    {
        db = new Database(_context);
        context = _context;
    }

    @Override
    public boolean importData(InputStreamReader dataInputReader)
    {
        //clear the current database
        db.dao.clearAll();

        //change inputstreamreader to json object by first changing to string
        JSONObject rootObj;
        try {
            rootObj = new JSONObject(makeString(dataInputReader));
        } catch(java.io.IOException e) {
            return false;
        } catch(org.json.JSONException e) {
            return false;
        }

        //load objects
        try {
            db.dao.importJSON(rootObj);
        } catch (org.json.JSONException e) {
            return false;
        }

        return true;
    }

    private static String makeString(InputStreamReader reader) throws IOException {

        StringBuilder sb = new StringBuilder();
        char[] buf = new char[512];

        int n = 0;
        while ((n = reader.read(buf)) > 0) {
            sb.append(buf, 0, n);
        }

        return sb.toString();
    }
}
