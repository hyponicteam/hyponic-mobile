package com.example.hyponic.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hyponic.R;
import com.example.hyponic.model.Artikel;


import java.util.ArrayList;

public class ArticleAdapter  extends RecyclerView.Adapter<ArticleAdapter.ListViewHolder> {
    private ArrayList<Artikel> list;
    private OnItemClickCallback onItemClickCallback;


    public ArticleAdapter(ArrayList<Artikel> articles){
        this.list=articles;
    }
    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }
    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_artikel, parent, false);
        return new ArticleAdapter.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        Artikel artikel = list.get(position);
        holder.tvTitle.setText(artikel.getTitle());
        holder.tvAuthor.setText(holder.tvAuthor.getText().toString()+" "+artikel.getAuthor().getName());
        holder.itemView.setOnClickListener(v->{
//            Toast.makeText(holder.itemView.getContext(), "Kamu memilih "
//                    + list.get(holder.getAdapterPosition()).getTitle(), Toast.LENGTH_SHORT).show();
            onItemClickCallback.onItemClicked(list.get(holder.getAdapterPosition()));
        });
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvAuthor;
        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle=itemView.findViewById(R.id.tv_artikel_title);
            tvAuthor=itemView.findViewById(R.id.tv_artikel_category);
        }
    }
    public interface OnItemClickCallback {
        void onItemClicked(Artikel data);
    }
}
