package com.example.plantsapp.ui.main;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.plantsapp.AlarmReceiver;
import com.example.plantsapp.BadgeView;
import com.example.plantsapp.DBHelper;
import com.example.plantsapp.Plant;
import com.example.plantsapp.R;
import com.example.plantsapp.ui.main.CreatePlantActivity;
import com.example.plantsapp.ui.main.PlantsFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.widget.SearchView;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.inputmethod.EditorInfo;

import androidx.appcompat.widget.Toolbar;
import com.example.plantsapp.ui.main.SectionsPagerAdapter;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    static BadgeView badge;
    static DBHelper plants_db;
    static ViewPager viewPager;
    static SectionsPagerAdapter sectionsPagerAdapter;
    int SECONDS_ALARM = 60 * 60;
    int SECONDS_THREAD = 60;
    public static List<Plant> plants;
    public static String filterText;

    private void updatePlantsThread() {
        Thread thread = new Thread() {

            @Override
            public void run() {
                try {
                    while (true) {
                        Thread.sleep(SECONDS_THREAD * 1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                update();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        thread.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        plants_db = new DBHelper(this);
        plants = plants_db.getPlants();
        setContentView(R.layout.activity_main);
        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);

        View view1 = getLayoutInflater().inflate(R.layout.customtab, null);
        view1.findViewById(R.id.icon).setBackgroundResource(R.mipmap.alive_plant_icon);
        View view2 = getLayoutInflater().inflate(R.layout.customtab, null);
        view2.findViewById(R.id.icon).setBackgroundResource(R.mipmap.dead_plant_icon);
        badge = new BadgeView(this, (View)view2.findViewById(R.id.icon));
        tabs.getTabAt(0).setCustomView(view1);
        tabs.getTabAt(1).setCustomView(view2);

        updatePlantsThread();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), CreatePlantActivity.class);
                startActivity(myIntent);
            }
        });

        SearchView searchView = findViewById(R.id.search_view);
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                updatePlantsFragments();
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                filterText = newText;
                updatePlantsFragments();
                return false;
            }
        });
        scheduleAlarm();
    }

    public void scheduleAlarm() {
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        final PendingIntent pIntent = PendingIntent.getBroadcast(getApplicationContext(), AlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long firstMillis = System.currentTimeMillis();
        AlarmManager alarm = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, firstMillis,
                1000 * SECONDS_ALARM, pIntent);
    }

    public static void insertPlant(Plant plant){
        MainActivity.plants.add(plant);
        MainActivity.plants_db.insertPlant(plant);
        MainActivity.update();
    }

    public static void deletePlant(Plant plant){
        MainActivity.plants.remove(plant);
        MainActivity.plants_db.deletePlant(plant.getUniqueId());
        MainActivity.update();
    }

    public static void refreshPlant(Plant plant){
        MainActivity.plants.get(MainActivity.plants.indexOf(plant)).refresh();
        MainActivity.update();
    }

    public static void update(){
        for (Plant plant : plants) {
            plant.update();
            plants_db.updatePlant(plant.getUniqueId(), plant);
        }
        updatePlantsFragments();
    }

    public static void updatePlantsFragments(){
        PlantsFragment[] plantsFragment = new PlantsFragment[]{(PlantsFragment) sectionsPagerAdapter.instantiateItem(viewPager, 0),
                (PlantsFragment) sectionsPagerAdapter.instantiateItem(viewPager, 1)};
        for (PlantsFragment pf : plantsFragment) {
            pf.update();
        }
    }

    public static BadgeView getBadge(){
        return badge;
    }
}