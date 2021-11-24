package com.example.hyponic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.hyponic.databinding.ActivityLoginBinding;
import com.example.hyponic.model.SharedPrefManager;
import org.json.JSONException;
import org.json.JSONObject;
import static com.example.hyponic.constant.ApiConstant.BASE_URL;

public class LoginActivity extends AppCompatActivity{
    private ActivityLoginBinding binding;
    private String email, password;
    SharedPrefManager preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();

        if (preferences.getSPIsLogin()){
            startActivity(new Intent(LoginActivity.this, MainActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }
    }
    public void init(){
        preferences = new SharedPrefManager(this);
        AndroidNetworking.initialize(getApplicationContext());
    }
    public void onClickDaftar(View view) {
        Intent moveIntent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(moveIntent);
    }
    public void onClickMasuk(View view) {
        email = binding.emailText.getText().toString();
        password =binding.passwordText.getText().toString();

        if(email.equals("")|| password.equals("")){
            binding.emailText.setError("email tidak boleh kosong");
            binding.passwordText.setError("Password tidak boleh kosong");
        }else{
            showLoading(true);
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
                            showLoading(false);
                            Log.d("TAG", String.valueOf(response));
                            try {
                                if(response.getJSONObject("meta").getString("status").equals("success")){
                                    JSONObject dataUser = response.getJSONObject("data").getJSONObject("user");
                                    String token = response.getJSONObject("data").getString("access_token");

                                    Toast.makeText(getApplicationContext(),response.getJSONObject("meta")
                                            .getString("message"),Toast.LENGTH_SHORT).show();

                                    preferences.saveSPString(preferences.SP_NAMA,dataUser.getString("name"));
                                    preferences.saveSPString(preferences.SP_EMAIL,dataUser.getString("email"));
                                    preferences.saveSPInt(String.valueOf(preferences.SP_ID),dataUser.getInt("id"));
                                    preferences.saveSPString(preferences.SP_TOKEN,token);
                                    preferences.saveSPBoolean(preferences.SP_IS_LOGIN,true);

                                    Intent moveIntent = new Intent(getApplicationContext(),MainActivity.class);
                                    startActivity(moveIntent);
                                    finish();
                                }else{
                                    Toast.makeText(getApplicationContext(),"Login Gagal",Toast.LENGTH_SHORT).show();
                                }

                            }catch (JSONException e){
                                Toast.makeText(getApplicationContext(),"Login Gagal"+e,Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onError(ANError error) {
                            showLoading(false);
                            // handle error
                            Toast.makeText(getApplicationContext(),""+error, Toast.LENGTH_SHORT).show();
                            Log.d("ERROR", String.valueOf(error));
                        }
                    });
            //showLoading(false);
            //Intent moveIntent = new Intent(getApplicationContext(),Home1Activity.class);
            //startActivity(moveIntent);
        }
    }
    private void showLoading(Boolean isLoading) {
        if (isLoading) {
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.GONE);
        }
    }
}