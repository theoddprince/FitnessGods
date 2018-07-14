package fitnessgods.udacity.com.fitnessgods;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import fitnessgods.udacity.com.fitnessgods.data.WorkoutsContract;

public class WorkoutsAdapter  extends RecyclerView.Adapter<WorkoutsAdapter.WorkoutAdapterViewHolder>{

    private Cursor mCursor;
    private final Context mContext;
    final private WorkoutAdapterOnClickHandler mClickHandler;

    public interface WorkoutAdapterOnClickHandler {
        void onClick(String exerciseName);
    }

    public WorkoutsAdapter(@NonNull Context context , WorkoutAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public WorkoutAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        int layoutId;
        layoutId = R.layout.workout_item;
        View view = LayoutInflater.from(mContext).inflate(layoutId, viewGroup, false);
        view.setFocusable(true);

        return new WorkoutsAdapter.WorkoutAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        String workout_name = mCursor.getString(mCursor.getColumnIndex(WorkoutsContract.WorkoutEntry.COLUMN_WORKOUT_NAME));
        String poster_url = mCursor.getString(mCursor.getColumnIndex(WorkoutsContract.WorkoutEntry.COLUMN_WORKOUT_POSTER_URL));
        holder.workout_name.setText(workout_name);
        if(poster_url != null)
            if(!TextUtils.isEmpty(poster_url))
                Picasso.with(mContext).load(poster_url).into(holder.workout_poster);

    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    class WorkoutAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        final TextView workout_name;
        final ImageView workout_poster;

        WorkoutAdapterViewHolder(View view)
        {
            super(view);
            workout_name = view.findViewById(R.id.workout_name);
            workout_poster = view.findViewById(R.id.workout_image);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            String exerciseName = mCursor.getString(mCursor.getColumnIndex(WorkoutsContract.WorkoutEntry.COLUMN_WORKOUT_NAME));
            mClickHandler.onClick(exerciseName);
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }

}


