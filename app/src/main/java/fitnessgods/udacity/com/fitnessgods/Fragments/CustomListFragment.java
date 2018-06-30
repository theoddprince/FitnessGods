package fitnessgods.udacity.com.fitnessgods.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fitnessgods.udacity.com.fitnessgods.R;

public class CustomListFragment  extends Fragment {

    public CustomListFragment() {
        // Required empty public constructor
    }

    public static CustomListFragment newInstance(String param1, String param2) {
        CustomListFragment fragment = new CustomListFragment();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.customlist_fragment, container, false);
    }
}
