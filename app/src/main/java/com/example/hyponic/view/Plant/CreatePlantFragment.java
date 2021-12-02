package com.example.hyponic.view.Plant;

import static com.example.hyponic.constant.ApiConstant.BASE_URL;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.hyponic.MainActivity;
import com.example.hyponic.databinding.FragmentCreatePlantBinding;
import com.example.hyponic.model.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;


public class CreatePlantFragment extends Fragment{
    FragmentCreatePlantBinding binding;
    SharedPrefManager pref;
    public CreatePlantFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreatePlantBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pref = new SharedPrefManager(getContext());
        binding.btnSave.setOnClickListener(v->{
            createPlant();
        });
        binding.btnCancel.setOnClickListener(v->{
            backToMainActivity();
        });

    }


    private void createPlant() {

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

                        Log.d("TAG", String.valueOf(response));
                        try {
                            if(response.getJSONObject("meta").getString("status").equals("success")){
                                Log.d("New Plant",""+response.getJSONObject("data"));
                                Toast.makeText(getContext(),"DATA BERHASIL DISIMPAN",Toast.LENGTH_SHORT).show();
                                backToMainActivity();
                            }else {
                                Toast.makeText(getContext(),"DATA GAGAl DISIMPAN",Toast.LENGTH_SHORT).show();
                            }

                        }catch (JSONException e){
                            Toast.makeText(getContext(),"Login Gagal"+e,Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onError(ANError error) {

                        // handle error
                        Toast.makeText(getContext(),""+error, Toast.LENGTH_SHORT).show();
                        Log.d("ERROR", String.valueOf(error));
                    }
                });
    }
    private void backToMainActivity() {
        Intent moveIntent = new Intent(getActivity(), MainActivity.class);
        startActivity(moveIntent);
    }
    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void onAttach(Context context) {
        super.onAttach(context);
    }

}