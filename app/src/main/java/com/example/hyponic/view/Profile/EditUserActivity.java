package com.example.hyponic.view.Profile;

import static com.example.hyponic.constant.ApiConstant.BASE_URL;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.hyponic.Home1Activity;
import com.example.hyponic.R;
import com.example.hyponic.databinding.ActivityEditUserBinding;
import com.example.hyponic.model.SharedPrefManager;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

public class EditUserActivity extends AppCompatActivity {

    private ActivityEditUserBinding binding;
    SharedPrefManager pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        pref = new SharedPrefManager(this);
        
        getDataUser();
    }

    private void getDataUser() {
        AndroidNetworking.post(BASE_URL+"auth/user")
                .addHeaders("Authorization","Bearer "+pref.getSPToken())
                .addHeaders("Accept", "application/json")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getJSONObject("meta").getString("status").equals("success")){
                                JSONObject data = response.getJSONObject("data");
                                binding.edName.setHint(data.getString("name"));
                                binding.edEmail.setHint(data.getString("email"));
                            }else {

                            }

                        }catch (JSONException e){
                            Toast.makeText(getApplicationContext(),"Login Gagal"+e,Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onError(ANError error) {

                        Toast.makeText(getApplicationContext(),""+error, Toast.LENGTH_SHORT).show();
                        Log.d("ERROR", String.valueOf(error));
                    }
                });
    }
}