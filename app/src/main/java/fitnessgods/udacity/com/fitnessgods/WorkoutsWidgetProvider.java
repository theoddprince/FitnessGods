package fitnessgods.udacity.com.fitnessgods;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import fitnessgods.udacity.com.fitnessgods.data.Workouts;
import fitnessgods.udacity.com.fitnessgods.sync.WorkoutsWidgetIntentService;

/**
 * Implementation of App Widget functionality.
 */
public class WorkoutsWidgetProvider extends AppWidgetProvider {

    //public static Exercises exercise;
    public static Workouts workout;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                Workouts oneWorkout ,int appWidgetId) {

        workout = oneWorkout;
        //In case we added the widget but still didn't open the app , as a default get the first recipe from the list
        // and assign it to be presented .
        if(workout == null)
        {
           // For now nothing to do.
        }

        if(workout != null) {
            Intent intent = new Intent(context, WorkoutViewService.class);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.workouts_widget_provider);
            views.setRemoteAdapter(R.id.list_view_widget, intent);

            Intent startActivityIntent = new Intent(context, DetailedExerciseActivity.class);
            PendingIntent startActivityPendingIntent = PendingIntent.getActivity(context, 0, startActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.list_view_widget, startActivityPendingIntent);

            views.setTextViewText(R.id.workout_name_widget , workout.getWorkout_name());
            ComponentName component = new ComponentName(context, WorkoutsWidgetProvider.class);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.list_view_widget);
            appWidgetManager.updateAppWidget(component, views);
        }
    }

    public static void updateWorkoutWidgets(Context context, AppWidgetManager appWidgetManager,
                                            Workouts workoutName , int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, workoutName , appWidgetId);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        WorkoutsWidgetIntentService.startActionUpdateWorkoutWidgets(context , workout);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }


}

