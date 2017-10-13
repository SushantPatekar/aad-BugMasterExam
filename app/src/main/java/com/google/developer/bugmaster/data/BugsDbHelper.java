package com.google.developer.bugmaster.data;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.developer.bugmaster.R;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
/**
 * Database helper class to facilitate creating and updating
 * the database from the chosen schema.
 */
public class BugsDbHelper extends SQLiteOpenHelper {
    private static final String TAG = BugsDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "insects.db";
    private static final int DATABASE_VERSION = 1;

    //Used to read data from res/ and assets/
    private Resources mResources;

    public BugsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        mResources = context.getResources();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //TODO: Create and fill the database
        final String SQL_CREATE_INSECT_TABLE = "CREATE TABLE " + BugsContract.BugEntry.TABLE_NAME + " (" +
                BugsContract.BugEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                BugsContract.BugEntry.COLUMN_NAME+ " TEXT NOT NULL, " +
                BugsContract.BugEntry.COLUMN_SCIENTIFICNAME + " TEXT NOT NULL, " +
                BugsContract.BugEntry.COLUMN_CLASSIFICATION+ " TEXT NOT NULL, " +
                BugsContract.BugEntry.COLUMN_IMAGEASSET + " TEXT NOT NULL, " +
                BugsContract.BugEntry.COLUMN_DANGERLEVEL + " INTEGER" +
                "); ";

        db.execSQL(SQL_CREATE_INSECT_TABLE);
        try
        {
            readInsectsFromResources(db);
        } catch(IOException e)
        {
            e.printStackTrace();
        } catch(JSONException e)
        {
            e.printStackTrace();
        }
    }


    private String readJsonDataFromFile() throws IOException
    {
        InputStream inputStream = null;
        StringBuilder builder = new StringBuilder();

        try
        {
            String jsonDataString = null;
            inputStream = mResources.openRawResource(R.raw.insects);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            while((jsonDataString = bufferedReader.readLine()) != null)
            {
                builder.append(jsonDataString);
            }
        }finally
        {
            if(inputStream != null)
            {
                inputStream.close();
            }
        }
        Log.d("Suck", builder.toString());

        return new String(builder);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO: Handle database version upgrades
        db.execSQL("DROP TABLE IF EXISTS " + BugsContract.BugEntry.TABLE_NAME);
        onCreate(db);
    }

    /**
     * Streams the JSON data from insect.json, parses it, and inserts it into the
     * provided {@link SQLiteDatabase}.
     *
     * @param db Database where objects should be inserted.
     * @throws IOException
     * @throws JSONException
     */
    private void readInsectsFromResources(SQLiteDatabase db) throws IOException, JSONException {
        try {

            final String NAME = "friendlyName";
            final String SCIENTIFIC_NAME = "scientificName";
            final String CLASSIFICATION = "classification";
            final String IMAGE_ASSET = "imageAsset";
            final String DANGER_LEVEL = "dangerLevel";

            //Parse resource into key/values

            final String rawJson = readJsonDataFromFile();
           // JSONArray j_array = new JSONArray(rawJson);
            JSONObject rawJSONObject = new JSONObject(rawJson);
            JSONArray j_array = rawJSONObject.getJSONArray("insects");

            for (int i = 0; i < j_array.length(); ++i) {
                String friendlyName;
                String scientificName;
                String classification;
                String image;
                int dangerLevel;

                JSONObject jsonObj = j_array.getJSONObject(i);

                friendlyName = jsonObj.getString(NAME);
                scientificName = jsonObj.getString(SCIENTIFIC_NAME);
                classification = jsonObj.getString(CLASSIFICATION);
                image = jsonObj.getString(IMAGE_ASSET);
                dangerLevel = jsonObj.getInt(DANGER_LEVEL);

                ContentValues contentValues = new ContentValues();

                contentValues.put(BugsContract.BugEntry.COLUMN_NAME, friendlyName);
                contentValues.put(BugsContract.BugEntry.COLUMN_SCIENTIFICNAME, scientificName);
                contentValues.put(BugsContract.BugEntry.COLUMN_CLASSIFICATION, classification);
                contentValues.put(BugsContract.BugEntry.COLUMN_IMAGEASSET, image);
                contentValues.put(BugsContract.BugEntry.COLUMN_DANGERLEVEL, dangerLevel);

                db.insert(BugsContract.BugEntry.TABLE_NAME, null, contentValues);

                Log.d(TAG, "Inserted successfully" + contentValues);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }
}
