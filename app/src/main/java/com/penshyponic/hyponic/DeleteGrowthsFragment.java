package com.penshyponic.hyponic;

import static com.penshyponic.hyponic.constant.ApiConstant.BASE_URL;

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
import com.penshyponic.hyponic.databinding.FragmentDeleteGrowthsBinding;
import com.penshyponic.hyponic.model.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;


public class DeleteGrowthsFragment extends DialogFragment {

    private FragmentDeleteGrowthsBinding binding;
    SharedPrefManager pref;

    public DeleteGrowthsFragment() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentDeleteGrowthsBinding.inflate(inflater,container,false);
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
//                Intent moveIntent = new Intent(getActivity(), GrowthsPlantActivity.class);
//                startActivity(moveIntent);
            }
        });
        binding.btnCancel.setOnClickListener(v->{
            dismiss();
        });

    }

    public void deleteDetailPlant(){
        AndroidNetworking.delete(BASE_URL+"growths/"+pref.getSPGrowthsId())
                .addHeaders("Authorization","Bearer "+pref.getSPToken())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("TAG", String.valueOf(response));
                        try {
                            if(response.getJSONObject("meta").getString("status").equals("success")){
                                //Toast.makeText(getParentFragment().getContext(), "Berhasil dihapus",Toast.LENGTH_SHORT).show();
                            }else {
                                //Toast.makeText(getParentFragment().getContext(), "Tanaman Gagal Dihapus",Toast.LENGTH_SHORT).show();
                            }

                        }catch (JSONException e){
                            Toast.makeText(getParentFragment().getContext(),"Login Gagal"+e,Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Toast.makeText(getParentFragment().getContext(),""+error, Toast.LENGTH_SHORT).show();
                        Log.d("ERROR", String.valueOf(error));
                    }
                });

    }

}