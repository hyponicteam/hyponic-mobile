package com.example.hyponic.view.Plant;

import static com.example.hyponic.constant.ApiConstant.BASE_URL;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.hyponic.R;
import com.example.hyponic.databinding.FragmentEditPlantBinding;
import com.example.hyponic.model.SharedPrefManager;
import com.example.hyponic.view.HomeFragment;

import org.json.JSONException;
import org.json.JSONObject;

public class EditPlantFragment extends DialogFragment {

    private FragmentEditPlantBinding binding;
    SharedPrefManager pref;
    public EditPlantFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEditPlantBinding.inflate(inflater, container, false);
        return  binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pref = new SharedPrefManager(getContext());
        getNamePlant();
        binding.btnSave.setOnClickListener(v->{
            editPlant(binding.edNamePlat.getText().toString());
            getDialog().dismiss();
        });
        binding.btnCancel.setOnClickListener(v->{
            getDialog().cancel();
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
    private void getNamePlant() {
        AndroidNetworking.get(BASE_URL+"plants/"+pref.getSPPlantId())
                .addHeaders("Authorization","Bearer "+pref.getSPToken())
                .addHeaders("Accept", "application/json")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener(){
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String name = response.getJSONObject("data").getString("name");
                            binding.edNamePlat.setHint(name);
                            Log.d("name",name);
                        }catch (JSONException e){
                            e.printStackTrace();
                            binding.edNamePlat.setHint("Data tidak ditemukan");
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        Log.d("TAG", "onError: " + error); //untuk log pada onerror
                    }
                });
    }
    public void editPlant(String name){
        AndroidNetworking.patch(BASE_URL+"plants/"+pref.getSPPlantId())
                .addHeaders("Authorization","Bearer "+pref.getSPToken())
                .addHeaders("Accept", "application/json")
                .addBodyParameter("name",name)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener(){
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getJSONObject("meta").getString("status");
                            if(status.equals("success")){
                                Toast.makeText(getContext(), "Berhasil diperbarui", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getContext(), "Gagal diperbarui", Toast.LENGTH_SHORT).show();

                            }
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
}