package com.penshyponic.hyponic.view.Profile;

import static com.penshyponic.hyponic.constant.ApiConstant.BASE_URL;

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
import com.penshyponic.hyponic.LoginActivity;
import com.penshyponic.hyponic.R;
import com.penshyponic.hyponic.databinding.FragmentUserBinding;
import com.penshyponic.hyponic.model.DateFormatter;
import com.penshyponic.hyponic.model.SharedPrefManager;

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


        binding.username.setText(pref.getSPNama());
        binding.email.setText(pref.getSPEmail());
        binding.createdAt.setText(getDate(pref.getSP_Created_At()));
        binding.updated.setText(getDate(pref.getSP_Edited_At()));

        binding.editUser.setOnClickListener(view1 -> {
            toEditUserFragment();
        });
        binding.logout.setOnClickListener(v->{
            logOut();
        });
    }
    private String getDate(String sp_created_at) {
        DateFormatter formatter = new DateFormatter(sp_created_at,'T');
        String date = formatter.getAfter_separated();
        int indexT = sp_created_at.indexOf('T')+1;
        String time = sp_created_at.substring(indexT,sp_created_at.indexOf('.'));
        return date+"   "+time;
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