package com.example.hyponic.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hyponic.R;
import com.example.hyponic.model.Artikel;

import java.util.ArrayList;

public class ArtikelAdapter extends RecyclerView.Adapter<ArtikelAdapter.ListViewHolder> {
    private ArrayList<Artikel> listArtikel;

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
        //holder.artikelPhoto.setImageResource(artikel.getImage_url());
        holder.tvTitle.setText(artikel.getTitle());
        holder.tvCategory.setText(artikel.getCategory().getName());

    }

    @Override
    public int getItemCount() {
        return listArtikel.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        //ImageView artikelPhoto;
        TextView tvTitle,tvCategory;
        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            //imgPhoto = itemView.findViewById(R.id.img_item_photo);
            tvTitle = itemView.findViewById(R.id.tv_artikel_title);
            tvCategory = itemView.findViewById(R.id.tv_artikel_category);
        }
    }
}
