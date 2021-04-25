package com.example.plantsapp;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.plantsapp.ui.main.MainActivity;

import java.util.List;

public class AlarmReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 54523;
    private static String CHANNEL_ID = "Plants";
    private static int NOTIFY_ID = 1543;

    @Override
    public void onReceive(Context context, Intent intent) {
        DBHelper plants_db = new DBHelper(context);
        List<Plant> plants = plants_db.getPlants();
        int counter = 0;
        plants_db.deleteAll();
        for (Plant plant : plants) {
            plant.update();
            plants_db.insertPlant(plant);
            if (!plant.isStatus()) counter++;
        }
        if (counter > 0 && !isActivityRunning(MainActivity.class, context)) Notify(counter, context);
        //Intent i = new Intent(context, NotificationService.class);
        //context.startService(i);
    }



    protected Boolean isActivityRunning(Class activityClass, Context context)
    {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);

        for (ActivityManager.RunningTaskInfo task : tasks) {
            if (activityClass.getCanonicalName().equalsIgnoreCase(task.baseActivity.getClassName()))
                return true;
        }

        return false;
    }

    protected void Notify(int counter, Context context){
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setContentTitle("Не забувайте про ваші рослини.")
                        .setSmallIcon(R.drawable.ic_stat_dead_plant_icon_small)
                        .setContentText("Деякі з віших рослин потребують догляду!")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setNumber(counter)
                        .setColor(context.getResources().getColor(R.color.black));
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(context);
        notificationManager.notify(NOTIFY_ID, builder.build());
    }
}
