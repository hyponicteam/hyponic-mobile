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
import com.example.hyponic.databinding.ActivityCreateDetailTanamanBinding;
import com.example.hyponic.model.SharedPrefManager;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

public class CreateDetailTanaman extends AppCompatActivity {

    private ActivityCreateDetailTanamanBinding binding;
    private String panjang, lebar, suhu, ph;
    SharedPrefManager pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCreateDetailTanamanBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        pref = new SharedPrefManager(this);

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDataPlant(view);
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
                    .setTag("test")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //showLoading(false);
                            Log.d("TAG", String.valueOf(response));
                            try {
                                if(response.getJSONObject("meta").getString("status").equals("success")){
                                    Snackbar.make(view, "Tanaman Berhasil Disimpan", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                    Log.d("Data",""+response.getJSONObject("data"));
                                    Intent moveIntent = new Intent(getApplicationContext(), Detailplantfragment.class);
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
                            //showLoading(false);
                            // handle error
                            Toast.makeText(getApplicationContext(),""+error, Toast.LENGTH_SHORT).show();
                            Log.d("ERROR", String.valueOf(error));
                        }
                    });
        }
    }
}