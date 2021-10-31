package com.example.hyponic.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hyponic.R;
import com.example.hyponic.model.DailyActivity;

import java.util.ArrayList;

public class DailyActivityAdapter extends RecyclerView.Adapter<DailyActivityAdapter.ListViewHolder> {
    private ArrayList<DailyActivity> listDailyActivity= new ArrayList();

    public DailyActivityAdapter(ArrayList<DailyActivity> list){
        this.listDailyActivity=list;

    }
    @NonNull
    @Override
    public DailyActivityAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_col_dailyactivity, parent, false);
        return new DailyActivityAdapter.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyActivityAdapter.ListViewHolder holder, int position) {
        DailyActivity dailyActivity = listDailyActivity.get(position);
        holder.tvTitle.setText(dailyActivity.getName());
        holder.tvJenisSayur.setText(dailyActivity.getTanaman().getName());
        //holder.img_dailyActivity.setImageResource(dailyActivity.ge);
    }

    @Override
    public int getItemCount() {
        return listDailyActivity.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        public ImageView img_dailyActivity;
        public TextView tvTitle,tvJenisSayur;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            //img_dailyActivity = itemView.findViewById(R.id.imageDailyActivity);
            tvTitle = itemView.findViewById(R.id.tv_daylyActivity_title);
            tvJenisSayur = itemView.findViewById(R.id.tv_item_jenisSayur);
        }
    }
}
