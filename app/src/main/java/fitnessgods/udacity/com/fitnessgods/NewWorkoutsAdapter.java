package fitnessgods.udacity.com.fitnessgods;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import fitnessgods.udacity.com.fitnessgods.data.WorkoutsContract;

public class NewWorkoutsAdapter  extends RecyclerView.Adapter<NewWorkoutsAdapter.NewWorkoutAdapterViewHolder>{

    private Cursor mCursor;
    private final Context mContext;
    final private NewWorkoutsAdapter.NewWorkoutAdapterOnClickHandler mClickHandler;
    private final static int FADE_DURATION = 1000; //FADE_DURATION in milliseconds

    public NewWorkoutsAdapter(@NonNull Context context , NewWorkoutAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public NewWorkoutAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        int layoutId;
        layoutId = R.layout.new_workout_item;
        View view = LayoutInflater.from(mContext).inflate(layoutId, viewGroup, false);
        view.setFocusable(true);

        return new NewWorkoutsAdapter.NewWorkoutAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewWorkoutAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        String new_workout_name = mCursor.getString(mCursor.getColumnIndex(WorkoutsContract.NewWorkoutEntry.COLUMN_NEW_WORKOUT_NAME));
        holder.workout_name.setText(new_workout_name);

        setFadeAnimation(holder.itemView);
    }

    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }

    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    public interface NewWorkoutAdapterOnClickHandler {
        void onClick(String newWorkoutName);
    }

    class NewWorkoutAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        final TextView workout_name;

        NewWorkoutAdapterViewHolder(View view)
        {
            super(view);
            workout_name = view.findViewById(R.id.new_workout_name);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            String newWorkoutName = mCursor.getString(mCursor.getColumnIndex(WorkoutsContract.NewWorkoutEntry.COLUMN_NEW_WORKOUT_NAME));
            mClickHandler.onClick(newWorkoutName);
        }

        @Override
        public boolean onLongClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            String newWorkoutName = mCursor.getString(mCursor.getColumnIndex(WorkoutsContract.NewWorkoutEntry.COLUMN_NEW_WORKOUT_NAME));
            ContentResolver newWorkoutsContentResolver = mContext.getContentResolver();
            String[] selectionArguments = new String[]{newWorkoutName};
            Uri uriForMovieClicked = WorkoutsContract.NewWorkoutEntry.buildExerciseUriWithName(newWorkoutName);
            newWorkoutsContentResolver.delete(uriForMovieClicked,
                    WorkoutsContract.NewWorkoutEntry.COLUMN_NEW_WORKOUT_NAME + " = ?",
                     selectionArguments);

            return true;
        }
    }
}
