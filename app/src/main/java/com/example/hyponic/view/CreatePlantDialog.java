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
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.hyponic.Home1Activity;
import com.example.hyponic.R;
import com.example.hyponic.model.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CreatePlantDialog extends AppCompatDialogFragment {
    EditText etNamePlant;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        SharedPrefManager pref = new SharedPrefManager(getContext());
        View view = inflater.inflate(R.layout.dialog_create_plant,null);
        etNamePlant=view.findViewById(R.id.namePlant);
        String namePlant =etNamePlant.getText().toString();
        builder.setView(view)
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AndroidNetworking.post("plants")
                                .addHeaders("Accept", "application/json")
                                .addHeaders("Authorization", "Bearer "+pref.getSPToken())
                                .addBodyParameter("name",namePlant)
                                .setPriority(Priority.LOW)
                                .build()
                                .getAsJSONObject(new JSONObjectRequestListener() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        try {
                                            if(response.getJSONObject("meta").getString("status").equals("success")){
                                                Toast.makeText(getContext(),response.getJSONObject("meta")
                                                        .getString("message"),Toast.LENGTH_SHORT).show();

                                            }else{
                                                Toast.makeText(getContext(),"Data gagal disimpan",Toast.LENGTH_SHORT).show();
                                            }

                                        }catch (JSONException e){
                                            Toast.makeText(getContext(),"Login Gagal"+e,Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onError(ANError error) {
                                        Log.d("ERROR", String.valueOf(error));
                                    }
                                });

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        etNamePlant.setText("");
                    }
                });


        return builder.create();
    }
}
