package com.penshyponic.hyponic.adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.penshyponic.hyponic.R;
import com.penshyponic.hyponic.model.Artikel;

import java.util.ArrayList;

public class ArtikelAdapter extends RecyclerView.Adapter<ArtikelAdapter.ListViewHolder> {
    private ArrayList<Artikel> listArtikel = new ArrayList<>();

    public ArtikelAdapter(ArrayList<Artikel> list){
        this.listArtikel=list;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_artikel, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        Artikel artikel = listArtikel.get(position);
        holder.tvTitle.setText(artikel.getTitle());
        holder.tvCategory.setText(artikel.getAuthor2());
    }

    @Override
    public int getItemCount() {
        return listArtikel.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        public ImageView artikelPhoto;
        TextView tvTitle,tvCategory;
        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            artikelPhoto = itemView.findViewById(R.id.imageArtikel);
            tvTitle = itemView.findViewById(R.id.tv_artikel_title);
            tvCategory = itemView.findViewById(R.id.tv_artikel_category);
        }
    }
}
