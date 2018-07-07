package fitnessgods.udacity.com.fitnessgods.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fitnessgods.udacity.com.fitnessgods.R;
import fitnessgods.udacity.com.fitnessgods.utilities.WorkoutsSyncUtils;

public class WorkoutsFragment  extends Fragment {

    public WorkoutsFragment() {
        // Required empty public constructor
    }

    public static WorkoutsFragment newInstance(String param1, String param2) {
        WorkoutsFragment fragment = new WorkoutsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        WorkoutsSyncUtils.initialize(getContext());
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.workouts_fragment, container, false);
    }
}
