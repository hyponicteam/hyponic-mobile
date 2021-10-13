package com.example.hyponic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity{
    private EditText edtPassword,edtEmail;
    private Button btnLogin;
    private String email, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }
    public void init(){
        edtPassword = findViewById(R.id.passwordText);
        edtEmail= findViewById(R.id.emailText);
        btnLogin =findViewById(R.id.loginButton);
    }
    public void onClickDaftar(View view) {
        Intent moveIntent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(moveIntent);
    }
    public void onClickMasuk(View view) {
        email = edtEmail.getText().toString();
        password = edtPassword.getText().toString();

        Intent moveIntent = new Intent(this, BerandaActivity.class);
        startActivity(moveIntent);

    }
}