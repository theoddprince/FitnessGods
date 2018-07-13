package fitnessgods.udacity.com.fitnessgods.data;

import java.util.ArrayList;

public class Workouts {

    private String workout_name;
    private String poster_url;
    private ArrayList<Exercises> Exercises ;

    public Workouts(String workout_name,String poster_url, ArrayList<Exercises> exercises) {
        this.workout_name = workout_name;
        this.poster_url = poster_url;
        Exercises = exercises;
    }

    public String getPoster_url() {
        return poster_url;
    }

    public void setPoster_url(String poster_url) {
        this.poster_url = poster_url;
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
