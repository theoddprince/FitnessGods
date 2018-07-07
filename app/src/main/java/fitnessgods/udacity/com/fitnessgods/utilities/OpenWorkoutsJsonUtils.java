package fitnessgods.udacity.com.fitnessgods.utilities;

import android.content.ContentValues;
import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fitnessgods.udacity.com.fitnessgods.data.WorkoutsContract;

public class OpenWorkoutsJsonUtils {

    private static final String WORKOUT_NAME = "workout_name";
    public static ContentValues[] getWorkoutsContentValuesFromJson(Context context, String workoutsJsonStr)
            throws JSONException {
        ContentValues[] workoutsContentValues =null;
      /*  JSONObject workoutsJson = new JSONObject(workoutsJsonStr);
        JSONArray jsonWorkoutsArray = workoutsJson.getJSONArray("Workouts");

        ContentValues[] workoutsContentValues = new ContentValues[jsonWorkoutsArray.length()];

        for (int i = 0; i < jsonWorkoutsArray.length(); i++) {
            String workout_name = jsonWorkoutsArray.getJSONObject(i).optString(WORKOUT_NAME) ;

            ContentValues workoutsValues = new ContentValues();
            workoutsValues.put(WorkoutsContract.WorkoutEntry.COLUMN_WORKOUT_NAME,workout_name);


            workoutsContentValues[i] = workoutsValues ;
        }*/

        return workoutsContentValues;
    }
}
