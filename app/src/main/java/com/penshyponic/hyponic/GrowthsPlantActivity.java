package com.penshyponic.hyponic;

import static com.penshyponic.hyponic.constant.ApiConstant.BASE_URL;
import static com.penshyponic.hyponic.model.CreateGrowthHistory.history;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.penshyponic.hyponic.adapter.GrowthsAdapter;
import com.penshyponic.hyponic.databinding.ActivityGrowthsPlantBinding;
import com.penshyponic.hyponic.model.GrowthHistory;
import com.penshyponic.hyponic.model.Plant;
import com.penshyponic.hyponic.model.SharedPrefManager;
import com.penshyponic.hyponic.model.Growths;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.penshyponic.hyponic.model.TopGrowth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class GrowthsPlantActivity extends AppCompatActivity {

    public static final String EXTRA_PLANTID = "extra_plantID";
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
        getTopHeigh();
        getTopWidth();

        binding.btnAddPlant.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            String nowDate = calendar.get(Calendar.YEAR)+"-"+calendar.get(Calendar.MONTH)+"-"+calendar.get(Calendar.DATE);
            boolean flag =false;
            for(int i=0; i<history.size(); i++){
                GrowthHistory histo = history.get(i);
                if(histo.getPlantId().equals(pref.getSPPlantId()) && histo.getTimeCreated().equals(nowDate)){
                    flag=true;
                }
            }
            if(flag || pref.getSP_Create_Growth().equals(getIntent().getStringExtra(EXTRA_PLANTID)+nowDate)){
                Toast.makeText(getApplicationContext(), "Maaf anda dapat menginputkan data lagi esok hari", Toast.LENGTH_SHORT).show();
            }else{
                Intent moveIntent = new Intent(this, CreateGrowthsActivity.class);
                moveIntent.putExtra(CreateGrowthsActivity.EXTRA_PLANTID,getIntent().getStringExtra(EXTRA_PLANTID));
                startActivity(moveIntent);
            }
        }
        );

        //getTabelGrowth();
    }

    private void getTopHeigh() {
        Log.d("TOKEN TOP HEIGHT", "respon: " + pref.getSPToken());
        ArrayList<TopGrowth> topHeightgrowths = new ArrayList<>();
        AndroidNetworking.get(BASE_URL+"top-growths?category=plant_height&n=3&plant_id="+pref.getSPPlantId())
                .addHeaders("Authorization","Bearer "+pref.getSPToken())
                .addHeaders("Accept", "application/json")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener(){
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("TOP HEIGHT GROW", "respon: " + response);
                        try {

                            JSONArray data = response.getJSONArray("data");
                            for(int i=0; i<data.length(); i++){
                                JSONObject jsonGrowth = data.getJSONObject(i);
                                Log.d("TAG","ke -"+i+" : "+data.getJSONObject(i));

                                TopGrowth growth = new TopGrowth();
                                growth.setGrowth_per_day(jsonGrowth.getJSONObject("growth").getDouble("growth_per_day"));
                                growth.setUnit(jsonGrowth.getJSONObject("growth").getString("unit"));
                                topHeightgrowths.add(growth);
                            }
                            Log.d("SIZE: ",""+topHeightgrowths.size());
                            if(topHeightgrowths.size()==0){
                                binding.topHeight.setVisibility(View.GONE);
                                binding.notFoundGrowthIsight.setText("Butuh minimal dua data pantauan untuk mendapatkan insight pertumbuhan!");
                                //binding.cardNotFoundGrowthIsight.setVisibility(View.VISIBLE);
                            }else{
                                showTopHeight(topHeightgrowths);
                            }

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        Log.d("TOP HEIGHT GROW", "onError: " + error.getErrorBody()); //untuk log pada onerror
                        binding.topHeight.setVisibility(View.GONE);
                        binding.notFoundGrowthIsight.setText("Butuh minimal dua data pantauan untuk mendapatkan insight pertumbuhan!");
                        //binding.cardNotFoundGrowthIsight.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void getTopWidth() {
        Log.d("TOKEN TOP WIDTH", "respon: " + pref.getSPToken());
        ArrayList<TopGrowth> topWidthGrow = new ArrayList<>();
        AndroidNetworking.get(BASE_URL+"top-growths?category=leaf_width&n=3&plant_id="+pref.getSPPlantId())
                .addHeaders("Authorization","Bearer "+pref.getSPToken())
                .addHeaders("Accept", "application/json")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener(){
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("TOP WIDTH GROW", "respon: " + response);
                        try {

                            JSONArray data = response.getJSONArray("data");
                            for(int i=0; i<data.length(); i++){
                                JSONObject jsonGrowth = data.getJSONObject(i);
                                Log.d("TAG","ke -"+i+" : "+data.getJSONObject(i));

                                TopGrowth growth = new TopGrowth();
                                growth.setGrowth_per_day(jsonGrowth.getJSONObject("growth").getDouble("growth_per_day"));
                                growth.setUnit(jsonGrowth.getJSONObject("growth").getString("unit"));
                                topWidthGrow.add(growth);
                            }
                            Log.d("SIZE: ",""+topWidthGrow.size());
                            if(topWidthGrow.size()==0){
                                binding.notFoundGrowthIsight.setText("Butuh minimal dua data pantauan untuk mendapatkan insight pertumbuhan!");
                                //binding.cardNotFoundGrowthIsight.setVisibility(View.VISIBLE);
                            }

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        Log.d("TOP WIDTH GROW", "onError: " + error.getErrorBody()); //untuk log pada onerror
                        //binding..setVisibility(View.GONE);
                        binding.notFoundGrowthIsight.setText("Butuh minimal dua data pantauan untuk mendapatkan insight pertumbuhan!");
                        //binding.cardNotFoundGrowthIsight.setVisibility(View.VISIBLE);
                    }
                });
    }
    private void showTopHeight(ArrayList<TopGrowth> top) {
        // Data-data yang akan ditampilkan di Chart
        List<BarEntry> heightData = new ArrayList<BarEntry>();
        if(top.size()!=0){
            binding.topHeight.setMinimumHeight(400);
            for(int i=0; i<top.size(); i++){
                heightData.add(new BarEntry(i,(float)top.get(i).getGrowth_per_day()));
            }
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

        // Diubah menjadi integer, kemudian dijadikan String
        // Ini berfungsi untuk menghilankan koma, dan tanda ribuah pada tahun
        xAxis.setValueFormatter(new IndexAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                //return String.valueOf((int) value);
                return top.get((int)value).getName();
            }
        });

        //Menghilangkan sumbu Y yang ada di sebelah kanan
        binding.topHeight.getAxisRight().setEnabled(false);
        binding.topHeight.setFitBars(true);
        binding.topHeight.setData(barData);
        binding.topHeight.getDescription().setEnabled(false);
        binding.topHeight.animateY(2000);
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
        AndroidNetworking.get(BASE_URL+"plants/"+getIntent().getStringExtra(EXTRA_PLANTID))
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
                                tabellist.setPlant_id(jsontabelList.getString("plant_id"));
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
            public void onEditClicked(Growths growth) {
                //pref.saveSPString(pref.SP_GROWTH_ID, growth.getId());
                Intent moveIntent = new Intent (GrowthsPlantActivity.this, EditGrowthsActivity.class);
                moveIntent.putExtra(EditGrowthsActivity.EXTRA_GROWTH_ID,growth.getId());
                moveIntent.putExtra(EditGrowthsActivity.EXTRA_PLANTID,growth.getPlant_id());
                startActivity(moveIntent);
            }

            @Override
            public void onDeleteClicked(Growths growth) {
                pref.saveSPString(pref.SP_GROWTH_ID, growth.getId());
                DeleteGrowthsFragment deleteDetailPlantfragment = new DeleteGrowthsFragment();
                FragmentManager mFragmentManager = getSupportFragmentManager();
                Bundle mBundle = new Bundle();
                mBundle.putString(DeleteGrowthsFragment.EXTRA_PLANTID,growth.getPlant_id());
                mBundle.putString(DeleteGrowthsFragment.EXTRA_GROWTH_ID,growth.getId());
                deleteDetailPlantfragment.setArguments(mBundle);
                deleteDetailPlantfragment.show(mFragmentManager, DeleteGrowthsFragment.class.getSimpleName());
                ;
            }
        });
    }
}
