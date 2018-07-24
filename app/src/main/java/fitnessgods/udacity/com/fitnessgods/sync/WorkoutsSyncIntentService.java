package fitnessgods.udacity.com.fitnessgods.sync;

import android.app.IntentService;
import android.content.Intent;

public class WorkoutsSyncIntentService extends IntentService {

    public WorkoutsSyncIntentService() {
        super("WorkoutsSyncIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
            WorkoutsSyncTask.syncWorkouts(this);
    }
}
