package com.example.hyponic.view.Plant;

import static com.example.hyponic.constant.ApiConstant.BASE_URL;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.hyponic.adapter.PlantAdapter;
import com.example.hyponic.adapter.TopHeightAdapter;
import com.example.hyponic.adapter.TopWidthAdapter;
import com.example.hyponic.databinding.FragmentPlantBinding;
import com.example.hyponic.model.Plant;
import com.example.hyponic.model.SharedPrefManager;
import com.example.hyponic.model.Time;
import com.example.hyponic.model.TopGrowth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PlantFragment extends Fragment {

    SharedPrefManager pref;
    private FragmentPlantBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPlantBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pref = new SharedPrefManager(getContext());
        binding.rvNLatestPlant.setHasFixedSize(true);
        getPlantData();
        getTopHeigh();
        getTopWidth();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void getPlantData() {
        showLoading(true);
        ArrayList<Plant> planList = new ArrayList<>();
        AndroidNetworking.get(BASE_URL+"plants")
                .addHeaders("Authorization","Bearer "+pref.getSPToken())
                .addHeaders("Accept", "application/json")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener(){

                    @Override
                    public void onResponse(JSONObject response) {
                        showLoading(false);
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
                        showLoading(false);
                        Log.d("TAG", "onError: " + error); //untuk log pada onerror

                    }
                });
    }
    private void getTopHeigh() {
        ArrayList<TopGrowth> growths = new ArrayList<>();
        AndroidNetworking.get(BASE_URL+"top-plants?category=plant_height&n=3")
                .addHeaders("Authorization","Bearer "+pref.getSPToken())
                .addHeaders("Accept", "application/json")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener(){

                    @Override
                    public void onResponse(JSONObject response) {
                        showLoading(false);
                        try {
                            Log.d("TAG", "respon: " + response.getJSONArray("data"));
                            JSONArray data = response.getJSONArray("data");
                            for(int i=0; i<data.length(); i++){
                                JSONObject jsonGrowth = data.getJSONObject(i);
                                Log.d("TAG","ke -"+i+" : "+data.getJSONObject(i));

                                TopGrowth growth = new TopGrowth();
                                growth.setName(jsonGrowth.getString("name"));
                                growth.setGrowth_per_day(jsonGrowth.getJSONObject("growth").getDouble("growth_per_day"));
                                growth.setUnit(jsonGrowth.getJSONObject("growth").getString("unit"));
                                growths.add(growth);
                            }
                            Log.d("SIZE: ",""+growths.size());
                            showListGrowthHeight(growths);

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        showLoading(false);
                        Log.d("TAG", "onError: " + error); //untuk log pada onerror
                    }
                });
    }
    private void getTopWidth() {
        ArrayList<TopGrowth> growths = new ArrayList<>();
        AndroidNetworking.get(BASE_URL+"top-plants?category=plant_height&n=3")
                .addHeaders("Authorization","Bearer "+pref.getSPToken())
                .addHeaders("Accept", "application/json")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener(){

                    @Override
                    public void onResponse(JSONObject response) {
                        showLoading(false);
                        try {
                            Log.d("TAG", "respon: " + response.getJSONArray("data"));
                            JSONArray data = response.getJSONArray("data");
                            for(int i=0; i<data.length(); i++){
                                JSONObject jsonGrowth = data.getJSONObject(i);
                                Log.d("TAG","ke -"+i+" : "+data.getJSONObject(i));

                                TopGrowth growth = new TopGrowth();
                                growth.setName(jsonGrowth.getString("name"));
                                growth.setGrowth_per_day(jsonGrowth.getJSONObject("growth").getDouble("growth_per_day"));
                                growth.setUnit(jsonGrowth.getJSONObject("growth").getString("unit"));
                                growths.add(growth);
                            }
                            Log.d("SIZE: ",""+growths.size());
                            showListGrowthWidth(growths);

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        showLoading(false);
                        Log.d("TAG", "onError: " + error); //untuk log pada onerror
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

    private void showRecyclerList(ArrayList<Plant> plants) {
        binding.rvNLatestPlant.setLayoutManager(new LinearLayoutManager(getContext()));
        PlantAdapter listPlantAdapter = new PlantAdapter(getContext(),getChildFragmentManager(),plants);
        binding.rvNLatestPlant.setAdapter(listPlantAdapter);
    }
    private void showListGrowthHeight(ArrayList<TopGrowth> plants) {
        binding.rvNTopHeight.setLayoutManager(new LinearLayoutManager(getContext()));
        TopHeightAdapter list = new TopHeightAdapter(plants);
        binding.rvNLatestPlant.setAdapter(list);
    }
    private void showListGrowthWidth(ArrayList<TopGrowth> plants) {
        binding.rvNTopWidth.setLayoutManager(new LinearLayoutManager(getContext()));
        TopWidthAdapter list = new TopWidthAdapter(plants);
        binding.rvNLatestPlant.setAdapter(list);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}