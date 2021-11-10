package com.example.hyponic;

import static com.example.hyponic.constant.ApiConstant.BASE_URL;

import android.os.Bundle;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.example.hyponic.adapter.PlantAdapter;
import com.example.hyponic.api_response.ListPlantResponse;
import com.example.hyponic.model.Artikel;
import com.example.hyponic.model.Artikel_Kategori;
import com.example.hyponic.model.Plant;
import com.example.hyponic.model.SharedPrefManager;
import com.example.hyponic.model.Time;
import com.example.hyponic.model.User;
import com.example.hyponic.view.CreatePlantDialog;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hyponic.databinding.ActivityHome1Binding;
import com.example.hyponic.DummyData.PlantDummyData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Home1Activity extends AppCompatActivity{

    private RecyclerView rvPlant;
    private ArrayList<Plant> planList = new ArrayList<>();
    private ActivityHome1Binding binding;
    SharedPrefManager pref;
    private TextView tvusername,tvGetAllPlant;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidNetworking.initialize(getApplicationContext());
        binding = ActivityHome1Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                  //      .setAction("Action", null).show();
                CreatePlantDialog dialog = new CreatePlantDialog();
                dialog.show(getSupportFragmentManager(),"Create Plant Dialog");

            }
        });


        pref = new SharedPrefManager(this);
        tvusername=findViewById(R.id.username);
        tvGetAllPlant=findViewById(R.id.btnGetAllPlant);
        tvusername.setText(pref.getSPNama());
        rvPlant=findViewById(R.id.rv_NLatestPlant);
        rvPlant.setHasFixedSize(true);
        //planList.addAll(PlantDummyData.getListData());
        token=pref.getSPToken();
        //showRecyclerList(planList);

        tvGetAllPlant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Action ke halaman Tanaman", Toast.LENGTH_SHORT).show();
            }
        });

        getNPlantDataApi();

    }
    public void getNPlantDataApi(){
        AndroidNetworking.get(BASE_URL+"latest-plants?n=3")
                .addHeaders("Authorization","Bearer "+pref.getSPToken())
                .addHeaders("Accept", "application/json")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener(){

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("TAG", "respon: " + response.getJSONArray("data"));
                            JSONArray data = response.getJSONArray("data");
                            for(int i=0; i<data.length(); i++){
                                JSONObject jsonPlant = data.getJSONObject(i);
                                Log.d("TAG","ke -"+i+" : "+data.getJSONObject(i));
                                Plant plant = new Plant(jsonPlant.getString("id"),
                                        jsonPlant.getString("name"));

                                plant.setTime(new Time());
                                plant.getTime().setCreated_at(jsonPlant.getString("created_at"));
                                plant.getTime().setUpdated_at(jsonPlant.getString("updated_at"));
                                planList.add(plant);
                            }
                            Log.d("SIZE: ",""+planList.size());
                            showRecyclerList(planList);

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
    private void showRecyclerList(ArrayList<Plant> plants) {
        rvPlant.setLayoutManager(new LinearLayoutManager(this));
        PlantAdapter listPlantAdapter = new PlantAdapter(this,getSupportFragmentManager(),plants);
        rvPlant.setAdapter(listPlantAdapter);
    }

}