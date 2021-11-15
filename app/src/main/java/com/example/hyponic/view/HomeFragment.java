package com.example.hyponic.view;

import static com.example.hyponic.constant.ApiConstant.BASE_URL;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.hyponic.R;
import com.example.hyponic.adapter.PlantAdapter;
import com.example.hyponic.databinding.FragmentHomeBinding;
import com.example.hyponic.model.Plant;
import com.example.hyponic.model.SharedPrefManager;
import com.example.hyponic.model.Time;
import com.example.hyponic.view.Plant.CreatePlantFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements  View.OnClickListener {


    private FragmentHomeBinding binding;

    SharedPrefManager pref;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pref = new SharedPrefManager(getContext());
        binding.username.setText(pref.getSPNama());
        showLoading(true);
        getPlantData();
        binding.rvNLatestPlant.setHasFixedSize(true);
        //planList.addAll(PlantDummyData.getListData());
        //showRecyclerList(planList);
        binding.btnAddPlant.setOnClickListener(this);

    }
    private void getPlantData() {
        ArrayList<Plant> planList = new ArrayList<>();
        AndroidNetworking.get(BASE_URL+"latest-plants?n=3")
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
    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnAddPlant){
            CreatePlantFragment createPlantFragment = new CreatePlantFragment();
            FragmentManager mFragmentManager = getChildFragmentManager();
            createPlantFragment.show(mFragmentManager, CreatePlantFragment.class.getSimpleName());
        }
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}