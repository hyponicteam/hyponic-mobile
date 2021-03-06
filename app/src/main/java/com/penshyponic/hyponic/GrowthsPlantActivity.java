package com.penshyponic.hyponic;

import static com.penshyponic.hyponic.constant.ApiConstant.BASE_URL;
import static com.penshyponic.hyponic.model.CreateGrowthHistory.history;

import androidx.activity.OnBackPressedCallback;
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
import com.penshyponic.hyponic.model.DateFormatter;
import com.penshyponic.hyponic.model.GrowthHistory;
import com.penshyponic.hyponic.model.SharedPrefManager;
import com.penshyponic.hyponic.model.Growths;
import com.github.mikephil.charting.components.XAxis;
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

        Log.d("TOKEN",pref.getSPToken());
        Log.d("PLANT ID",getIntent().getStringExtra(EXTRA_PLANTID));
        binding.btnAddPlant.setOnClickListener(view -> {
            cekAvailableToCreate();
        }
        );

    }

    private void cekAvailableToCreate() {
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

    private void getTopHeigh() {
        Log.d("TOKEN TOP HEIGHT", "respon: " + pref.getSPToken());
        ArrayList<TopGrowth> topHeightgrowths = new ArrayList<>();
        AndroidNetworking.get(BASE_URL+"top-growths?category=plant_height&n=1&plant_id="+pref.getSPPlantId())
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
                            Log.d("DATA TOP HEIGHT",""+data);
                            if(topHeightgrowths.size()==0){

                            }else{

                            }

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        Log.d("TOP HEIGHT GROW", "onError: " + error.getErrorBody()); //untuk log pada onerror

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
                            if(growths.size()==0){
                                binding.cardNoGrowthData.getRoot().setVisibility(View.VISIBLE);
                                binding.rvlisttabeltanaman.setVisibility(View.GONE);
                                binding.height.setVisibility(View.GONE);
                                binding.leafWidth.setVisibility(View.GONE);
                                binding.temperature.setVisibility(View.GONE);
                                binding.acidity.setVisibility(View.GONE);
                                binding.cardNotFoundGrowth.getRoot().setVisibility(View.VISIBLE);
                            }else{
                                showTabelGrowth(growths);
                                showHeight(growths);
                                showLeafWidth(growths);
                                showTemperature(growths);
                                showAcidity(growths);
                                                            }

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        Log.d("TAG", "onError: " + error.getErrorBody()); //untuk log pada onerror
                        binding.rvlisttabeltanaman.setVisibility(View.GONE);
                        binding.height.setVisibility(View.GONE);
                        binding.leafWidth.setVisibility(View.GONE);
                        binding.temperature.setVisibility(View.GONE);
                        binding.acidity.setVisibility(View.GONE);
                        binding.cardNotFoundGrowth.getRoot().setVisibility(View.VISIBLE);
                        binding.cardNoGrowthData.getRoot().setVisibility(View.VISIBLE);

                    }
                });
    }

    private void showTabelGrowth(ArrayList<Growths> tabellist ){
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
    private void showHeight(ArrayList<Growths> grow) {
        List<BarEntry> heightData = new ArrayList<BarEntry>();
        String tgl[] = new String[grow.size()];
        if(grow.size()!=0){
            binding.height.setMinimumHeight(400);
            for(int i=0; i<grow.size(); i++){
                heightData.add(new BarEntry(i,(float)grow.get(i).getPlant_height()));
                DateFormatter date = new DateFormatter(grow.get(i).getDate(),' ');
                tgl[i]=date.getAfter_separated().substring(5);
                tgl[i]=tgl[i].replace('-','/');
            }
        }else{
            heightData.add(new BarEntry(0,0f));
        }


        // Pengaturan atribut bar, seperti warna dan lain-lain
        BarDataSet dataSet1 = new BarDataSet(heightData, "Tinggi Tanaman (cm) ");
        dataSet1.setColor(ColorTemplate.rgb("#FF38AC91"));
        dataSet1.setValueTextColor(Color.BLACK);
        dataSet1.setValueTextSize(16f);

        // Membuat Bar data yang akan di set ke Chart
        BarData barData = new BarData(dataSet1);
        // Pengaturan sumbu X
        XAxis xAxis = binding.height.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM.BOTTOM);

        // Agar ketika di zoom tidak menjadi pecahan
        xAxis.setGranularity(1f);

        // Diubah menjadi integer, kemudian dijadikan String
        // Ini berfungsi untuk menghilankan koma, dan tanda ribuah pada tahun
        xAxis.setValueFormatter(new IndexAxisValueFormatter(tgl));

        //Menghilangkan sumbu Y yang ada di sebelah kanan
        binding.height.getAxisRight().setEnabled(false);
        binding.height.setFitBars(true);
        binding.height.setData(barData);
        binding.height.getDescription().setEnabled(false);
        binding.height.animateY(2000);
    }

    private void showLeafWidth(ArrayList<Growths> grow) {
        // Data-data yang akan ditampilkan di Chart
        List<BarEntry> heightData = new ArrayList<>();
        String tgl[] = new String[grow.size()];
        if(grow.size()!=0){
            binding.leafWidth.setMinimumHeight(400);
            for(int i=0; i<grow.size(); i++){
                heightData.add(new BarEntry(i,(float)grow.get(i).getLeaf_widht()));
                DateFormatter date = new DateFormatter(grow.get(i).getDate(),' ');
                tgl[i]=date.getAfter_separated().substring(5);
                tgl[i]=tgl[i].replace('-','/');
            }
        }else{
            heightData.add(new BarEntry(0,0f));
        }

        BarDataSet dataSet1 = new BarDataSet(heightData, "Lebar Daun (cm) ");
        dataSet1.setColor(ColorTemplate.rgb("#FF38AC91"));
        dataSet1.setValueTextColor(Color.BLACK);
        dataSet1.setValueTextSize(16f);

        BarData barData = new BarData(dataSet1);
        XAxis xAxis = binding.leafWidth.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM.BOTTOM);

        xAxis.setGranularity(1f);

        // Diubah menjadi integer, kemudian dijadikan String
        // Ini berfungsi untuk menghilankan koma, dan tanda ribuah pada tahun
        xAxis.setValueFormatter(new IndexAxisValueFormatter(tgl));

        //Menghilangkan sumbu Y yang ada di sebelah kanan
        binding.leafWidth.getAxisRight().setEnabled(false);
        binding.leafWidth.setFitBars(true);
        binding.leafWidth.setData(barData);
        binding.leafWidth.getDescription().setEnabled(false);
        binding.leafWidth.animateY(2000);
    }

    public void showTemperature(ArrayList<Growths> grow) {
        // Data-data yang akan ditampilkan di Chart
        List<BarEntry> heightData = new ArrayList<>();
        String tgl[] = new String[grow.size()];
        if(grow.size()!=0){
            binding.temperature.setMinimumHeight(400);
            for(int i=0; i<grow.size(); i++){
                heightData.add(new BarEntry(i,(float)grow.get(i).getTemperature()));
                DateFormatter date = new DateFormatter(grow.get(i).getDate(),' ');
                tgl[i]=date.getAfter_separated().substring(5);
                tgl[i]=tgl[i].replace('-','/');
            }
        }else{
            heightData.add(new BarEntry(0,0f));
        }


        // Pengaturan atribut bar, seperti warna dan lain-lain
        BarDataSet dataSet1 = new BarDataSet(heightData, "Suhu (C) ");
        dataSet1.setColor(ColorTemplate.rgb("#FF38AC91"));
        dataSet1.setValueTextColor(Color.BLACK);
        dataSet1.setValueTextSize(16f);

        // Membuat Bar data yang akan di set ke Chart
        BarData barData = new BarData(dataSet1);
        // Pengaturan sumbu X
        XAxis xAxis = binding.temperature.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM.BOTTOM);

        // Agar ketika di zoom tidak menjadi pecahan
        xAxis.setGranularity(1f);

        xAxis.setValueFormatter(new IndexAxisValueFormatter(tgl));

        //Menghilangkan sumbu Y yang ada di sebelah kanan
        binding.temperature.getAxisRight().setEnabled(false);
        binding.temperature.setFitBars(true);
        binding.temperature.setData(barData);
        binding.temperature.getDescription().setEnabled(false);
        binding.temperature.animateY(2000);
    }
    private void showAcidity(ArrayList<Growths> grow) {

        List<BarEntry> heightData = new ArrayList<BarEntry>();
        String tgl[] = new String[grow.size()];
        if(grow.size()!=0){
            binding.acidity.setMinimumHeight(400);
            for(int i=0; i<grow.size(); i++){
                heightData.add(new BarEntry(i,(float)grow.get(i).getAcidity()));
                DateFormatter date = new DateFormatter(grow.get(i).getDate(),' ');
                tgl[i]=date.getAfter_separated().substring(5);
                tgl[i]=tgl[i].replace('-','/');
            }
        }else{
            heightData.add(new BarEntry(0,0f));
        }

        BarDataSet dataSet1 = new BarDataSet(heightData, "PH Air");
        dataSet1.setColor(ColorTemplate.rgb("#FF38AC91"));
        dataSet1.setValueTextColor(Color.BLACK);
        dataSet1.setValueTextSize(16f);

        BarData barData = new BarData(dataSet1);

        XAxis xAxis = binding.acidity.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM.BOTTOM);

        xAxis.setGranularity(1f);

        xAxis.setValueFormatter(new IndexAxisValueFormatter(tgl));

        binding.acidity.getAxisRight().setEnabled(false);
        binding.acidity.setFitBars(true);
        binding.acidity.setData(barData);
        binding.acidity.getDescription().setEnabled(false);
        binding.acidity.animateY(2000);
    }

}
