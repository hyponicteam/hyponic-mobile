package com.example.hyponic;

import static com.example.hyponic.constant.ApiConstant.BASE_URL;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.hyponic.adapter.GrowthsAdapter;
import com.example.hyponic.databinding.ActivityGrowthsPlantBinding;
import com.example.hyponic.model.SharedPrefManager;
import com.example.hyponic.model.Growths;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GrowthsPlantActivity extends AppCompatActivity {

    private ArrayList<Growths> growths = new ArrayList<>();
    private ActivityGrowthsPlantBinding binding;
    SharedPrefManager pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        binding = ActivityGrowthsPlantBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //private ArrayList<DetailPlant> detailPlantList = new ArrayList<>();

        pref = new SharedPrefManager(this);
        binding.rvlisttabeltanaman.setHasFixedSize(true);
        getGrowth();

        binding.btnAddPlant.setOnClickListener(view ->
                {
                    Intent moveIntent = new Intent(this, CreateGrowthsActivity.class);
                    startActivity(moveIntent);
                }

        );


        //getTabelGrowth();

//        binding..setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent moveIntent = new Intent(DetailPlantActivity.this, CreateGrowthsActivity.class);
//                startActivity(moveIntent);
//            }
//        });
    }

    public void getGrowth(){
        AndroidNetworking.get(BASE_URL+"plants/"+pref.getSPPlantId())
                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization","Bearer "+pref.getSPToken())
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("growths", "respon: " + response.getJSONObject("data").getJSONArray("growths"));
                            JSONArray data = response.getJSONObject("data").getJSONArray("growths");
                            for(int i=0; i<data.length(); i++){
                                JSONObject jsontabelList = data.getJSONObject(i);
                                Log.d("GROW","ke -"+i+" : "+data.getJSONObject(i));
                                Growths tabellist = new Growths(
                                        jsontabelList.getString("id"),
                                        jsontabelList.getString("plant_height"),
                                        jsontabelList.getString("leaf_width"),
                                        jsontabelList.getString("temperature"),
                                        jsontabelList.getString("acidity"),
                                        jsontabelList.getString("created_at"));
                                growths.add(tabellist);
                            }
                            Log.d("SIZEGROW: ",""+growths.size());
                            showTabelList(growths);

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

    public void getTabelGrowth(){
        AndroidNetworking.get(BASE_URL+"plants/"+pref.getSPPlantId())
                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization","Bearer "+pref.getSPToken())
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            Log.d("TAG", "respon: " + response.getJSONObject("data").getJSONArray("growths"));
                            JSONArray data = response.getJSONObject("data").getJSONArray("growths");
                            for(int i=0; i<data.length(); i++){
                                JSONObject jsontabelList = data.getJSONObject(i);
                                Log.d("TAG","ke -"+i+" : "+data.getJSONObject(i));
                                Growths growthslist = new Growths(jsontabelList.getString("id"),
                                        jsontabelList.getString("plant_height"),
                                        jsontabelList.getString("leaf_width"),
                                        jsontabelList.getString("temperature"),
                                        jsontabelList.getString("acidity"),
                                        jsontabelList.getString("created_at"));
                                growths.add(growthslist);
                            }
                            Log.d("SIZE: ",""+growths.size());
                            showTabelList(growths);

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

    private void showTabelList(ArrayList<Growths> tabellist ){
        binding.rvlisttabeltanaman.setLayoutManager(new LinearLayoutManager(this));
        GrowthsAdapter listGrowthsAdapter = new GrowthsAdapter(tabellist,getSupportFragmentManager(),this);
        binding.rvlisttabeltanaman.setAdapter(listGrowthsAdapter);

        listGrowthsAdapter.setOnItemClickCallback(new GrowthsAdapter.OnItemClickCallback() {
            @Override
            public void onEditClicked(Growths growths) {

                pref.saveSPString(pref.SP_GROWTH_ID, growths.getId());
                Intent moveIntent = new Intent (GrowthsPlantActivity.this, EditGrowthsActivity.class);
                startActivity(moveIntent);
            }

            @Override
            public void onDeleteClicked(Growths growths) {
                pref.saveSPString(pref.SP_GROWTH_ID, growths.getId());

                DeleteGrowthsFragment deleteDetailPlantfragment = new DeleteGrowthsFragment();
                FragmentManager mFragmentManager = getSupportFragmentManager();
                deleteDetailPlantfragment.show(mFragmentManager, DeleteGrowthsFragment.class.getSimpleName());
                ;
            }
        });
    }
}
