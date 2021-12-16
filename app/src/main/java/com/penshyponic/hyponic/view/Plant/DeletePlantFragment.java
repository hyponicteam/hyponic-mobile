package com.penshyponic.hyponic.view.Plant;

import static com.penshyponic.hyponic.constant.ApiConstant.BASE_URL;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.penshyponic.hyponic.MainActivity;
import com.penshyponic.hyponic.databinding.FragmentDeletePlantBinding;
import com.penshyponic.hyponic.model.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

public class DeletePlantFragment extends DialogFragment {

    FragmentDeletePlantBinding binding;
    SharedPrefManager pref;
    public DeletePlantFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDeletePlantBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pref= new SharedPrefManager(getContext());
        binding.btnDelete.setOnClickListener(v->{
            deletePlant();
            //getDialog().dismiss();
            backToMainActivity();
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
    }
    public void deletePlant(){
        AndroidNetworking.delete(BASE_URL+"plants/"+pref.getSPPlantId())
                .addHeaders("Authorization","Bearer "+pref.getSPToken())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("TAG", String.valueOf(response));
                        try {
                            if(response.getJSONObject("meta").getString("status").equals("success")){
                                Toast.makeText(getParentFragment().getContext(),"Berhasil dihapus",Toast.LENGTH_SHORT).show();

                            }else {
                                Toast.makeText(getParentFragment().getContext(), "Tanaman Gagal Dihapus",Toast.LENGTH_SHORT).show();
                            }

                        }catch (JSONException e){
                            Toast.makeText(getParentFragment().getContext(),"Login Gagal"+e,Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Toast.makeText(getParentFragment().getContext(),"Pastikan tanaman anda tidak memiliki data pantauan", Toast.LENGTH_SHORT).show();
                        Log.d("ERROR", error.getErrorBody());
                    }
                });

    }
    private void backToMainActivity() {
        Intent moveIntent = new Intent(getActivity(), MainActivity.class);
        startActivity(moveIntent);
    }
}