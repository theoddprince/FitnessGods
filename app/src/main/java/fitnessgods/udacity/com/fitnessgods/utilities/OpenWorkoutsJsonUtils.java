package fitnessgods.udacity.com.fitnessgods.utilities;

import android.content.ContentValues;
import android.content.Context;
import org.json.JSONException;

import java.util.List;

import fitnessgods.udacity.com.fitnessgods.data.Workouts;
import fitnessgods.udacity.com.fitnessgods.data.WorkoutsContract;

public class OpenWorkoutsJsonUtils {

    public static ContentValues[] getWorkoutsContentValues(Context context, List<Workouts> workouts)
    {
        ContentValues[] workoutsContentValues =new ContentValues[workouts.size()];

        for(int i=0;i<workouts.size();i++)
        {
            String workout_name = workouts.get(i).getWorkout_name();
            String poster_url = workouts.get(i).getPoster_url();

            ContentValues workoutsValues = new ContentValues();
            workoutsValues.put(WorkoutsContract.WorkoutEntry.COLUMN_WORKOUT_NAME,workout_name);
            workoutsValues.put(WorkoutsContract.WorkoutEntry.COLUMN_WORKOUT_POSTER_URL,poster_url);

            workoutsContentValues[i] = workoutsValues ;
        }

        return workoutsContentValues;
    }
}
