package fitnessgods.udacity.com.fitnessgods.data;

import java.util.ArrayList;

public class Workouts {

    private String workout_name;
    private ArrayList<Exercises> Exercises ;

    public Workouts(String workout_name, ArrayList<Exercises> exercises) {
        this.workout_name = workout_name;
        Exercises = exercises;
    }

    public String getWorkout_name() {
        return workout_name;
    }

    public void setWorkout_name(String workout_name) {
        this.workout_name = workout_name;
    }

    public ArrayList getExercises() {
        return Exercises;
    }

    public void setExercises(ArrayList<Exercises> exercises) {
        Exercises = exercises;
    }
}
