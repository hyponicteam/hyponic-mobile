package com.penshyponic.hyponic;

import static com.penshyponic.hyponic.constant.ApiConstant.BASE_URL;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.penshyponic.hyponic.databinding.ActivityEditGrowthsBinding;
import com.penshyponic.hyponic.model.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

public class EditGrowthsActivity extends AppCompatActivity {

    ActivityEditGrowthsBinding binding;
    SharedPrefManager pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEditGrowthsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        pref = new SharedPrefManager(this);
        getData();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.logo);
        binding.btnSave.setOnClickListener(view -> {
            editData();
            Intent moveIntent = new Intent(EditGrowthsActivity.this, GrowthsPlantActivity.class);
            startActivity(moveIntent);
            //finish();
        });
        binding.btnCancel.setOnClickListener(v->{
            pref.saveSPString(pref.SP_PLANT_ID,pref.getSPPlantId());
            Intent moveIntent = new Intent(this,GrowthsPlantActivity.class);
            startActivity(moveIntent);
        });

    }

    public void getData() {
        AndroidNetworking.get(BASE_URL+"growths/"+pref.getSPGrowthsId())
                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization","Bearer "+pref.getSPToken())
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("growths", "respon: " + response.getJSONObject("data"));

                            JSONObject data = response.getJSONObject("data");
                            binding.edPanjang.setText(String.valueOf(data.getDouble("plant_height")));
                            binding.edLebar.setText(String.valueOf(data.getDouble("leaf_width")));
                            binding.edSuhu.setText(String.valueOf(data.getDouble("temperature")));
                            binding.edPh.setText(String.valueOf(data.getDouble("acidity")));

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        Log.d("TAG", "onError: " + error); //untuk log pada onerror
                    }
                });
    }

    public void editData() {
        AndroidNetworking.patch(BASE_URL+"growths/"+pref.getSPGrowthsId())
                .addHeaders("Authorization","Bearer "+pref.getSPToken())
                .addHeaders("Accept", "application/json")
                .addBodyParameter("plant_height",binding.edPanjang.getText().toString())
                .addBodyParameter("leaf_width",binding.edLebar.getText().toString())
                .addBodyParameter("temperature",binding.edSuhu.getText().toString())
                .addBodyParameter("acidity",binding.edPh.getText().toString())
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener(){
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getJSONObject("meta").getString("status");
                            if(status.equals("success")){
                                Toast.makeText(getApplicationContext(), "Berhasil diperbarui", Toast.LENGTH_SHORT).show();
                                saveSPPlantID(response.getJSONObject("data").getString("plant_id"));
                            }else{
                                Toast.makeText(getApplicationContext(), "Gagal diperbarui", Toast.LENGTH_SHORT).show();

                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        Log.d("TAG", "onError: " + error); //untuk log pada onerror
                    }
                });
    }
    public void saveSPPlantID(String id){
        pref.saveSPString(pref.SP_PLANT_ID,id);
    }
}