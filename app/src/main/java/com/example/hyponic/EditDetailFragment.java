package com.example.hyponic;

import static com.example.hyponic.constant.ApiConstant.BASE_URL;

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
import com.example.hyponic.databinding.FragmentEditDetailBinding;
import com.example.hyponic.model.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

public class EditDetailFragment extends Fragment {

    private FragmentEditDetailBinding binding;
    SharedPrefManager pref;

    public EditDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        pref = new SharedPrefManager(getContext());


        getData();

    }

    public void getData() {

    }

//    public editData() {
//        AndroidNetworking.patch(BASE_URL+"plants/"+pref.getSPPlantId())
//                .addHeaders("Authorization","Bearer "+pref.getSPToken())
//                .addHeaders("Accept", "application/json")
//                .addBodyParameter()
//                .setPriority(Priority.LOW)
//                .build()
//                .getAsJSONObject(new JSONObjectRequestListener(){
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            String status = response.getJSONObject("meta").getString("status");
//                            if(status.equals("success")){
//                                Toast.makeText(getContext(), "Berhasil diperbarui", Toast.LENGTH_SHORT).show();
//                            }else{
//                                Toast.makeText(getContext(), "Gagal diperbarui", Toast.LENGTH_SHORT).show();
//
//                            }
//                        }catch (JSONException e){
//                            e.printStackTrace();
//                        }
//                    }
//                    @Override
//                    public void onError(ANError error) {
//                        Log.d("TAG", "onError: " + error); //untuk log pada onerror
//                    }
//                });
//    }

}