package fitnessgods.udacity.com.fitnessgods.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WorkoutsDBHelper extends SQLiteOpenHelper {

    // The name of the database
    private static final String DATABASE_NAME = "workouts.db";

    // If you change the database schema, you must increment the database version
    private static final int VERSION = 8;

    // Constructor
    WorkoutsDBHelper(Context context) {super(context, DATABASE_NAME, null, VERSION); }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String CREATE_TABLE = "CREATE TABLE "  + WorkoutsContract.WorkoutEntry.TABLE_NAME + " (" +
                WorkoutsContract.WorkoutEntry._ID                    + " INTEGER PRIMARY KEY, " +
                WorkoutsContract.WorkoutEntry.COLUMN_WORKOUT_NAME    + " TEXT NOT NULL, "+
                WorkoutsContract.WorkoutEntry.COLUMN_WORKOUT_POSTER_URL + " TEXT);";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + WorkoutsContract.WorkoutEntry.TABLE_NAME);
        onCreate(db);
    }
}
