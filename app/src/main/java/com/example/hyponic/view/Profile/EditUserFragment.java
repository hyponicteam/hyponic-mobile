package com.example.hyponic.view.Profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hyponic.R;
import com.example.hyponic.databinding.FragmentEditUserBinding;
import com.example.hyponic.model.SharedPrefManager;

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
        binding.edName.setHint(pref.getSPNama());
        binding.edEmail.setHint(pref.getSPEmail());

        binding.btnCancel.setOnClickListener(v-> cancel());
        binding.btnSave.setOnClickListener(v-> save());
    }

    private void save() {
        pref.saveSPString(pref.SP_NAMA,binding.edName.getText().toString());
        pref.saveSPString(pref.SP_EMAIL,binding.edEmail.getText().toString());
        cancel();
    }

    private void cancel() {
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