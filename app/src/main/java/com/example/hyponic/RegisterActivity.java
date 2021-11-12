package com.example.hyponic;

import static com.example.hyponic.constant.ApiConstant.BASE_URL;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.hyponic.databinding.ActivityRegisterBinding;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    //viewBinding
    private ActivityRegisterBinding binding;
    private String name, email, password, konfpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.buttonRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                simpanData();
            }
        });
        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(moveIntent);
            }
        });

//        binding.daftarGoogle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent moveIntent = new Intent(RegisterActivity.this, RegisterActivity.class);
//                startActivity(moveIntent);
//            }
//        });
    }

    public void simpanData() {
        name = binding.NameLabel.getText().toString();
        email = binding.mailRegisLabel.getText().toString();
        password = binding.RegisPasswordLabel.getText().toString();
        konfpass = binding.KonfPassLabel.getText().toString();

        //binding.cobaview.setText(String.valueOf(password)+String.valueOf(konfpass));

        if(email.equals("")|| password.equals("") || name.equals("") || konfpass.equals("")){
            Toast.makeText(this,"Data tidak boleh kosong", Toast.LENGTH_LONG).show();
        }
        else {
            if(password.equals(konfpass)) {
                showLoading(true);
                AndroidNetworking.post(BASE_URL+"auth/register")
                        .addHeaders("Accept", "application/json")
                        .addBodyParameter("name", name)
                        .addBodyParameter("email", email)
                        .addBodyParameter("password", password)
                        .addBodyParameter("password_confirmation", konfpass)
                        .setTag("test")
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // do anything with response
                                showLoading(false);

                                try {
                                    if(response.getJSONObject("meta").getString("status").equals("success")){
                                        Toast.makeText(getApplicationContext(),"Register Berhasil",Toast.LENGTH_SHORT).show();
                                        Intent moveIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(moveIntent);
                                    }else{
                                        Toast.makeText(getApplicationContext(),"Register Gagal",Toast.LENGTH_SHORT).show();
                                    }

                                }catch (JSONException e){
                                    Toast.makeText(getApplicationContext(),"Register Gagal"+e,Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onError(ANError error) {
                                showLoading(false);
                                // handle error
                                Toast.makeText(getApplicationContext(),""+error, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
            else {
                Toast.makeText(this, "Konfirmasi password yang sesuai", Toast.LENGTH_LONG).show();
            }
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