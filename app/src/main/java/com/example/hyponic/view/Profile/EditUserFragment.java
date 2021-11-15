package com.example.hyponic.view.Profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hyponic.DummyData.PlantDummyData;
import com.example.hyponic.R;
import com.example.hyponic.databinding.FragmentEditPlantBinding;
import com.example.hyponic.databinding.FragmentEditUserBinding;
import com.example.hyponic.databinding.FragmentHomeBinding;

public class EditUserFragment extends Fragment {

    private FragmentEditUserBinding binding;
    public EditUserFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_edit_user, container, false);
        binding = FragmentEditUserBinding.inflate(inflater, container, false);
        return  binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}