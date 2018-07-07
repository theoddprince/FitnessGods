package fitnessgods.udacity.com.fitnessgods.utilities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import fitnessgods.udacity.com.fitnessgods.data.WorkoutsContract;
import fitnessgods.udacity.com.fitnessgods.sync.WorkoutsSyncIntentService;

public class WorkoutsSyncUtils {

    private static boolean sInitialized;

    public static void startImmediateSync(@NonNull final Context context) {
        Intent intentToSyncImmediately = new Intent(context, WorkoutsSyncIntentService.class);
        context.startService(intentToSyncImmediately);
    }

    synchronized public static void initialize(@NonNull final Context context) {

        /*
         * Only perform initialization once per app lifetime. If initialization has already been
         * performed, we have nothing to do in this method.
         */
        if (sInitialized) return;

        sInitialized = true;

        Thread checkForEmpty = new Thread(new Runnable() {
            @Override
            public void run() {

                /* URI for every row of workouts data in our workouts table*/
                Uri QueryUri = WorkoutsContract.WorkoutEntry.CONTENT_URI;
                String[] projectionColumns = {WorkoutsContract.WorkoutEntry._ID};

                /* Here, we perform the query to check to see if we have any workout data */
                Cursor cursor = context.getContentResolver().query(
                        QueryUri,
                        projectionColumns,
                        null,
                        null,
                        null);

                if (null == cursor || cursor.getCount() == 0) {
                    startImmediateSync(context);
                }

                /* Make sure to close the Cursor to avoid memory leaks! */
                if(cursor != null)
                    cursor.close();
            }
        });

        /* Finally, once the thread is prepared, fire it off to perform our checks. */
        checkForEmpty.start();
    }


}
