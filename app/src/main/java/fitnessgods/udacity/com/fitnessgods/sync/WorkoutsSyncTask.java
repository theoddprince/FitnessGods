package fitnessgods.udacity.com.fitnessgods.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import fitnessgods.udacity.com.fitnessgods.data.Exercises;
import fitnessgods.udacity.com.fitnessgods.data.Workouts;
import fitnessgods.udacity.com.fitnessgods.data.WorkoutsContract;
import fitnessgods.udacity.com.fitnessgods.utilities.OpenWorkoutsJsonUtils;

public class WorkoutsSyncTask {

    synchronized public static void syncWorkouts(final Context context) {

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        firestore.setFirestoreSettings(settings);

        firestore.collection("Workouts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    public static final String TAG = "WorkoutsSyncTask" ;
                    List<Workouts> workoutsList = new ArrayList<>();

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {


                            for(DocumentSnapshot doc : task.getResult()){
                                // I still need to fix this , its partially functional
                                ArrayList<Exercises> exercisesList = new ArrayList<>();


                                String workout_name = doc.getString("workout_name");
                                String poster_url = doc.getString("poster_url");
                                Object exercise_object = doc.get("exercises");
                                Map exercises = (HashMap) exercise_object;

                                Iterator it = exercises.entrySet().iterator();
                                while (it.hasNext()) {
                                    Map.Entry entry = (Map.Entry)it.next(); //current entry in a loop
                                    Object exercise_entry = entry.getValue();
                                    Map exercise = (HashMap) exercise_entry;
                                    Exercises newExercise = new Exercises(exercise.get("exercise_name").toString(),
                                            exercise.get("exercise_url").toString(),
                                            exercise.get("exercise_steps").toString(),
                                            exercise.get("exercise_img_url").toString(),
                                            workout_name);
                                    exercisesList.add(newExercise);
                                }
                                Workouts newWorkout = new Workouts(workout_name,poster_url,exercisesList);
                                workoutsList.add(newWorkout);

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                        ContentValues[] workoutsValues = OpenWorkoutsJsonUtils.getWorkoutsContentValues(context,workoutsList);
                        ContentValues[] exercisesValues = OpenWorkoutsJsonUtils.getExercisesContentValues(context,workoutsList);

                        if (workoutsValues != null && workoutsValues.length != 0) {
                            ContentResolver workoutsContentResolver = context.getContentResolver();

                            workoutsContentResolver.delete(WorkoutsContract.WorkoutEntry.CONTENT_URI,
                                    null,
                                    null);

                            workoutsContentResolver.bulkInsert(
                                    WorkoutsContract.WorkoutEntry.CONTENT_URI,
                                    workoutsValues);

                            workoutsContentResolver.delete(WorkoutsContract.ExercisetEntry.CONTENT_URI,
                                    null,
                                    null);

                            workoutsContentResolver.bulkInsert(
                                    WorkoutsContract.ExercisetEntry.CONTENT_URI,
                                    exercisesValues);
                        }

                    }
                });
    }
}
