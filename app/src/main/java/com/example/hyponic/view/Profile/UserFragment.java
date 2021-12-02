package com.example.hyponic.view.Profile;

import static com.example.hyponic.constant.ApiConstant.BASE_URL;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.hyponic.LoginActivity;
import com.example.hyponic.R;
import com.example.hyponic.databinding.FragmentUserBinding;
import com.example.hyponic.model.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

public class UserFragment extends Fragment {

    SharedPrefManager pref;
    private FragmentUserBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentUserBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pref= new SharedPrefManager(getContext());
        getUserData();
        binding.username.setText(pref.getSPNama());
        binding.email.setText(pref.getSPEmail());
        binding.editUser.setOnClickListener(view1 -> {
            toEditUserFragment();
        });
        binding.logout.setOnClickListener(v->{
            logOut();
        });
    }

    private void getUserData() {
        AndroidNetworking.get(BASE_URL+"user")
                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization","Bearer "+pref.getSPToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        showLoading(false);
                        Log.d("USER RESPON", String.valueOf(response));

                        try {
                                JSONObject dataUser = response.getJSONObject("data");
                                binding.createdAt.setText(dataUser.getString("created_at"));
                                binding.updatedAt.setText(dataUser.getString("updated_at"));

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

    private void logOut() {
        pref.saveSPBoolean(pref.SP_IS_LOGIN, false);
        startActivity(new Intent(getActivity(), LoginActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
        getActivity().finish();
    }

    private void toEditUserFragment() {
        EditUserFragment mCategoryFragment = new EditUserFragment();
        FragmentManager mFragmentManager = getParentFragmentManager();
        mFragmentManager
                .beginTransaction()
                .replace(R.id.nav_host_fragment_activity_main, mCategoryFragment, EditUserFragment.class.getSimpleName())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}