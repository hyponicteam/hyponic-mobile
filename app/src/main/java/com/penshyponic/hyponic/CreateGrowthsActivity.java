package com.penshyponic.hyponic;

import static com.penshyponic.hyponic.constant.ApiConstant.BASE_URL;
import static com.penshyponic.hyponic.model.CreateGrowthHistory.history;

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
import com.penshyponic.hyponic.databinding.ActivityCreateGrowthsBinding;
import com.penshyponic.hyponic.model.GrowthHistory;
import com.penshyponic.hyponic.model.Growths;
import com.penshyponic.hyponic.model.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

public class CreateGrowthsActivity extends AppCompatActivity {
    public static final String EXTRA_PLANTID = "extra_plant_id";
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
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.logo);
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cekSave(view);
            }
        });

        binding.btnCancel.setOnClickListener(v->{
              //finish();
            Intent moveIntent = new Intent(CreateGrowthsActivity.this, GrowthsPlantActivity.class);
            moveIntent.putExtra(GrowthsPlantActivity.EXTRA_PLANTID,getIntent().getStringExtra(EXTRA_PLANTID));
            startActivity(moveIntent);
        });
    }
    public void cekSave(View view){
        panjang = binding.edPanjang.getText().toString();
        lebar = binding.edLebar.getText().toString();
        suhu = binding.edSuhu.getText().toString();
        ph = binding.edPh.getText().toString();

        if(panjang.equals("")|| lebar.equals("") || suhu.equals("") || ph.equals("")){
            binding.inputpanjang.setError("Data tidak boleh kosong");
            binding.inputLebar.setError("Data tidak boleh kosong");
            binding.inputSuhu.setError("Data tidak boleh kosong");
            binding.inputPh.setError("Data tidak boleh kosong");
        }
        else{
            saveDataPlant(view);
            Calendar calender = Calendar.getInstance();
            String date =calender.get(Calendar.YEAR)+"-"+calender.get(Calendar.MONTH)+"-"+calender.get(Calendar.DATE);
            int flag = -1;
            for(int i =0; i<history.size();i++){
                if(history.get(i).getPlantId().equals(pref.getSPPlantId())){
                    flag=i;
                }
            }
            if(flag==-1){
                history.add(new GrowthHistory(pref.getSPPlantId(),date));
            }else{
                history.get(flag).setTimeCreated(date);
            }
            Intent moveIntent = new Intent(CreateGrowthsActivity.this, GrowthsPlantActivity.class);
            moveIntent.putExtra(GrowthsPlantActivity.EXTRA_PLANTID,pref.getSPPlantId());
            startActivity(moveIntent);
        }
    }
    public void saveDataPlant(View view){

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
                                    Calendar calendar = Calendar.getInstance();
                                    String plantId =response.getJSONObject("data").getString("plant_id");
                                    String date = calendar.get(Calendar.YEAR)+"-"+calendar.get(Calendar.MONTH)+"-"+calendar.get(Calendar.DATE);
                                    pref.saveSPString(pref.SP_Create_Growth,plantId+date);
                                    Toast.makeText(getApplicationContext(),"Data Berhasil Disimpan",Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(getApplicationContext(),"Data Gagal Disimpan",Toast.LENGTH_SHORT).show();
                                }

                            }catch (JSONException e){
                                Toast.makeText(getApplicationContext(),"Gagal Disimpan"+e,Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onError(ANError error) {
                            //showLoading(false);
                            // handle error
                            Toast.makeText(getApplicationContext(),"Gagal Disimpan, hanya dapat memasukkan satu data pantauan dalam satu hari", Toast.LENGTH_SHORT).show();
                            Log.d("CREATEERROR", String.valueOf(error.getErrorBody()));
                        }
                    });

    }
}