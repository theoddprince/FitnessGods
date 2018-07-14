package fitnessgods.udacity.com.fitnessgods.Fragments;

import android.content.Context;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import fitnessgods.udacity.com.fitnessgods.ExercisesActivity;
import fitnessgods.udacity.com.fitnessgods.R;
import fitnessgods.udacity.com.fitnessgods.WorkoutsAdapter;
import fitnessgods.udacity.com.fitnessgods.data.WorkoutsContract;
import fitnessgods.udacity.com.fitnessgods.utilities.WorkoutsSyncUtils;

public class WorkoutsFragment  extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>,
        WorkoutsAdapter.WorkoutAdapterOnClickHandler{

    private RecyclerView mRecyclerView;
    private WorkoutsAdapter tAdapter;
    private static final int ID_WORKOUT_LOADER = 99;
    private Context mContext ;
    private ProgressBar mLoadingIndicator;
    private int mPosition = RecyclerView.NO_POSITION;

    public static final String[] MAIN_WORKOUT_PROJECTION = {
            WorkoutsContract.WorkoutEntry.COLUMN_WORKOUT_NAME,
            WorkoutsContract.WorkoutEntry.COLUMN_WORKOUT_POSTER_URL,
            WorkoutsContract.WorkoutEntry.COLUMN_ID
    };

    public WorkoutsFragment() {
        super();
    }

    public WorkoutsFragment(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflator =  inflater.inflate(R.layout.workouts_fragment, container, false);
        mRecyclerView =  inflator.findViewById(R.id.recyclerview_workouts);
        mLoadingIndicator = inflator.findViewById(R.id.pb_loading_indicator);

        LinearLayoutManager layoutManagerWorkouts =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManagerWorkouts);
        mRecyclerView.setHasFixedSize(true);
        tAdapter = new WorkoutsAdapter(getContext() , this);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setAdapter(tAdapter);

       // mUri = getActivity().getIntent().getData();
       // if (mUri == null) throw new NullPointerException("URI for WorkoutsFragment cannot be null");
        showLoading();
        LoaderManager loaderManager = getActivity().getSupportLoaderManager();
        loaderManager.initLoader(ID_WORKOUT_LOADER, null, this);
        WorkoutsSyncUtils.initialize(mContext);

        return inflator;
    }

    private void showWorkoutsDataView() {
        /* First, hide the loading indicator */
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        /* Finally, make sure the weather data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showLoading() {
        /* Then, hide the movies data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Finally, show the loading indicator */
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, @Nullable Bundle args) {
        switch (loaderId) {
            case ID_WORKOUT_LOADER:
                showLoading();
                Uri workoutQueryUri = WorkoutsContract.WorkoutEntry.CONTENT_URI;
                String selection = WorkoutsContract.WorkoutEntry.getSqlSelectWorkouts();
                return new CursorLoader(mContext,
                        workoutQueryUri,
                        MAIN_WORKOUT_PROJECTION,
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
        if (data.getCount() != 0) showWorkoutsDataView();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        tAdapter.swapCursor(null);
    }

    @Override
    public void onClick(String exerciseName) {
        Intent exerciseDetailIntent = new Intent(getActivity(), ExercisesActivity.class);
        Uri uriForWorkoutClicked = WorkoutsContract.ExercisetEntry.buildExerciseUriWithName(exerciseName);
        exerciseDetailIntent.setData(uriForWorkoutClicked);
        startActivity(exerciseDetailIntent);
    }
}
