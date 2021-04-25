package com.example.plantsapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class CustomListAdapter extends BaseAdapter implements Filterable {

    private List<Plant> plantsList;
    private List<Plant> plantsListFull;
    private LayoutInflater layoutInflater;
    private Context context;
    private ViewHolder holder;

    public CustomListAdapter(Context aContext,  List<Plant> plantsList) {
        this.context = aContext;
        this.plantsList = plantsList;
        this.plantsListFull = new ArrayList<>(plantsList);
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public int getCount() {
        return plantsList.size();
    }

    @Override
    public Object getItem(int position) {
        return plantsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.plant_item_layout, null);
            holder = new ViewHolder();
            holder.plantImageView = (ImageView) convertView.findViewById(R.id.plantImageView);
            holder.plantStatusView = (ProgressBar) convertView.findViewById(R.id.plantStatusView);
            holder.plantNameView = (TextView) convertView.findViewById(R.id.plantNameView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Plant plant = this.plantsList.get(position);
        holder.plantNameView.setText(plant.getPlant_name());
        Glide.with(context)
                .load(plant.getPlantImage())
                .circleCrop()
                .into(holder.plantImageView);
        holder.plantStatusView.setProgress(plant.getRemainingPercentage());
        return convertView;
    }

    public void update(List<Plant> plants){
        plantsList.clear();
        plantsList.addAll(plants);
        plantsListFull = new ArrayList<>(plants);
    }

    public int getMipmapResIdByName(String resName)  {
        String pkgName = context.getPackageName();
        int resID = context.getResources().getIdentifier(resName , "mipmap", pkgName);
        Log.i("CustomListView", "Res Name: "+ resName+"==> Res ID = "+ resID);
        return resID;
    }

    @Override
    public Filter getFilter() {
        return FilteredPlants;
    }

    private Filter FilteredPlants = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Plant> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(plantsListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Plant plant : plantsListFull) {
                    if (plant.getPlant_name().toLowerCase().contains(filterPattern)) {
                        filteredList.add(plant);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            plantsList.clear();
            plantsList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public List<Plant> getCurrentPlants(){ return plantsList;}

    static class ViewHolder {
        ImageView plantImageView;
        TextView plantNameView;
        ProgressBar plantStatusView;
    }
}
