package com.penshyponic.hyponic;

import static com.penshyponic.hyponic.constant.ApiConstant.BASE_URL;
import static com.penshyponic.hyponic.model.CreateGrowthHistory.history;

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
import com.penshyponic.hyponic.databinding.FragmentDeleteGrowthsBinding;
import com.penshyponic.hyponic.model.DateFormatter;
import com.penshyponic.hyponic.model.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;


public class DeleteGrowthsFragment extends DialogFragment {

    public static final String EXTRA_PLANTID = "extra_plantID";
    public static final String EXTRA_GROWTH_ID ="extra_growth_id";
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
                //dismiss();
                Intent moveIntent = new Intent(getActivity(), GrowthsPlantActivity.class);
                moveIntent.putExtra(GrowthsPlantActivity.EXTRA_PLANTID,getArguments().getString(EXTRA_PLANTID));
                startActivity(moveIntent);
            }
        });
        binding.btnCancel.setOnClickListener(v->{
            dismiss();
        });

    }
    private void getGrwothById(){
        AndroidNetworking.get(BASE_URL+"growths/"+pref.getSPGrowthsId())
                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization","Bearer "+pref.getSPToken())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("TAG", String.valueOf(response));
                        try {
                            if(response.getJSONObject("meta").getString("status").equals("success")){
                                String date = response.getJSONObject("data").getString("created_at");
                                DateFormatter dateFormatter = new DateFormatter(date,'T');
                                date=dateFormatter.getAfter_separated();
                                deleteHistory(date);
                            }else {
                                Log.d("meta",response.toString());
                            }

                        }catch (JSONException e){
                            Log.d("GROWTH BY ID",e.toString());
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        Log.d("ERROR", String.valueOf(error.getErrorBody()));
                    }
                });

    }
    private void deleteHistory(String date){
        for(int i=0; i< history.size(); i++){
            if(history.get(i).getPlantId().equals(getArguments().getString(EXTRA_PLANTID))){
                if(history.get(i).getTimeCreated().equals(date)){
                    history.get(i).setTimeCreated("");
                }
            }
        }
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
                                getGrwothById();
                                Toast.makeText(getContext(), "Berhasil dihapus",Toast.LENGTH_SHORT).show();
                            }else {
                               Toast.makeText(getContext(), "Gagal Dihapus",Toast.LENGTH_SHORT).show();
                            }

                        }catch (JSONException e){
                            Toast.makeText(getContext(),"Gagal dihapus"+e,Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Toast.makeText(getContext(),"Gagal dihapus", Toast.LENGTH_SHORT).show();
                        Log.d("ERROR", String.valueOf(error.getErrorBody()));
                    }
                });

    }


}