package com.penshyponic.hyponic.view;

import static com.penshyponic.hyponic.constant.ApiConstant.BASE_URL;

import android.content.Intent;
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
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.penshyponic.hyponic.GrowthsPlantActivity;
import com.penshyponic.hyponic.R;
import com.penshyponic.hyponic.adapter.PlantAdapter;
import com.penshyponic.hyponic.databinding.FragmentHomeBinding;
import com.penshyponic.hyponic.model.Plant;
import com.penshyponic.hyponic.model.SharedPrefManager;
import com.penshyponic.hyponic.model.Time;
import com.penshyponic.hyponic.view.Plant.CreatePlantFragment;
import com.penshyponic.hyponic.view.Plant.EditPlantFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements View.OnClickListener{

    private FragmentHomeBinding binding;
    SharedPrefManager pref;

    public HomeFragment() {
        // Required empty public constructor
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return  binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pref = new SharedPrefManager(getContext());
        binding.username.setText("Hai "+pref.getSPNama()+",");
        showLoading(true);
        getPlantData();
        Log.d("TOKEN USER",pref.getSPToken());
        binding.rvNLatestPlant.setHasFixedSize(true);
        //planList.addAll(PlantDummyData.getListData());tr
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
                            Log.d("LAST EDIT PLANT", "respon: " + response.getJSONArray("data"));
                            JSONArray data = response.getJSONArray("data");
                            for(int i=0; i<data.length(); i++){
                                JSONObject jsonPlant = data.getJSONObject(i);
                                Log.d("LAST EDIT PLANT","ke -"+i+" : "+data.getJSONObject(i));
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
                        Log.d("LAST EDIT PLANT", "Belum ada data " + error); //untuk log pada onerror
                        showNotFound(true);
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnAddPlant){
            CreatePlantFragment mCategoryFragment = new CreatePlantFragment();
            FragmentManager mFragmentManager = getParentFragmentManager();
            mFragmentManager
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment_activity_main, mCategoryFragment, CreatePlantFragment.class.getSimpleName())
                    .addToBackStack(null)
                    .commit();
        }
    }
    private void showLoading(Boolean isLoading) {
        if (isLoading) {
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.GONE);
        }
    }
    private void showNotFound(Boolean isAnyData){
        if(isAnyData){
            binding.notFound.getRoot().setVisibility(View.VISIBLE);
        }else{
            binding.notFound.getRoot().setVisibility(View.GONE);
        }
    }

    private void showRecyclerList(ArrayList<Plant> plants) {
        binding.rvNLatestPlant.setLayoutManager(new LinearLayoutManager(getContext()));
        PlantAdapter listPlantAdapter = new PlantAdapter(getContext(),getParentFragmentManager(),plants);
        binding.rvNLatestPlant.setAdapter(listPlantAdapter);

        listPlantAdapter.setOnItemClickCallback(new PlantAdapter.OnItemClickCallback() {

            @Override
            public void onEditClicked(Plant plant) {
                EditPlantFragment mCategoryFragment = new EditPlantFragment();
                FragmentManager mFragmentManager = getParentFragmentManager();
                mFragmentManager
                        .beginTransaction()
                        .replace(R.id.nav_host_fragment_activity_main, mCategoryFragment, EditPlantFragment.class.getSimpleName())
                        .addToBackStack(null)
                        .commit();
            }

            @Override
            public void onViewClicked(Plant plant) {
                pref.saveSPString(pref.SP_PLANT_ID,plant.getId());
                Intent moveIntent = new Intent(getActivity(), GrowthsPlantActivity.class);
                moveIntent.putExtra(GrowthsPlantActivity.EXTRA_PLANTID,plant.getId());
                startActivity(moveIntent);
            }
        });
    }

}