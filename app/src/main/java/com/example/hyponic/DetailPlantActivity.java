package com.example.hyponic;

import static com.example.hyponic.constant.ApiConstant.BASE_URL;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.hyponic.adapter.TableAdapter;
import com.example.hyponic.databinding.ActivityDetailPlantBinding;
import com.example.hyponic.model.SharedPrefManager;
import com.example.hyponic.model.TabelModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DetailPlantActivity extends AppCompatActivity {

    private ArrayList<TabelModel> growths = new ArrayList<>();
    private ActivityDetailPlantBinding binding;
    SharedPrefManager pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailPlantBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        pref = new SharedPrefManager(this);
        //binding.rvListtabelTanaman.setHasFixedSize(true);
        //getTabelGrowth();

        binding.btnAddDataPlant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveIntent = new Intent(DetailPlantActivity.this, CreateDetailTanaman.class);
                startActivity(moveIntent);
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
                                TabelModel tabellist = new TabelModel(jsontabelList.getString("id"),
                                        jsontabelList.getString("plant_height"),
                                        jsontabelList.getString("leaf_width"),
                                        jsontabelList.getString("temperature"),
                                        jsontabelList.getString("acidity"),
                                        jsontabelList.getString("created_at"));
                                growths.add(tabellist);
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

    private void showTabelList(ArrayList<TabelModel> tabellist ){
        binding.rvListtabelTanaman.setLayoutManager(new LinearLayoutManager(this));
        TableAdapter listTabelAdapter = new TableAdapter(tabellist,getSupportFragmentManager(),this);
        binding.rvListtabelTanaman.setAdapter(listTabelAdapter);
    }
}
