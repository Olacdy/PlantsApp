package com.example.plantsapp.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.plantsapp.CustomListAdapter;
import com.example.plantsapp.Plant;
import com.example.plantsapp.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.plantsapp.ui.main.MainActivity.filterText;

public class DeadPlantsFragment extends PlantsFragment {

    private List<Plant> dead_plants = new ArrayList<>();
    private int badgeNum;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment2_layout, container, false);
        ListView listView = view.findViewById(R.id.listview);
        dead_plants = getPlants(MainActivity.plants);
        adapter = new CustomListAdapter(getActivity(), dead_plants);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View plantView, int position, long id) {
                MainActivity.plants.get(MainActivity.plants.indexOf(adapter.getCurrentPlants().get(position))).refresh();
                MainActivity.update();
                return true;
            }
        });
        return view;
    }

    public void update() {
        dead_plants = getPlants(MainActivity.plants);
        adapter.update(dead_plants);
        adapter.getFilter().filter(filterText);
    }

    public List<Plant> getPlants(List<Plant> all_plants){
        MainActivity.getBadge().decrement(badgeNum);
        badgeNum = 0;
        List<Plant> only_dead = new ArrayList<>();
        for (Plant plant: all_plants) {
            if(!plant.isStatus()){
                only_dead.add(plant);
                badgeNum++;
            }
        }
        if(badgeNum > 0) {
            MainActivity.getBadge().show();
            MainActivity.getBadge().increment(badgeNum);
        }
        else MainActivity.getBadge().hide();
        return only_dead;
    }
}
