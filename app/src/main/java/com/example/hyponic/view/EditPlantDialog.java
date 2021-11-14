package com.example.hyponic.view;

import static com.example.hyponic.constant.ApiConstant.BASE_URL;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.hyponic.DummyData.PlantDummyData;
import com.example.hyponic.Home1Activity;
import com.example.hyponic.R;
import com.example.hyponic.databinding.ActivityHome1Binding;
import com.example.hyponic.databinding.DialogEditPlantBinding;
import com.example.hyponic.model.Plant;
import com.example.hyponic.model.SharedPrefManager;
import com.example.hyponic.model.Time;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EditPlantDialog extends AppCompatDialogFragment {
    SharedPrefManager pref;
    DialogEditPlantBinding binding;

    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.dialog_edit_plant);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        pref = new SharedPrefManager(getContext());
        binding = DialogEditPlantBinding.inflate(getLayoutInflater());

        getNamePlant();
        //View view = inflater.inflate(R.layout.dialog_edit_plant,null);
        builder.setView(binding.getRoot())
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("ID PLANT",pref.getSPPlantId());
                        editPlant(binding.insertEditNamePlant.getText().toString());
                        Intent moveIntent = new Intent(getContext(), Home1Activity.class);
                        startActivity(moveIntent);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        binding.insertEditNamePlant.setText("");
                        Intent moveIntent = new Intent(getContext(), Home1Activity.class);
                        startActivity(moveIntent);
                    }
                });
        return builder.create();
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
                            binding.insertEditNamePlant.setHint(name);
                            Log.d("name",name);
                        }catch (JSONException e){
                            e.printStackTrace();
                            binding.insertEditNamePlant.setHint("Data tidak ditemukan");
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
