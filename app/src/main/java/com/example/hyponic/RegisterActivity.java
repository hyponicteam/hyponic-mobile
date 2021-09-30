package com.example.hyponic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void onClickDaftar(View view) {
        Intent moveIntent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(moveIntent);
    }
    public void onClickMasuk(View view) {
        Intent moveIntent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(moveIntent);
    }
}