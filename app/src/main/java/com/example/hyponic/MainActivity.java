package com.example.hyponic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity{
    private EditText edtPassword,edtEmail;
    private Button btnMasuk;
    private int coba;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtPassword = findViewById(R.id.passwordText);
        edtEmail= findViewById(R.id.emailText);
        btnMasuk =findViewById(R.id.loginButton);


    }
    public void onClickDaftar(View view) {
        Intent moveIntent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(moveIntent);
    }
    public void onClickMasuk(View view) {
        Intent moveIntent = new Intent(MainActivity.this, BerandaActivity.class);
        startActivity(moveIntent);
    }
}