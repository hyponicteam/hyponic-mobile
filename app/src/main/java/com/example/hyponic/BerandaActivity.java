package com.example.hyponic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;

import com.example.hyponic.adapter.ArtikelAdapter;
import com.example.hyponic.model.Artikel;

import java.util.ArrayList;

public class BerandaActivity extends AppCompatActivity {
    private RecyclerView rvArtikel;
    private ArrayList<Artikel> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beranda);
        init();
        list.addAll(getListArtikel());
        showRecyclerList();
    }
    public void init(){
        rvArtikel = findViewById(R.id.rv_artikel);
        rvArtikel.setHasFixedSize(true);

    }

    public void onClickBacaArtikel(View view) {
        Intent intent = new Intent(BerandaActivity.this,LihatArtikelActivity.class);
        startActivity(intent);
    }
    public ArrayList<Artikel> getListArtikel() {
        String[] dataTitle = getResources().getStringArray(R.array.data_title);
        String[] dataDescription = getResources().getStringArray(R.array.data_category);
        //TypedArray dataPhoto = getResources().obtainTypedArray(R.array.data_photo);
        ArrayList<Artikel> listHero = new ArrayList<>();
        for (int i = 0; i < dataTitle.length; i++) {
            Artikel artikel = new Artikel();
            artikel.setTitle(dataTitle[i]);
            artikel.setArticle_categories_id(dataDescription[i]);
            //hero.setPhoto(dataPhoto.getResourceId(i, -1));
            listHero.add(artikel);
        }
        return listHero;
    }

    private void showRecyclerList(){
        rvArtikel.setLayoutManager(new LinearLayoutManager(this));
        ArtikelAdapter listHeroAdapter = new ArtikelAdapter(list);
        rvArtikel.setAdapter(listHeroAdapter);
    }
}