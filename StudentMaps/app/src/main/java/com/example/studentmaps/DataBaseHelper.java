package com.example.studentmaps;

import android.content.ContextWrapper;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import android.content.Context;

import android.content.ContentValues;

import android.database.Cursor;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;


public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "locations.db";
    public String DATABASE_PATH= "/data/data/com.example.studentmaps/databases/";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "map_data";
    public static final String ID = "ID";
    public static final String NAME = "Name";
    public static final String TYPE = "Type";
    public static final String LATITUDE = "Latitude";
    public static final String LONGITUDE = "Longitude";


    private SQLiteDatabase mydb;
    private final Context context;

    public static final String TAG = DataBaseHelper.class.getSimpleName();
    public static int flag;
    String outFileName = "";

    // creating a constructor for our database handler.
    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        ContextWrapper cw = new ContextWrapper(context);
        Log.e(TAG, "Databasehelper: DATABASE_PATH " + DATABASE_PATH);
        outFileName = DATABASE_PATH + DATABASE_NAME;
        File file = new File(DATABASE_PATH);
        Log.e(TAG, "Databasehelper: " + file.exists());
        if (!file.exists()) {
            file.mkdir();
        }
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     */
    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if (dbExist) {
            //do nothing - database already exist
        } else {
            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }
    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(outFileName, null, SQLiteDatabase.OPEN_READWRITE);
        } catch (SQLiteException e) {
            try {
                copyDataBase();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     */

    private void copyDataBase() throws IOException {

        Log.i("Database",
                "Copying Database");
        byte[] buffer = new byte[1024];
        OutputStream myOutput = null;
        int length;
        // Open your local db as the input stream
        InputStream myInput = null;
        try {
            myInput = context.getAssets().open("databases/" + DATABASE_NAME);
            // transfer bytes from the inputfile to the
            // outputfile
            myOutput = new FileOutputStream(DATABASE_PATH + DATABASE_NAME);
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            myOutput.close();
            myOutput.flush();
            myInput.close();
            Log.i("Database",
                    "Copied");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openDataBase() throws SQLException {
        //Open the database
        String myPath = DATABASE_PATH + DATABASE_NAME;
        mydb = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        Log.e(TAG, "openDataBase: Open " + mydb.isOpen());
    }

    @Override
    public synchronized void close() {
        if (mydb != null)
            mydb.close();
        super.close();
    }

    public void onCreate(SQLiteDatabase arg0) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

    }

    /*@Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }*/

    // we have created a new method for reading all the courses.
    public ArrayList<LocationModel> readTable(String type, float range) {
        // on below line we are creating a
        // database for reading our database.
        SQLiteDatabase db = this.getReadableDatabase();

        // on below line we are creating a cursor with query to read data from database.
        Cursor cursorLocations = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE Type = '"+ type +"'", null);

        // on below line we are creating a new array list.
        ArrayList<LocationModel> locationModelArrayList = new ArrayList<>();

        // moving our cursor to first position.
        if (cursorLocations.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.
                locationModelArrayList.add(new LocationModel(cursorLocations.getString(1),
                        cursorLocations.getString(2),
                        cursorLocations.getDouble(3),
                        cursorLocations.getDouble(4)));
            } while (cursorLocations.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor
        // and returning our array list.
        cursorLocations.close();
        return locationModelArrayList;
    }

}
