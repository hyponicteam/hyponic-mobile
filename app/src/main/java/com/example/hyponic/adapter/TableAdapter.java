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
import com.example.hyponic.model.SharedPrefManager;
import com.example.hyponic.model.TabelModel;

import java.util.ArrayList;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.ListViewHolder> {


    private final ArrayList<TabelModel> listTabel;
    private final FragmentManager fragmentManager;
    private final Context tabelContext;
    private SharedPrefManager pref;

    public TableAdapter(ArrayList<TabelModel> listTabel, FragmentManager fragmentManager, Context tabelContext) {
        this.listTabel = listTabel;
        this.fragmentManager = fragmentManager;
        this.tabelContext = tabelContext;
    }

    @NonNull
    @Override
    public TableAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_table_detailtanaman, parent, false);
        return new TableAdapter.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TableAdapter.ListViewHolder holder, int position) {
        TabelModel tabel = listTabel.get(position);

        holder.tvTanggal.setText(tabel.getTime().getCreated_at());
        holder.tvPanjang.setText(tabel.getPlant_height());
        holder.tvLebardaun.setText(tabel.getLeaf_widht());
        holder.tvSuhu.setText(tabel.getTemperature());
        holder.tvPhAir.setText(tabel.getAcidity());
    }

    @Override
    public int getItemCount() {
        return listTabel.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        TextView tvPanjang, tvLebardaun, tvSuhu, tvPhAir, tvTanggal;
        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPanjang = itemView.findViewById(R.id.panjangtable);
            tvLebardaun = itemView.findViewById(R.id.lebardauntable);
            tvPhAir = itemView.findViewById(R.id.phairtable);
            tvSuhu = itemView.findViewById(R.id.suhutable);
            tvTanggal = itemView.findViewById(R.id.tanggaltable);
        }
    }
}
