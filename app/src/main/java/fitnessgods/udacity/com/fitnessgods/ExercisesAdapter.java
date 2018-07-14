package fitnessgods.udacity.com.fitnessgods;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import fitnessgods.udacity.com.fitnessgods.data.WorkoutsContract;
import fitnessgods.udacity.com.fitnessgods.utilities.CircleTransform;

public class ExercisesAdapter extends RecyclerView.Adapter<ExercisesAdapter.ExercisesAdapterViewHolder> {

    private Cursor mCursor;
    private final Context mContext;
    final private ExercisesAdapterOnClickHandler mClickHandler;

    public ExercisesAdapter(@NonNull Context context , ExercisesAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public ExercisesAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        int layoutId;
        layoutId = R.layout.exercise_item;
        View view = LayoutInflater.from(mContext).inflate(layoutId, viewGroup, false);
        view.setFocusable(true);

        return new ExercisesAdapter.ExercisesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExercisesAdapter.ExercisesAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        String exercise_name = mCursor.getString(mCursor.getColumnIndex(WorkoutsContract.ExercisetEntry.COLUMN_EXERCISE_NAME));
        String exercise_image_url = mCursor.getString(mCursor.getColumnIndex(WorkoutsContract.ExercisetEntry.COLUMN_EXERCISE_IMG_URL));
        holder.exercise_name.setText(exercise_name);
        if(exercise_image_url != null)
            if(!TextUtils.isEmpty(exercise_image_url))
                Picasso.with(mContext).load(exercise_image_url).transform(new CircleTransform()).into(holder.exercise_image);
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

    public interface ExercisesAdapterOnClickHandler {
        void onClick(String exerciseName);
    }

    class ExercisesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        final TextView exercise_name;
        final ImageView exercise_image;

        ExercisesAdapterViewHolder(View view)
        {
            super(view);
            exercise_name = view.findViewById(R.id.exercise_name);
            exercise_image = view.findViewById(R.id.exercise_image);

            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            String exerciseParentName = mCursor.getString(mCursor.getColumnIndex(WorkoutsContract.ExercisetEntry.COLUMN_EXERCISE_PARENT_NAME));
            mClickHandler.onClick(exerciseParentName);
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }
}
