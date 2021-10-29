package com.example.hyponic;

import android.os.Bundle;

import com.example.hyponic.adapter.PlantAdapter;
import com.example.hyponic.model.Plant;
import com.example.hyponic.model.SharedPrefManager;
import com.example.hyponic.view.CreatePlanDialog;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.TextView;

import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hyponic.databinding.ActivityHome1Binding;
import com.example.hyponic.DummyData.PlantDummyData;
import java.util.ArrayList;

public class Home1Activity extends AppCompatActivity {

    private RecyclerView rvPlant;
    private ArrayList<Plant> planList = new ArrayList<>();
    private ActivityHome1Binding binding;
    SharedPrefManager pref;
    private TextView tvusername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHome1Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                // CreatePlanDialog dialog = new CreatePlanDialog();

            }
        });

        pref = new SharedPrefManager(this);
        tvusername=findViewById(R.id.username);
        tvusername.setText(pref.getSPNama());
        rvPlant=findViewById(R.id.rv_NLatestPlant);
        rvPlant.setHasFixedSize(true);
        planList.addAll(PlantDummyData.getListData());
        showRecyclerList();

    }
    private void showRecyclerList() {
        rvPlant.setLayoutManager(new LinearLayoutManager(this));
        PlantAdapter listPlantAdapter = new PlantAdapter(planList);
        rvPlant.setAdapter(listPlantAdapter);
    }

}