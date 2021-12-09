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

import com.penshyponic.hyponic.R;
import com.penshyponic.hyponic.model.Growths;
import com.penshyponic.hyponic.model.SharedPrefManager;

import java.util.ArrayList;

public class GrowthsAdapter extends RecyclerView.Adapter<GrowthsAdapter.ListViewHolder> {


    private final ArrayList<Growths> listGrowths;
    private final FragmentManager fragmentManager;
    private final Context tabelContext;
    private SharedPrefManager pref;
    private GrowthsAdapter.OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(GrowthsAdapter.OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public GrowthsAdapter(ArrayList<Growths> listTabel, FragmentManager fragmentManager, Context tabelContext) {
        this.listGrowths = listTabel;
        this.fragmentManager = fragmentManager;
        this.tabelContext = tabelContext;
    }

    @NonNull
    @Override
    public GrowthsAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_detailplant, parent, false);
        return new GrowthsAdapter.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GrowthsAdapter.ListViewHolder holder, int position) {
        Growths growths = listGrowths.get(position);

        holder.tvTanggal.setText(growths.getDate());
        holder.tvPanjang.setText(String.valueOf(growths.getPlant_height()));
        holder.tvLebardaun.setText(String.valueOf(growths.getLeaf_widht()));
        holder.tvSuhu.setText(String.valueOf(growths.getTemperature()));
        holder.tvPhAir.setText(String.valueOf(growths.getAcidity()));
        holder.btnEdit.setOnClickListener(view ->{
            onItemClickCallback.onEditClicked(growths);
        }
        );
        holder.btnDelete.setOnClickListener(view ->{
            onItemClickCallback.onDeleteClicked(growths);
        });
    }

    @Override
    public int getItemCount() {
        return listGrowths.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        TextView tvPanjang, tvLebardaun, tvSuhu, tvPhAir, tvTanggal;
        ImageView btnEdit, btnDelete;
        public ListViewHolder(@NonNull View itemView) {
            super(itemView);

            tvPanjang = itemView.findViewById(R.id.panjang);
            tvLebardaun = itemView.findViewById(R.id.lebardaun);
            tvPhAir = itemView.findViewById(R.id.phair);
            tvSuhu = itemView.findViewById(R.id.suhu);
            tvTanggal = itemView.findViewById(R.id.tanggalPantau);
            btnDelete = itemView.findViewById(R.id.deleteTable);
            btnEdit = itemView.findViewById(R.id.editTable);
        }
    }

    public interface OnItemClickCallback {
        void onEditClicked(Growths growths);
        void onDeleteClicked(Growths growths);
    }
}