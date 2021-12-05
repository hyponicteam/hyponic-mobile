package com.example.hyponic;

import static com.example.hyponic.constant.ApiConstant.BASE_URL;

import android.content.Intent;
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
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.hyponic.databinding.FragmentDeleteDetailPlantBinding;
import com.example.hyponic.databinding.FragmentDetailplantfragmentBinding;
import com.example.hyponic.model.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;


public class DeleteDetailPlantFragment extends DialogFragment {

    private FragmentDeleteDetailPlantBinding binding;
    SharedPrefManager pref;

    public DeleteDetailPlantFragment() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentDeleteDetailPlantBinding.inflate(inflater,container,false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pref = new SharedPrefManager(getContext());

        binding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteDetailPlant();
                dismiss();
//                Intent moveIntent = new Intent(getActivity(), DetailPlantActivity.class);
//                startActivity(moveIntent);
            }
        });

    }

    public void deleteDetailPlant(){
        AndroidNetworking.delete(BASE_URL+"growths/"+pref.getSpGrowthId())
                .addHeaders("Authorization","Bearer "+pref.getSPToken())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("TAG", String.valueOf(response));
                        try {
                            if(response.getJSONObject("meta").getString("status").equals("success")){
                                Toast.makeText(getContext(),"Berhasil dihapus",Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getContext(), "Tanaman Gagal Dihapus",Toast.LENGTH_SHORT).show();
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

}