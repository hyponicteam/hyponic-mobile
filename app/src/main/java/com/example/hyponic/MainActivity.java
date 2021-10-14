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
import static com.example.hyponic.constant.ApiConstant.BASE_URL;

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
        if(email.equals("")|| password.equals("")){
            edtEmail.setError("email tidak boleh kosong");
            edtPassword.setError("password tidak boleh kosong");
        }else{
            AndroidNetworking.post(BASE_URL+"auth/login")
                    .addBodyParameter("email", email)
                    .addBodyParameter("password",password)
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if(response.getJSONObject("meta").getString("status").equals("success")){
                                    Toast.makeText(getApplicationContext(),"Login Berhasil",Toast.LENGTH_SHORT).show();
                                    Intent moveIntent = new Intent(getApplicationContext(), BerandaActivity.class);
                                    startActivity(moveIntent);
                                }else{
                                    Toast.makeText(getApplicationContext(),"Login Gagal",Toast.LENGTH_SHORT).show();
                                }

                            }catch (JSONException e){
                                Toast.makeText(getApplicationContext(),"Login Gagal"+e,Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onError(ANError error) {
                            // handle error
                            Toast.makeText(getApplicationContext(),""+error, Toast.LENGTH_SHORT).show();
                        }
                    });
        }

    }
}