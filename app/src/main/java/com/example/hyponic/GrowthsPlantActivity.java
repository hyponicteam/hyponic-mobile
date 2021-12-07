package com.example.hyponic;

import static com.example.hyponic.constant.ApiConstant.BASE_URL;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.Color;
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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

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
        showGrafik();
    }

    private void showGrafik() {
        ArrayList<Entry> kasus = new ArrayList<>();
        kasus.add(new Entry(0F, 149F));
        kasus.add(new Entry(1F, 113F));
        kasus.add(new Entry(2F, 196F));
        kasus.add(new Entry(3F, 106F));
        kasus.add(new Entry(4F, 181F));
        kasus.add(new Entry(5F, 218F));
        kasus.add(new Entry(6F, 247F));
        kasus.add(new Entry(7F, 218F));
        kasus.add(new Entry(8F, 337F));
        kasus.add(new Entry(9F, 219F));

        ArrayList<Entry> sembuh = new ArrayList<>();
        sembuh.add(new Entry(0F, 22F));
        sembuh.add(new Entry(1F, 9F));
        sembuh.add(new Entry(2F, 22F));
        sembuh.add(new Entry(3F, 16F));
        sembuh.add(new Entry(4F, 14F));
        sembuh.add(new Entry(5F, 28F));
        sembuh.add(new Entry(6F, 12F));
        sembuh.add(new Entry(7F, 18F));
        sembuh.add(new Entry(8F, 30F));
        sembuh.add(new Entry(9F, 30F));

        ArrayList<Entry> meninggal = new ArrayList<Entry>();
        meninggal.add(new Entry(0F, 21F));
        meninggal.add(new Entry(1F, 13F));
        meninggal.add(new Entry(2F, 11F));
        meninggal.add(new Entry(3F, 10F));
        meninggal.add(new Entry(4F, 7F));
        meninggal.add(new Entry(5F, 11F));
        meninggal.add(new Entry(6F, 12F));
        meninggal.add(new Entry(7F, 19F));
        meninggal.add(new Entry(8F, 40F));
        meninggal.add(new Entry(9F, 26F));

        LineDataSet kasusLineDataSet = new LineDataSet(kasus, "Suhu");
        kasusLineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        kasusLineDataSet.setColor(Color.BLUE);
        kasusLineDataSet.setCircleRadius(5f);
        kasusLineDataSet.setCircleColor(Color.BLUE);

        LineDataSet sembuhLineDataSet = new LineDataSet(sembuh, "Tinggi Tanaman");
        sembuhLineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        sembuhLineDataSet.setColor(Color.GREEN);
        sembuhLineDataSet.setCircleRadius(5f);
        sembuhLineDataSet.setCircleColor(Color.GREEN);

        LineDataSet meninggalLineDataSet = new LineDataSet(meninggal, "Lebar Daun");
        meninggalLineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        meninggalLineDataSet.setColor(Color.RED);
        meninggalLineDataSet.setCircleRadius(5f);
        meninggalLineDataSet.setCircleColor(Color.RED);

        //Setup Legend
        Legend legend =binding.lineChart.getLegend();
        binding.lineChart.getLegend().setEnabled(true);
        binding.lineChart.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        binding.lineChart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        binding.lineChart.getLegend().setOrientation(Legend.LegendOrientation.HORIZONTAL);
        binding.lineChart.getLegend().setDrawInside(false);

        binding.lineChart.getDescription().setEnabled(false);
        binding.lineChart.getXAxis().setPosition( XAxis.XAxisPosition.BOTTOM);
        binding.lineChart.setData(new LineData(kasusLineDataSet, sembuhLineDataSet, meninggalLineDataSet));
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
