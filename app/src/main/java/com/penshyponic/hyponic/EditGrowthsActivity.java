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
    public static final String EXTRA_PLANTID = "extra_plant_id";
    public static final String EXTRA_GROWTH_ID = "extra_growth_id";
    ActivityEditGrowthsBinding binding;
    SharedPrefManager pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEditGrowthsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        pref = new SharedPrefManager(this);
        getData();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.logo);
        binding.btnSave.setOnClickListener(view -> {
            cekEdit();

        });
        binding.btnCancel.setOnClickListener(v->{
            pref.saveSPString(pref.SP_PLANT_ID,pref.getSPPlantId());
            //finish();
            Intent moveIntent = new Intent(this,GrowthsPlantActivity.class);
            moveIntent.putExtra(GrowthsPlantActivity.EXTRA_PLANTID,getIntent().getStringExtra(EXTRA_PLANTID));
            startActivity(moveIntent);
        });

    }

    private void cekEdit() {
        String panjang = binding.edPanjang.getText().toString();
        String lebar = binding.edLebar.getText().toString();
        String suhu = binding.edSuhu.getText().toString();
        String ph = binding.edPh.getText().toString();

        if(panjang.equals("")|| lebar.equals("") || suhu.equals("") || ph.equals("")){
            binding.inputpanjang.setError("Data tidak boleh kosong");
            binding.inputLebar.setError("Data tidak boleh kosong");
            binding.inputSuhu.setError("Data tidak boleh kosong");
            binding.inputPh.setError("Data tidak boleh kosong");
        }else{
            editData();
            //finish();
            Intent moveIntent = new Intent(EditGrowthsActivity.this, GrowthsPlantActivity.class);
            moveIntent.putExtra(GrowthsPlantActivity.EXTRA_PLANTID,getIntent().getStringExtra(EXTRA_PLANTID));
            startActivity(moveIntent);
        }
    }

    public void getData() {
        AndroidNetworking.get(BASE_URL+"growths/"+getIntent().getStringExtra(EXTRA_GROWTH_ID))
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
        AndroidNetworking.patch(BASE_URL+"growths/"+getIntent().getStringExtra(EXTRA_GROWTH_ID))
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