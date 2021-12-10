package com.penshyponic.hyponic;

import static com.penshyponic.hyponic.constant.ApiConstant.BASE_URL;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.penshyponic.hyponic.adapter.GrowthsAdapter;
import com.penshyponic.hyponic.databinding.ActivityGrowthsPlantBinding;
import com.penshyponic.hyponic.model.SharedPrefManager;
import com.penshyponic.hyponic.model.Growths;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.logo);

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
        //showGrafik();
    }

    private void showGrafik(ArrayList<Growths> grow) {
        ArrayList<Entry> leaf_width = new ArrayList<>();
        ArrayList<Entry> height_plant = new ArrayList<>();
        ArrayList<Entry> phAir = new ArrayList<>();
        ArrayList<Entry> temperature = new ArrayList<>();

        if(grow.size()!=0){
            for(int i =0; i<grow.size(); i++){
                leaf_width.add(new Entry(i, (float)grow.get(i).getLeaf_widht()));
                height_plant.add(new Entry(i,(float)grow.get(i).getPlant_height()));
                phAir.add(new Entry(i,(float)grow.get(i).getAcidity()));
                temperature.add(new Entry(i,(float)grow.get(i).getTemperature()));
            }
        }else{
            leaf_width.add(new Entry(0, 0f));
            height_plant.add(new Entry(0,0f));
            phAir.add(new Entry(0,0f));
            temperature.add(new Entry(0,0f));
        }

        LineDataSet leafWidthLineDataSet = new LineDataSet(leaf_width, "Lebar Daun");
        leafWidthLineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        leafWidthLineDataSet.setColor(Color.GREEN);
        leafWidthLineDataSet.setCircleRadius(5f);
        leafWidthLineDataSet.setCircleColor(Color.GREEN);

        LineDataSet heightPlantLineDataSet = new LineDataSet(height_plant, "Tinggi Tanaman");
        heightPlantLineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        heightPlantLineDataSet.setColor(Color.GRAY);
        heightPlantLineDataSet.setCircleRadius(5f);
        heightPlantLineDataSet.setCircleColor(Color.GRAY);

        LineDataSet AcidityLineDataSet = new LineDataSet(phAir, "PH Air");
        AcidityLineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        AcidityLineDataSet.setColor(Color.BLUE);
        AcidityLineDataSet.setCircleRadius(5f);
        AcidityLineDataSet.setCircleColor(Color.BLUE);

        LineDataSet TemperatureLineDataSet = new LineDataSet(temperature, "Suhu");
        TemperatureLineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        TemperatureLineDataSet.setColor(Color.RED);
        TemperatureLineDataSet.setCircleRadius(5f);
        TemperatureLineDataSet.setCircleColor(Color.RED);

        //Setup Legend
        binding.lineChart.getLegend().setEnabled(true);
        binding.lineChart.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        binding.lineChart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        binding.lineChart.getLegend().setOrientation(Legend.LegendOrientation.HORIZONTAL);
        binding.lineChart.getLegend().setDrawInside(false);

        binding.lineChart.getDescription().setEnabled(false);
        binding.lineChart.getXAxis().setPosition( XAxis.XAxisPosition.BOTTOM);
//        binding.lineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(){
//            @Override
//            public String getFormattedValue(float value) {
//                //return String.valueOf((int) value);
//                DateFormatter date = new DateFormatter(grow.get((int)value).getDate());
//                return date.getDate();
//            }
//        });
        binding.lineChart.setData(new LineData(leafWidthLineDataSet, heightPlantLineDataSet,AcidityLineDataSet,TemperatureLineDataSet));
        binding.lineChart.animateXY(100,500);

    }

    @Override
    protected  void onResume() {
        super.onResume();
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
                            String namePlant = response.getJSONObject("data").getString("name");
                            binding.namePlant.setText(namePlant.toUpperCase());
                            for(int i=0; i<data.length(); i++){
                                JSONObject jsontabelList = data.getJSONObject(i);
                                Log.d("GROW","ke -"+i+" : "+data.getJSONObject(i));
                                Growths tabellist = new Growths(
                                        jsontabelList.getString("id"),
                                        jsontabelList.getDouble("plant_height"),
                                        jsontabelList.getDouble("leaf_width"),
                                        jsontabelList.getDouble("temperature"),
                                        jsontabelList.getDouble("acidity"),
                                        jsontabelList.getString("created_at"));
                                growths.add(tabellist);
                            }
                            Log.d("SIZEGROW: ",""+growths.size());
                            showGrafik(growths);
                            if(growths.size()==0){
                                binding.rvlisttabeltanaman.setVisibility(View.GONE);
                                binding.cardNotFoundGrowth.setVisibility(View.VISIBLE);
                            }else{
                                showTabelList(growths);
                            }

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        Log.d("TAG", "onError: " + error.getErrorBody()); //untuk log pada onerror
                        binding.rvlisttabeltanaman.setVisibility(View.GONE);
                        binding.cardNotFoundGrowth.setVisibility(View.VISIBLE);
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
                                        jsontabelList.getDouble("plant_height"),
                                        jsontabelList.getDouble("leaf_width"),
                                        jsontabelList.getDouble("temperature"),
                                        jsontabelList.getDouble("acidity"),
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
