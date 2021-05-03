package com.example.plantsapp.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.plantsapp.CustomListAdapter;
import com.example.plantsapp.Plant;
import com.example.plantsapp.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.plantsapp.ui.main.MainActivity.filterText;


public class AllPlantsFragment extends PlantsFragment {

    private List<Plant> all_plants = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1_layout, container, false);
        ListView listView = view.findViewById(R.id.listview);
        all_plants = getPlants(MainActivity.plants);
        adapter = new CustomListAdapter(getActivity(), all_plants);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View plantView, int position, long id) {
                int plant_id = MainActivity.plants.indexOf(adapter.getCurrentPlants().get(position));
                MainActivity.plants.remove(plant_id);
                MainActivity.update();
                Toast.makeText(getContext(), "Plant is deleted successfully.", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        return view;
    }

    @Override
    public List<Plant> getPlants(List<Plant> allplants){
        return new ArrayList<>(allplants);
    }

    public void update() {
        all_plants = getPlants(MainActivity.plants);
        adapter.update(all_plants);
        adapter.getFilter().filter(filterText);
    }
}
