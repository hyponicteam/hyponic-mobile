package com.example.hyponic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class BerandaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beranda);
    }

    public void onClickBacaArtikel(View view) {
        Intent intent = new Intent(BerandaActivity.this,LihatArtikelActivity.class);
        startActivity(intent);
    }
}