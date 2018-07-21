package fitnessgods.udacity.com.fitnessgods.Fragments;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import fitnessgods.udacity.com.fitnessgods.CustomExercisesActivity;
import fitnessgods.udacity.com.fitnessgods.ExercisesActivity;
import fitnessgods.udacity.com.fitnessgods.NewWorkoutsAdapter;
import fitnessgods.udacity.com.fitnessgods.R;
import fitnessgods.udacity.com.fitnessgods.WorkoutsAdapter;
import fitnessgods.udacity.com.fitnessgods.data.WorkoutsContract;

public class CustomListFragment  extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>,
        NewWorkoutsAdapter.NewWorkoutAdapterOnClickHandler{
    private String m_Text = "";
    Button newWorkout;
    private static final int ID_NEW_WORKOUT_LOADER = 101;
    private RecyclerView mRecyclerView;
    private NewWorkoutsAdapter tAdapter;
    private int mPosition = RecyclerView.NO_POSITION;

    public static final String[] MAIN_NEW_WORKOUT_PROJECTION = {
            WorkoutsContract.NewWorkoutEntry.COLUMN_NEW_WORKOUT_NAME
    };

    public CustomListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.customlist_fragment, container, false);
        mRecyclerView = rootView.findViewById(R.id.recycler_new_workouts);
        newWorkout = rootView.findViewById(R.id.new_workout_btn);

        LinearLayoutManager layoutManagerNewWorkouts =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManagerNewWorkouts);
        mRecyclerView.setHasFixedSize(true);
        tAdapter = new NewWorkoutsAdapter(getContext() , this);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setAdapter(tAdapter);

        newWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Workout Custom Name ");
                final EditText input = new EditText(getContext());

                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();

                        ContentResolver newWorkoutsContentResolver = getContext().getContentResolver();
                        ContentValues[] newWorkoutContentValues =new ContentValues[1];
                        ContentValues newWorkoutValues = new ContentValues();
                        newWorkoutValues.put(WorkoutsContract.NewWorkoutEntry.COLUMN_NEW_WORKOUT_NAME,m_Text);
                        newWorkoutContentValues[0] = newWorkoutValues;

                        newWorkoutsContentResolver.bulkInsert(
                                WorkoutsContract.NewWorkoutEntry.CONTENT_URI,
                                newWorkoutContentValues);

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        LoaderManager loaderManager = getActivity().getSupportLoaderManager();
        loaderManager.initLoader(ID_NEW_WORKOUT_LOADER, null, this);

        // Inflate the layout for this fragment
        return rootView;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, @Nullable Bundle args) {
        switch (loaderId) {
            case ID_NEW_WORKOUT_LOADER:

                Uri workoutQueryUri = WorkoutsContract.NewWorkoutEntry.CONTENT_URI;
                String selection = WorkoutsContract.NewWorkoutEntry.getSqlSelectNewWorkouts();
                return new CursorLoader(getContext(),
                        workoutQueryUri,
                        MAIN_NEW_WORKOUT_PROJECTION,
                        selection,
                        null,
                        null);
            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        tAdapter.swapCursor(data);
        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
        mRecyclerView.smoothScrollToPosition(mPosition);

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        tAdapter.swapCursor(null);
    }

    @Override
    public void onClick(String newWorkoutName) {
        Intent exerciseDetailIntent = new Intent(getActivity(), CustomExercisesActivity.class);
        Uri uriForWorkoutClicked = WorkoutsContract.CustomExercisesEntry.buildExerciseUriWithCustomName(newWorkoutName);
        exerciseDetailIntent.setData(uriForWorkoutClicked);
        startActivity(exerciseDetailIntent);
    }
}
