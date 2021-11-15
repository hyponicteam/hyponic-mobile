package com.example.hyponic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hyponic.R;
import com.example.hyponic.model.DetailPlant;
import com.example.hyponic.model.Plant;
import com.example.hyponic.model.SharedPrefManager;

import java.util.ArrayList;

public class DetailPlantAdapter extends RecyclerView.Adapter<DetailPlantAdapter.ListViewHolder>{

    private ArrayList<DetailPlant> listDetailPlant;
    private FragmentManager fragmentManager;
    private Context detailplantContext;
    private LayoutInflater mInflater;
    private SharedPrefManager pref;

    public DetailPlantAdapter(ArrayList<DetailPlant> listDetailPlant, FragmentManager fragmentManager, Context detailplantContext) {
        this.listDetailPlant = listDetailPlant;
        this.fragmentManager = fragmentManager;
        this.detailplantContext = detailplantContext;
        this.mInflater = mInflater;
    }

//    @Override
//    public DetailPlantAdapter(){}
//
//    public PlantAdapter(Context context,FragmentManager fragment, ArrayList<Plant> list) {
//        this.plantContext=context;
//        fragmentManager = fragment;
//        this.listPlant=list;
//        this.mInflater = LayoutInflater.from(this.plantContext);
//    }

    @NonNull
    @Override
    public DetailPlantAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_detailplant, parent, false);
        return new DetailPlantAdapter.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailPlantAdapter.ListViewHolder holder, int position) {
        DetailPlant detailplant = listDetailPlant.get(position);
        holder.tvTanggal.setText(detailplant.getTime().getCreated_at());
        holder.tvPanjang.setText(detailplant.getPlant_height());
        holder.tvLebardaun.setText(detailplant.getLeaf_widht());
        holder.tvSuhu.setText(detailplant.getTemperature());
        holder.tvPhAir.setText(detailplant.getAcidity());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        TextView tvPanjang, tvLebardaun, tvSuhu, tvPhAir, tvTanggal;
        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPanjang = itemView.findViewById(R.id.panjang2);
            tvLebardaun = itemView.findViewById(R.id.lebardaun2);
            tvPhAir = itemView.findViewById(R.id.phair2);
            tvSuhu = itemView.findViewById(R.id.suhu2);
            tvTanggal = itemView.findViewById(R.id.tanggalPantau);
        }
    }
}
