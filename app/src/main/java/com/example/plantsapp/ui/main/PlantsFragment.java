package com.example.plantsapp.ui.main;

import androidx.fragment.app.Fragment;

import com.example.plantsapp.CustomListAdapter;
import com.example.plantsapp.Plant;

import java.util.ArrayList;
import java.util.List;

public class PlantsFragment extends Fragment {
    public CustomListAdapter adapter;

    protected List<Plant> getPlants(List<Plant> all_plants){
        return new ArrayList<>();
    }

    public void update() { }
}

