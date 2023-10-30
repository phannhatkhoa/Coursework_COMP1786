package com.example.courseworkcomp1786;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "hike";
    // A set of constants to store the column and table names
    private static final String TABLE_NAME = "hike";
    public static final String ID_COLUMN = "hike_id";
    public static final String NAME_COLUMN = "name";

    public static final String LOCATION_COLUMN = "location";
    public static final String DOB_COLUMN = "dob";

    public static final String PARKING_COLUMN = "parking";

    public static final String LENGTH_COLUMN = "length";

    public static final String DIFFICULTY_COLUMN = "difficulty";
    public static final String DESCRIPTION_COLUMN = "description";


    private SQLiteDatabase database;

    private static final String TABLE_CREATE = String.format(
            "CREATE TABLE %s (" +
                    "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "%s TEXT, " +
                    "%s TEXT, " +
                    "%s TEXT, " +
                    "%s TEXT, " +
                    "%s TEXT, " +
                    "%s TEXT)",
            TABLE_NAME, ID_COLUMN, NAME_COLUMN, LOCATION_COLUMN, DOB_COLUMN, PARKING_COLUMN, LENGTH_COLUMN, DIFFICULTY_COLUMN
    );

    // The constructor makes a call to the method in the super class, passing the database name
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        database = getWritableDatabase();
    }

    // Overriding the onCreate() method which generates the database
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    // This method upgrades the database if the version number changes
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        Log.v(this.getClass().getName(), TABLE_NAME +
                "database upgrade to version" + newVersion + " - old data lost"
        );
        onCreate(db);
    }

    // Returns the automatically generated primary key
    public long insertDetails(String name, String location, String dob, String parking, String length, String difficulty) {
        ContentValues rowValues = new ContentValues();
        rowValues.put(NAME_COLUMN, name);
        rowValues.put(LOCATION_COLUMN, location);
        rowValues.put(DOB_COLUMN, dob);
        rowValues.put(PARKING_COLUMN, parking);
        rowValues.put(LENGTH_COLUMN, length);
        rowValues.put(DIFFICULTY_COLUMN, difficulty);

        return database.insert(TABLE_NAME, null, rowValues);
    }


    public String getDetails() {
        // The query will be:
        // SELECT person_id, name, dob, email
        // FROM persons
        // ORDER BY name
        Cursor results = database.query(TABLE_NAME,
                // Defines the query to execute
                new String[]{ID_COLUMN, NAME_COLUMN,LOCATION_COLUMN, DOB_COLUMN,PARKING_COLUMN,LENGTH_COLUMN,DIFFICULTY_COLUMN },
                null, null, null, null, NAME_COLUMN
        );
        String resultText = "";

        // Moves to the first position of the result set
        results.moveToFirst();

        // Checks whether there are more rows in the result set
        while (!results.isAfterLast()) {

            // Extracts the values from the row
            int id = results.getInt(0);
            String name = results.getString(1);
            String location = results.getString(2);
            String dob = results.getString(3);
            String parking = results.getString(4);
            String length = results.getString(5);
            String difficulty = results.getString(6);



            // Concatenates the text values
            resultText += id + " " + name + " " + location + " " + dob + " " + parking + " " + length + " " + difficulty +  "\n";

            // Moves to the next row in the result set
            results.moveToNext();
        }

        // Returns a long string of all results
        return resultText;
    }
}
