package com.example.hyponic.adapter;
import static com.example.hyponic.constant.ApiConstant.BASE_URL;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.manager.SupportRequestManagerFragment;
import com.example.hyponic.Home1Activity;
import com.example.hyponic.R;
import com.example.hyponic.model.Plant;
import com.example.hyponic.model.SharedPrefManager;
import com.example.hyponic.view.DeletePlantDialog;
import com.example.hyponic.view.EditPlantDialog;
import com.example.hyponic.view.Plant.EditPlantActivity;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PlantAdapter extends RecyclerView.Adapter<PlantAdapter.ListViewHolder> {
    private ArrayList<Plant> listPlant;
    private FragmentManager fragmentManager;
    private Context plantContext;
    private LayoutInflater mInflater;
    private SharedPrefManager pref;

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

        holder.tvView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(plantContext.getApplicationContext(), "Ganti dengan Action lihat", Toast.LENGTH_SHORT).show();
            }
        });
        holder.tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pref.saveSPString(pref.SP_PLANT_ID,plant.getId());
                EditPlantDialog dialog = new EditPlantDialog();
                dialog.show(fragmentManager,"Dialog");
            }
        });
        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DeletePlantDialog dialog = new DeletePlantDialog();
                dialog.show(fragmentManager,"Delete Dialog");
                pref.saveSPString(pref.SP_PLANT_ID,plant.getId());

            }
        });
    }

    @Override
    public int getItemCount() {
        return listPlant.size();
    }


    public class ListViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvLastEdit, tvEdit, tvView, tvDelete;
        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.namePlant);
            tvLastEdit = itemView.findViewById(R.id.lastEditPlant);
            tvEdit=itemView.findViewById(R.id.editPlant);
            tvView=itemView.findViewById(R.id.seePlant);
            tvDelete=itemView.findViewById(R.id.deletePlant);
        }
    }

}
