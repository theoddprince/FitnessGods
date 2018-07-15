package fitnessgods.udacity.com.fitnessgods;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import fitnessgods.udacity.com.fitnessgods.Fragments.DetailedExerciseFragment;
import fitnessgods.udacity.com.fitnessgods.data.Exercises;

public class DetailedExerciseActivity extends AppCompatActivity {

    private ActionBar toolbar;
    Exercises exercise;
    Bundle bundle = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_exercise);

        toolbar = getSupportActionBar();
        toolbar.setDisplayHomeAsUpEnabled(true);

        FragmentManager fragmentManager = getSupportFragmentManager();

        try
        {
            bundle = getIntent().getExtras();
            exercise =  (Exercises) bundle.getSerializable("Exercise");
        }
        catch(Throwable e)
        {e.printStackTrace();}


        if(savedInstanceState == null) {
            DetailedExerciseFragment exerciseNewFragment = new DetailedExerciseFragment();
            exerciseNewFragment.setOneExercise(exercise);

            fragmentManager.beginTransaction()
                    .add(R.id.player_container,exerciseNewFragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        super.onSaveInstanceState(currentState);
        //Must save the step data in case we rotate the screen
        currentState.putSerializable("Exercise" , exercise);

    }
}
