package fitnessgods.udacity.com.fitnessgods.data;

public class Exercises {

    private String exersice_name;
    private String exersice_url;
    private String exercise_step;

    public Exercises(String exersice_name, String exersice_url, String exercise_step) {
        this.exersice_name = exersice_name;
        this.exersice_url = exersice_url;
        this.exercise_step = exercise_step;
    }

    public String getExersice_name() {
        return exersice_name;
    }

    public void setExersice_name(String exersice_name) {
        this.exersice_name = exersice_name;
    }

    public String getExersice_url() {
        return exersice_url;
    }

    public void setExersice_url(String exersice_url) {
        this.exersice_url = exersice_url;
    }

    public String getExercise_step() {
        return exercise_step;
    }

    public void setExercise_step(String exercise_step) {
        this.exercise_step = exercise_step;
    }
}
