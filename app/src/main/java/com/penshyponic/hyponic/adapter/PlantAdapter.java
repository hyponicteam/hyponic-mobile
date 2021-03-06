package com.penshyponic.hyponic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

//import com.example.hyponic.DetailGrowthsFragment;
import com.penshyponic.hyponic.R;
import com.penshyponic.hyponic.model.Plant;
import com.penshyponic.hyponic.model.SharedPrefManager;
import com.penshyponic.hyponic.view.Plant.DeletePlantFragment;
import com.penshyponic.hyponic.view.Plant.EditPlantFragment;
import java.util.ArrayList;

public class PlantAdapter extends RecyclerView.Adapter<PlantAdapter.ListViewHolder> {
    private ArrayList<Plant> listPlant;
    private FragmentManager fragmentManager;
    private Context plantContext;
    private LayoutInflater mInflater;
    private SharedPrefManager pref;
    private OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }
    public PlantAdapter(Context context,FragmentManager fragment, ArrayList<Plant> list) {
        this.plantContext=context;
        fragmentManager = fragment;
        this.listPlant=list;
        this.mInflater = LayoutInflater.from(this.plantContext);
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_plant, parent, false);
        return new PlantAdapter.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        Plant plant = listPlant.get(position);
        holder.tvName.setText(plant.getName());
        holder.tvLastEdit.setText(plant.getTime().getUpdated_at());
        pref = new SharedPrefManager(plantContext);

        holder.tvView.setOnClickListener(view -> {
            onItemClickCallback.onViewClicked(listPlant.get(holder.getAdapterPosition()));
        });
        holder.tvEdit.setOnClickListener(view -> {
            pref.saveSPString(pref.SP_PLANT_ID,plant.getId());
            onItemClickCallback.onEditClicked(listPlant.get(holder.getAdapterPosition()));
        });
        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pref.saveSPString(pref.SP_PLANT_ID,plant.getId());
                DeletePlantFragment deletePlantFragment = new DeletePlantFragment();
                FragmentManager mFragmentManager = fragmentManager;
                deletePlantFragment.show(mFragmentManager, EditPlantFragment.class.getSimpleName());

            }
        });
    }

    @Override
    public int getItemCount() {
        return listPlant.size();
    }




    public class ListViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvLastEdit, tvView;
        ImageView tvEdit,tvDelete;
        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.namePlant);
            tvLastEdit = itemView.findViewById(R.id.lastEditPlant);
            tvEdit=itemView.findViewById(R.id.editPlant);
            tvView=itemView.findViewById(R.id.seePlant);
            tvDelete=itemView.findViewById(R.id.deletePlant);
        }
    }
    public interface OnItemClickCallback {
        void onEditClicked(Plant plant);
        void onViewClicked(Plant plant);
    }

}
