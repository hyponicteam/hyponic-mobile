package com.example.hyponic.view.Profile;

import static com.example.hyponic.constant.ApiConstant.BASE_URL;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

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
import com.example.hyponic.databinding.FragmentEditUserBinding;
import com.example.hyponic.model.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

public class EditUserFragment extends Fragment {

    SharedPrefManager pref;
    private FragmentEditUserBinding binding;
    public EditUserFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditUserBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pref= new SharedPrefManager(getContext());
        binding.edName.setText(pref.getSPNama());

        binding.btnCancel.setOnClickListener(v-> backToUserFragment());
        binding.btnSave.setOnClickListener(v-> save());
    }

    private void save() {
        if(!binding.edName.getText().toString().equals("")){
            saveApi();
            pref.saveSPString(pref.SP_NAMA,binding.edName.getText().toString());
            backToUserFragment();
        }else{
            binding.inputLayoutName.setError("Nama tidak boleh kosong");
        }
    }
    private void saveApi() {
        AndroidNetworking.patch(BASE_URL+"user")
                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization","Bearer "+pref.getSPToken())
                .addBodyParameter("name",binding.edName.getText().toString())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        showLoading(false);
                        Log.d("UPDATE USER ", String.valueOf(response));
                        Toast.makeText(getContext(),"Berhasil disimpan",Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject dataUser = response.getJSONObject("data");
                            pref.saveSPString(pref.SP_Edited_At,dataUser.getString("updated_at"));

                        }catch (JSONException e){
                            Toast.makeText(getContext(),"Data gagal diakses"+e,Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
//                        showLoading(false);
                        // handle error
                        Toast.makeText(getContext(),""+error, Toast.LENGTH_SHORT).show();
                        Log.d("ERROR", String.valueOf(error));
                    }
                });

    }

    private void backToUserFragment() {
        UserFragment mCategoryFragment = new UserFragment();
        FragmentManager mFragmentManager = getParentFragmentManager();
        mFragmentManager
                .beginTransaction()
                .replace(R.id.nav_host_fragment_activity_main, mCategoryFragment, UserFragment.class.getSimpleName())
                .addToBackStack(null)
                .commit();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}