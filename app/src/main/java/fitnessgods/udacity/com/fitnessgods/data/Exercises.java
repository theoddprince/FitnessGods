package fitnessgods.udacity.com.fitnessgods.data;

public class Exercises {

    private String exercise_name;
    private String exercise_url;
    private String exercise_step;
    private String exercise_img_url;

    public Exercises(String exercise_name, String exercise_url, String exercise_step,String exercise_img_url) {
        this.exercise_name = exercise_name;
        this.exercise_url = exercise_url;
        this.exercise_step = exercise_step;
        this.exercise_img_url = exercise_img_url;
    }

    public String getExercise_img_url() {
        return exercise_img_url;
    }

    public void setExercise_img_url(String exercise_img_url) {
        this.exercise_img_url = exercise_img_url;
    }

    public String getExersice_name() {
        return exercise_name;
    }

    public void setExersice_name(String exersice_name) {
        this.exercise_name = exersice_name;
    }

    public String getExersice_url() {
        return exercise_url;
    }

    public void setExersice_url(String exersice_url) {
        this.exercise_url = exersice_url;
    }

    public String getExercise_step() {
        return exercise_step;
    }

    public void setExercise_step(String exercise_step) {
        this.exercise_step = exercise_step;
    }
}
