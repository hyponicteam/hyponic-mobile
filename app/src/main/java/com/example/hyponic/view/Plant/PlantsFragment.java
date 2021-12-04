package com.example.hyponic.view.Plant;

import static com.example.hyponic.constant.ApiConstant.BASE_URL;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.fonts.Font;
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
import com.example.hyponic.DetailPlantActivity;
import com.example.hyponic.DummyData.GrowthDummyData;
import com.example.hyponic.DummyData.PlantDummyData;
import com.example.hyponic.R;
import com.example.hyponic.adapter.PlantAdapter;
import com.example.hyponic.adapter.TopHeightAdapter;
import com.example.hyponic.adapter.TopWidthAdapter;
import com.example.hyponic.databinding.FragmentPlantsBinding;
import com.example.hyponic.model.Plant;
import com.example.hyponic.model.SharedPrefManager;
import com.example.hyponic.model.Time;
import com.example.hyponic.model.TopGrowth;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PlantsFragment extends Fragment {
    private FragmentPlantsBinding binding;
    SharedPrefManager pref;
    ArrayList<TopGrowth> growths = new ArrayList<>();

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
        growths.addAll(GrowthDummyData.getListData());
        Log.d("DUMMY TOP", String.valueOf(growths));
        showBarChart();
        showTopWidth();
        getTopHeigh();
//        getTopWidth();
    }


    private void showTopWidth() {
        // Data-data yang akan ditampilkan di Chart
        List<BarEntry> dataPemasukan = new ArrayList<BarEntry>();
        for(int i=0; i<growths.size(); i++){
            dataPemasukan.add(new BarEntry(i,(float)growths.get(i).getGrowth_per_day()));
        }

        // Pengaturan atribut bar, seperti warna dan lain-lain
        BarDataSet dataSet1 = new BarDataSet(dataPemasukan, "Pertumbuhan Tanaman");
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

        // Diubah menjadi integer, kemudian dijadikan String
        // Ini berfungsi untuk menghilankan koma, dan tanda ribuah pada tahun
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                //return String.valueOf((int) value);
                return growths.get((int)value).getName();
            }
        });

        //Menghilangkan sumbu Y yang ada di sebelah kanan
        binding.barTopWidth.getAxisRight().setEnabled(false);
        binding.barTopWidth.setFitBars(true);
        binding.barTopWidth.setData(barData);
        binding.barTopWidth.getDescription().setEnabled(false);
        binding.barTopWidth.animateY(2000);
    }

    private void showBarChart() {
        // Data-data yang akan ditampilkan di Chart
        List<BarEntry> dataPemasukan = new ArrayList<BarEntry>();
       for(int i=0; i<growths.size(); i++){
           dataPemasukan.add(new BarEntry(i,(float)growths.get(i).getGrowth_per_day()));
       }

        // Pengaturan atribut bar, seperti warna dan lain-lain
        BarDataSet dataSet1 = new BarDataSet(dataPemasukan, "Pertumbuhan Tanaman");
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

        // Diubah menjadi integer, kemudian dijadikan String
        // Ini berfungsi untuk menghilankan koma, dan tanda ribuah pada tahun
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                //return String.valueOf((int) value);
                return growths.get((int)value).getName();
            }
        });

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
        ArrayList<TopGrowth> growths = new ArrayList<>();
        AndroidNetworking.get(BASE_URL+"top-plants?category=plant_height&n=1")
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
                                growths.add(growth);
                            }
                            Log.d("SIZE: ",""+growths.size());

                        }catch (JSONException e){
                           e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        showLoading(false);
                        Log.d("TOP HEIGHT", "onError: " + error); //untuk log pada onerror
                    }
                });
    }
//    private void getTopWidth() {
//        ArrayList<TopGrowth> growths = new ArrayList<>();
//        AndroidNetworking.get(BASE_URL+"top-plants?category=leaf_width&n=1")
//                .addHeaders("Authorization","Bearer "+pref.getSPToken())
//                .addHeaders("Accept", "application/json")
//                .setPriority(Priority.LOW)
//                .build()
//                .getAsJSONObject(new JSONObjectRequestListener(){
//
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        showLoading(false);
//                        Log.d("TOP WIDTH", "respon: " + response);
//                        try {
//                            //Log.d("TAG", "respon: " + response.getJSONArray("data"));
//                            JSONArray data = response.getJSONArray("data");
//                            for(int i=0; i<data.length(); i++){
//                                JSONObject jsonGrowth = data.getJSONObject(i);
//                                Log.d("TAG","ke -"+i+" : "+data.getJSONObject(i));
//
//                                TopGrowth growth = new TopGrowth();
//                                growth.setName(jsonGrowth.getString("name"));
//                                growth.setGrowth_per_day(jsonGrowth.getJSONObject("growth").getDouble("growth_per_day"));
//                                growth.setUnit(jsonGrowth.getJSONObject("growth").getString("unit"));
//                                growths.add(growth);
//                            }
//                            Log.d("SIZE: ",""+growths.size());
////                            showListGrowthWidth(growths);
//
//                        }catch (JSONException e){
//                            e.printStackTrace();
//                        }
//                    }
//                    @Override
//                    public void onError(ANError error) {
//                        showLoading(false);
//                        Log.d("TOP WIDTH", "onError: " + error); //untuk log pada onerror
//                    }
//                });
//    }
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
                Intent moveIntent = new Intent(getActivity(), DetailPlantActivity.class);
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