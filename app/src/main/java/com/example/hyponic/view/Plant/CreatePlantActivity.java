package com.example.hyponic.view.Plant;

import static com.example.hyponic.constant.ApiConstant.BASE_URL;

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
import com.example.hyponic.Home1Activity;
import com.example.hyponic.R;
import com.example.hyponic.databinding.ActivityCreatePlantBinding;
import com.example.hyponic.databinding.ActivityHome1Binding;
import com.example.hyponic.model.SharedPrefManager;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

public class CreatePlantActivity extends AppCompatActivity{

    private ActivityCreatePlantBinding binding;
    SharedPrefManager pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreatePlantBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        pref = new SharedPrefManager(this);

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePlant(view);
            }
        });

    }

    private void savePlant(View view) {
        showLoading(true);
        String namePlant = binding.edNamePlat.getText().toString();
        Log.d("Nama Tanaman",""+namePlant);
        AndroidNetworking.post(BASE_URL+"plants")
                .addHeaders("Authorization","Bearer "+pref.getSPToken())
                .addHeaders("Accept", "application/json")
                .addBodyParameter("name", namePlant)
                .setPriority(Priority.MEDIUM)
                .setTag("test")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        showLoading(false);
                        Log.d("TAG", String.valueOf(response));
                        try {
                            if(response.getJSONObject("meta").getString("status").equals("success")){
                                Snackbar.make(view, "Tanaman Berhasil Disimpan", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                Log.d("Data",""+response.getJSONObject("data"));
                                Intent moveIntent = new Intent(getApplicationContext(), Home1Activity.class);
                                startActivity(moveIntent);
                            }else {
                                Snackbar.make(view, "Tanaman Gagal Disimpan", Snackbar.LENGTH_LONG).setAction("Action", null).show();
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
    }
    private void showLoading(Boolean isLoading) {
        if (isLoading) {
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.GONE);
        }
    }
}