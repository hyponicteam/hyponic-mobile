package com.example.hyponic.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hyponic.R;
import com.example.hyponic.model.TopGrowth;

import java.util.ArrayList;

public class TopWidthAdapter extends RecyclerView.Adapter<TopWidthAdapter.ListViewHolder> {
    private ArrayList<TopGrowth> growths;
    public TopWidthAdapter(ArrayList<TopGrowth> list){
        this.growths=list;
    }
    @NonNull
    @Override
    public TopWidthAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_top_width_growth, parent, false);
        return new TopWidthAdapter.ListViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TopWidthAdapter.ListViewHolder holder, int position) {
        TopGrowth growth = growths.get(position);
        holder.tvNo.setText(position+1);
        holder.tvName.setText(growth.getName());
        holder.tvGrowth.setText(growth.getGrowth_per_day()+" "+growth.getUnit());
    }

    @Override
    public int getItemCount() {
        return growths.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        TextView tvNo, tvName, tvGrowth;
        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNo= itemView.findViewById(R.id.no);
            tvName= itemView.findViewById(R.id.name);
            tvGrowth= itemView.findViewById(R.id.growth);
        }
    }
}
