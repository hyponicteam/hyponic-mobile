package com.example.hyponic;

import static com.example.hyponic.constant.ApiConstant.BASE_URL;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.hyponic.adapter.DetailPlantAdapter;
import com.example.hyponic.adapter.TableAdapter;
import com.example.hyponic.databinding.ActivityCreateDetailTanamanBinding;
import com.example.hyponic.databinding.ActivityDetailPlantBinding;
import com.example.hyponic.databinding.FragmentDetailplantfragmentBinding;
import com.example.hyponic.model.DetailPlant;
import com.example.hyponic.model.SharedPrefManager;
import com.example.hyponic.model.TabelModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DetailPlantActivity extends AppCompatActivity {

    private RecyclerView rvdetailPlant;
    private RecyclerView rvTabelDetail;
    private ArrayList<TabelModel> tabelList = new ArrayList<>();
    private ArrayList<DetailPlant> detailPlantList = new ArrayList<>();
    private ActivityDetailPlantBinding binding;
    SharedPrefManager pref;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDetailPlantBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        pref = new SharedPrefManager(this);

        binding.rvListdetTanaman.setHasFixedSize(true);
        binding.rvListtabelTanaman.setHasFixedSize(true);

        getGrowth();
        getTabelGrowth();
    }

    public void getGrowth(){
        AndroidNetworking.get(BASE_URL+"/plants/fb21a915-a119-464a-8b89-f2884388e1cb")
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
                                JSONObject jsondetailPlant = data.getJSONObject(i);
                                Log.d("TAG","ke -"+i+" : "+data.getJSONObject(i));
                                DetailPlant detailplant = new DetailPlant(jsondetailPlant.getString("id"),
                                        jsondetailPlant.getString("plant_height"), jsondetailPlant.getString("leaf_width"), jsondetailPlant.getString("temperature"), jsondetailPlant.getString("acidity"));

                                detailPlantList.add(detailplant);
                            }
                            Log.d("SIZE: ",""+detailPlantList.size());
                            showRecyclerList(detailPlantList);

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
        AndroidNetworking.get(BASE_URL+"/plants/fb21a915-a119-464a-8b89-f2884388e1cb")
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
                                TabelModel tabellist = new TabelModel(jsontabelList.getString("id"),
                                        jsontabelList.getString("plant_height"), jsontabelList.getString("leaf_width"), jsontabelList.getString("temperature"), jsontabelList.getString("acidity"), jsontabelList.getString("created_at"));

                                tabelList.add(tabellist);
                            }
                            Log.d("SIZE: ",""+tabelList.size());
                            showTabelList(tabelList);

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
    private void showRecyclerList(ArrayList<DetailPlant> detailplants) {
        rvdetailPlant.setLayoutManager(new LinearLayoutManager(this));
        DetailPlantAdapter listDetailPlantAdapter = new DetailPlantAdapter(detailplants, getSupportFragmentManager(), this);
        rvdetailPlant.setAdapter(listDetailPlantAdapter);
    }

    private void showTabelList(ArrayList<TabelModel> tabellist ){
        rvTabelDetail.setLayoutManager(new LinearLayoutManager(this));
        TableAdapter listTabelAdapter = new TableAdapter(tabellist,getSupportFragmentManager(),this);
        rvTabelDetail.setAdapter(listTabelAdapter);
    }
}
