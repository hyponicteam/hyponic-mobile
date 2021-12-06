package com.example.hyponic;

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
import com.example.hyponic.databinding.ActivityCreateGrowthsBinding;
import com.example.hyponic.model.DetailPlant;
import com.example.hyponic.model.Growths;
import com.example.hyponic.model.SharedPrefManager;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

public class CreateGrowthsActivity extends AppCompatActivity {

    private ActivityCreateGrowthsBinding binding;
    private String panjang, lebar, suhu, ph;
    SharedPrefManager pref;
    private Growths growths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCreateGrowthsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        pref = new SharedPrefManager(this);

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveDataPlant(view);
                Intent moveIntent = new Intent(CreateGrowthsActivity.this, GrowthsPlantActivity.class);
                startActivity(moveIntent);
            }
        });

//        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
    }

    public void saveDataPlant(View view){
        panjang = binding.edPanjang.getText().toString();
        lebar = binding.edLebar.getText().toString();
        suhu = binding.edSuhu.getText().toString();
        ph = binding.edPh.getText().toString();

        if(panjang.equals("")|| lebar.equals("") || suhu.equals("") || ph.equals("")){
            Toast.makeText(this,"Data tidak boleh kosong", Toast.LENGTH_LONG).show();
        }
        else{
            AndroidNetworking.post(BASE_URL+"growths")
                    .addHeaders("Authorization","Bearer "+pref.getSPToken())
                    .addHeaders("Accept", "application/json")
                    .addBodyParameter("plant_height", panjang)
                    .addBodyParameter("leaf_width", lebar)
                    .addBodyParameter("temperature", suhu)
                    .addBodyParameter("acidity", ph)
                    .addBodyParameter("plant_id", pref.getSPPlantId())
                    .setPriority(Priority.MEDIUM)
                    .setTag("test")
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //showLoading(false);
                            Log.d("TAG", String.valueOf(response));
                            try {
                                if(response.getJSONObject("meta").getString("status").equals("success")){
                                    Log.d("Data",""+response.getJSONObject("data"));
                                    Toast.makeText(getApplicationContext(),"DATA BERHASIL DISIMPAN",Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(getApplicationContext(),"DATA GAGAl DISIMPAN",Toast.LENGTH_SHORT).show();
                                }

                            }catch (JSONException e){
                                Toast.makeText(getApplicationContext(),"Login Gagal"+e,Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onError(ANError error) {
                            //showLoading(false);
                            // handle error
                            Toast.makeText(getApplicationContext(),""+error, Toast.LENGTH_SHORT).show();
                            Log.d("ERROR", String.valueOf(error));
                        }
                    });
        }
    }
}