package com.example.hyponic.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hyponic.R;
import com.example.hyponic.model.TopGrowth;
import java.util.ArrayList;

public class TopHeightAdapter extends RecyclerView.Adapter<TopHeightAdapter.ListViewHolder> {

    private ArrayList<TopGrowth> listGrowth;

    public TopHeightAdapter(ArrayList<TopGrowth> list){
        this.listGrowth=list;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_top_height_growth, parent, false);
        return new TopHeightAdapter.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        TopGrowth growth = listGrowth.get(position);
        holder.tvNo.setText(position+1);
        holder.tvName.setText(growth.getName());
        holder.tvGrowth.setText(growth.getGrowth_per_day()+" "+growth.getUnit());
    }

    @Override
    public int getItemCount() {
        return listGrowth.size();
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
