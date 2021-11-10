package com.example.hyponic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.hyponic.model.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;
import static com.example.hyponic.constant.ApiConstant.BASE_URL;

public class LoginActivity extends AppCompatActivity{
    private EditText edtPassword,edtEmail;
    private Button btnLogin;
    private String email, password;
    SharedPrefManager preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }
    public void init(){
        edtPassword = findViewById(R.id.passwordText);
        edtEmail= findViewById(R.id.emailText);
        btnLogin =findViewById(R.id.loginButton);
        preferences = new SharedPrefManager(this);
        AndroidNetworking.initialize(getApplicationContext());
    }
    public void onClickDaftar(View view) {
        Intent moveIntent = new Intent(LoginActivity.this, RegisterActivity.class);
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
                    .addHeaders("Accept", "application/json")
                    .addBodyParameter("email", email)
                    .addBodyParameter("password",password)
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("TAG", String.valueOf(response));
                            try {
                                if(response.getJSONObject("meta").getString("status").equals("success")){
                                    JSONObject dataUser = response.getJSONObject("data").getJSONObject("user");
                                    String token = response.getJSONObject("data").getString("access_token");

                                    Toast.makeText(getApplicationContext(),response.getJSONObject("meta")
                                            .getString("message"),Toast.LENGTH_SHORT).show();

                                    preferences.saveSPString(preferences.SP_NAMA,dataUser.getString("name"));
                                    preferences.saveSPInt(String.valueOf(preferences.SP_ID),dataUser.getInt("id"));
                                    preferences.saveSPString(preferences.SP_TOKEN,token);

                                    Intent moveIntent = new Intent(getApplicationContext(),Home1Activity.class);
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
                            Log.d("ERROR", String.valueOf(error));
                        }
                    });
            //Intent moveIntent = new Intent(getApplicationContext(),Home1Activity.class);
            //startActivity(moveIntent);
        }

    }
}