package fitnessgods.udacity.com.fitnessgods.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class WorkoutsContract {

    public static final String CONTENT_AUTHORITY = "udacity.com.fitnessgods";
    public static final String PATH_WORKOUTS= "workouts";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_WORKOUT_EXERCISES= "exercises";

    public static final class WorkoutEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_WORKOUTS)
                .build();

        public static final String TABLE_NAME = "workouts";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_WORKOUT_NAME = "workout_name";

        public static String getSqlSelectWorkouts() {
            return WorkoutsContract.WorkoutEntry.COLUMN_WORKOUT_NAME;
        }

        public static Uri buildMovieUriWithId(String workoutId) {
            return CONTENT_URI.buildUpon()
                    .appendPath(workoutId)
                    .build();
        }

    }
}
