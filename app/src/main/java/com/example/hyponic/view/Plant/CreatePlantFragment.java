package com.example.hyponic.view.Plant;

import static com.example.hyponic.constant.ApiConstant.BASE_URL;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.hyponic.Home1Activity;
import com.example.hyponic.R;
import com.example.hyponic.databinding.FragmentCreatePlantBinding;
import com.example.hyponic.model.SharedPrefManager;
import com.example.hyponic.view.HomeFragment;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;


public class CreatePlantFragment extends DialogFragment{
    FragmentCreatePlantBinding binding;
    SharedPrefManager pref;
    public CreatePlantFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCreatePlantBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pref = new SharedPrefManager(getContext());
        binding.btnSave.setOnClickListener(v->{
            createPlant();
            getDialog().dismiss();
        });
        binding.btnCancel.setOnClickListener(v->{
            getDialog().cancel();
        });

    }

    private void createPlant() {
        showLoading(true);
        String namePlant = binding.edNamePlat.getText().toString();
        Log.d("Nama Tanaman",""+namePlant);
        AndroidNetworking.post(BASE_URL+"plants")
                .addHeaders("Authorization","Bearer "+pref.getSPToken())
                .addHeaders("Accept", "application/json")
                .addBodyParameter("name", namePlant)
                .setPriority(Priority.MEDIUM)
                .setTag("test")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        showLoading(false);
                        Log.d("TAG", String.valueOf(response));
                        try {
                            if(response.getJSONObject("meta").getString("status").equals("success")){
                                Log.d("Data",""+response.getJSONObject("data"));
                                Toast.makeText(getActivity(),"DATA BERHASIL DISIMPAN",Toast.LENGTH_SHORT).show();
                                Log.d("Data2",""+response.getJSONObject("data"));
                            }else {
                                Toast.makeText(getActivity(),"DATA GAGAl DISIMPAN",Toast.LENGTH_SHORT).show();
                            }

                        }catch (JSONException e){
                            Toast.makeText(getContext(),"Login Gagal"+e,Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        showLoading(false);
                        // handle error
                        Toast.makeText(getContext(),""+error, Toast.LENGTH_SHORT).show();
                        Log.d("ERROR", String.valueOf(error));
                    }
                });
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        Fragment fragment = getParentFragment();
        if (fragment instanceof HomeFragment) {
            HomeFragment homeFragment = (HomeFragment) fragment;
        }
    }
    private void showLoading(Boolean isLoading) {
        if (isLoading) {
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.GONE);
        }
    }
}