package fitnessgods.udacity.com.fitnessgods.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.events.Event;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import fitnessgods.udacity.com.fitnessgods.data.Exercises;
import fitnessgods.udacity.com.fitnessgods.data.Workouts;

public class WorkoutsSyncTask {

    synchronized public static void syncWorkouts(Context context) {

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        firestore.setFirestoreSettings(settings);

        //Query jsonQuery = firestore.collection("Workouts");

        firestore.collection("Workouts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    public static final String TAG = "WorkoutsSyncTask" ;

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Workouts> workoutsList = new ArrayList<>();

                            for(DocumentSnapshot doc : task.getResult()){
                                // I still need to fix this , its partially functional
                                ArrayList<Exercises> exercisesList = new ArrayList<>();
                                Exercises newExercise = new Exercises("","","");
                                exercisesList.add(newExercise);
                                String workout_name = doc.getString("workout_name");
                                Object exercise_object = doc.get("exercises");
                                Workouts newWorkout = new Workouts(workout_name,exercisesList);
                                workoutsList.add(newWorkout);

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


       /* try {
            ContentValues[] workoutsValues = OpenWorkoutsJsonUtils
                    .getWorkoutsContentValuesFromJson(context, jsonQuery.toString());

            if (workoutsValues != null && workoutsValues.length != 0) {
                ContentResolver workoutsContentResolver = context.getContentResolver();

                workoutsContentResolver.delete(WorkoutsContract.WorkoutEntry.CONTENT_URI,
                        null,
                        null);

                workoutsContentResolver.bulkInsert(
                        WorkoutsContract.WorkoutEntry.CONTENT_URI,
                        workoutsValues);
            }


         } catch (JSONException e) {
            e.printStackTrace();
        }*/
    }
}
