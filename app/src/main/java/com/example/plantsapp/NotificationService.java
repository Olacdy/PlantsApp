package com.example.plantsapp;

import android.app.ActivityManager;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.plantsapp.ui.main.MainActivity;

import java.util.List;

public class NotificationService extends IntentService {
    private static String CHANNEL_ID = "Plants";
    private static int NOTIFY_ID = 1543;

    public NotificationService() {
        super("NotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        DBHelper plants_db = new DBHelper(this);
        List<Plant> plants = plants_db.getPlants();
        int counter = 0;
        plants_db.deleteAll();
        for (Plant plant : plants) {
            plant.update();
            plants_db.insertPlant(plant);
            if (!plant.isStatus()) counter++;
        }
        if (counter > 0 && !isActivityRunning(MainActivity.class)) Notify(counter);
    }


    protected Boolean isActivityRunning(Class activityClass)
    {
        ActivityManager activityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);

        for (ActivityManager.RunningTaskInfo task : tasks) {
            if (activityClass.getCanonicalName().equalsIgnoreCase(task.baseActivity.getClassName()))
                return true;
        }

        return false;
    }

    protected void Notify(int counter){
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setContentTitle("Не забувайте про ваші рослини.")
                        .setSmallIcon(R.drawable.ic_stat_dead_plant_icon_small)
                        .setContentText("Деякі з віших рослин потребують догляду!")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setNumber(counter)
                        .setColor(this.getResources().getColor(R.color.black));
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);
        notificationManager.notify(NOTIFY_ID, builder.build());
    }
}
