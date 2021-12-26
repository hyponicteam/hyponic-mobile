package com.penshyponic.hyponic.view.Plant;

import static com.penshyponic.hyponic.constant.ApiConstant.BASE_URL;

import android.content.Intent;
import android.graphics.Color;
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
import com.penshyponic.hyponic.GrowthsPlantActivity;
import com.penshyponic.hyponic.DummyData.GrowthDummyData;
import com.penshyponic.hyponic.R;
import com.penshyponic.hyponic.adapter.PlantAdapter;
import com.penshyponic.hyponic.databinding.FragmentPlantsBinding;
import com.penshyponic.hyponic.model.Plant;
import com.penshyponic.hyponic.model.SharedPrefManager;
import com.penshyponic.hyponic.model.Time;
import com.penshyponic.hyponic.model.TopGrowth;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PlantsFragment extends Fragment {
    private FragmentPlantsBinding binding;
    SharedPrefManager pref;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentPlantsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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


    private void showTopWidth(ArrayList<TopGrowth> top) {
        // Data-data yang akan ditampilkan di Chart
        List<BarEntry> widthData = new ArrayList<BarEntry>();
        String names[] = new String[top.size()];
        if(top.size()!=0){
            binding.barTopWidth.setMinimumHeight(400);
            for(int i=0; i<top.size(); i++){
                names[i]=top.get(i).getName();
                widthData.add(new BarEntry(i,(float)top.get(i).getGrowth_per_day()));
            }
        }else{
            widthData.add(new BarEntry(0,0f));
        }

        // Pengaturan atribut bar, seperti warna dan lain-lain
        BarDataSet dataSet1 = new BarDataSet(widthData, "Pertumbuhan Lebar Daun "+top.get(0).getUnit());
        dataSet1.setColor(ColorTemplate.JOYFUL_COLORS[1]);
        dataSet1.setValueTextColor(Color.BLACK);
        dataSet1.setValueTextSize(16f);

        // Membuat Bar data yang akan di set ke Chart
        BarData barData = new BarData(dataSet1);
        // Pengaturan sumbu X
        XAxis xAxis = binding.barTopWidth.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM.BOTTOM);

        // Agar ketika di zoom tidak menjadi pecahan
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(names));
        //Menghilangkan sumbu Y yang ada di sebelah kanan
        binding.barTopWidth.getAxisRight().setEnabled(false);
        binding.barTopWidth.setFitBars(true);
        binding.barTopWidth.setData(barData);
        binding.barTopWidth.getDescription().setEnabled(false);
        binding.barTopWidth.animateY(2000);
    }

    private void showTopHeight(ArrayList<TopGrowth> top) {
        // Data-data yang akan ditampilkan di Chart
        List<BarEntry> heightData = new ArrayList<BarEntry>();
        String nama[] = new String[top.size()];
        if(top.size()!=0){
            binding.topHeight.setMinimumHeight(400);
            for(int i=0; i<top.size(); i++){
                heightData.add(new BarEntry(i,(float)top.get(i).getGrowth_per_day()));
                nama[i]=top.get(i).getName();
            }
        }else{
            heightData.add(new BarEntry(0,0f));
        }


        // Pengaturan atribut bar, seperti warna dan lain-lain
        BarDataSet dataSet1 = new BarDataSet(heightData, "Pertumbuhan Tinggi Tanaman "+top.get(0).getUnit());
        dataSet1.setColor(ColorTemplate.JOYFUL_COLORS[1]);
        dataSet1.setValueTextColor(Color.BLACK);
        dataSet1.setValueTextSize(16f);

        // Membuat Bar data yang akan di set ke Chart
        BarData barData = new BarData(dataSet1);
        // Pengaturan sumbu X
        XAxis xAxis = binding.topHeight.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM.BOTTOM);

        // Agar ketika di zoom tidak menjadi pecahan
        xAxis.setGranularity(1f);
        xAxis.setAxisMaximum(0);
        xAxis.setAxisMaximum(0+(1*3));

        // Diubah menjadi integer, kemudian dijadikan String
        // Ini berfungsi untuk menghilankan koma, dan tanda ribuah pada tahun
        xAxis.setValueFormatter(new IndexAxisValueFormatter(nama));

        //Menghilangkan sumbu Y yang ada di sebelah kanan
        binding.topHeight.getAxisRight().setEnabled(false);
        binding.topHeight.setFitBars(true);
        binding.topHeight.setData(barData);
        binding.topHeight.getDescription().setEnabled(false);
        binding.topHeight.animateY(2000);
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
                            if(data.length()==0){
                                showNotFound(true);
                            }else{
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
                        showNotFound(true);
                    }
                });
    }
    private void getTopHeigh() {
        Log.d("TOKEN TOP HEIGHT", "respon: " + pref.getSPToken());
        ArrayList<TopGrowth> topHeightgrowths = new ArrayList<>();
        AndroidNetworking.get(BASE_URL+"top-plants?category=plant_height&n=3")
                .addHeaders("Authorization","Bearer "+pref.getSPToken())
                .addHeaders("Accept", "application/json")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener(){
                    @Override
                    public void onResponse(JSONObject response) {
                        showLoading(false);
                        Log.d("TOP HEIGT", "respon: " + response);
                        try {

                            JSONArray data = response.getJSONArray("data");
                            for(int i=0; i<data.length(); i++){
                                JSONObject jsonGrowth = data.getJSONObject(i);
                                Log.d("TAG","ke -"+i+" : "+data.getJSONObject(i));

                                TopGrowth growth = new TopGrowth();
                                growth.setName(jsonGrowth.getString("name"));
                                growth.setGrowth_per_day(jsonGrowth.getJSONObject("growth").getDouble("growth_per_day"));
                                growth.setUnit(jsonGrowth.getJSONObject("growth").getString("unit"));
                                topHeightgrowths.add(growth);
                            }
                            Log.d("SIZE: ",""+topHeightgrowths.size());
                            showTopHeight(topHeightgrowths);


                        }catch (JSONException e){
                           e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        showLoading(false);
                        Log.d("TOP HEIGHT", "onError: " + error.getErrorBody()); //untuk log pada onerror
                        binding.topHeight.setVisibility(View.GONE);
                        binding.notFoundTopHeight.setText("Butuh minimal 2 data perkembangan per tanaman untuk mendapatkan insight pertumbuhan!");
                        binding.cardNotFoundTopHeight.setVisibility(View.VISIBLE);

                    }
                });
    }
    private void getTopWidth() {
        ArrayList<TopGrowth> topWidth = new ArrayList<>();
        AndroidNetworking.get(BASE_URL+"top-plants?category=leaf_width&n=3")
                .addHeaders("Authorization","Bearer "+pref.getSPToken())
                .addHeaders("Accept", "application/json")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener(){

                    @Override
                    public void onResponse(JSONObject response) {
                        showLoading(false);
                        Log.d("TOP WIDTH", "respon: " + response);
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
                                topWidth.add(growth);
                            }
                            Log.d("SIZE: ",""+topWidth.size());
                            showTopWidth(topWidth);


                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        showLoading(false);
                        Log.d("TOP HEIGHT", "onError: " + error.getErrorBody()); //untuk log pada onerror
                        binding.barTopWidth.setVisibility(View.GONE);
                        binding.notFoundTopWeight.setText("Butuh minimal 2 data perkembangan per tanaman untuk mendapatkan insight pertumbuhan!");
                        binding.cardNotFoundTopWeight.setVisibility(View.VISIBLE);

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
    private void showNotFound(Boolean isAnyData){
        if(isAnyData){
            binding.notFound.setVisibility(View.VISIBLE);
        }else{
            binding.notFound.setVisibility(View.GONE);
        }
    }
}