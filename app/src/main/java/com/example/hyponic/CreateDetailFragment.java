//package com.example.hyponic;
//
//import static com.example.hyponic.constant.ApiConstant.BASE_URL;
//
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentManager;
//
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Toast;
//
//import com.androidnetworking.AndroidNetworking;
//import com.androidnetworking.common.Priority;
//import com.androidnetworking.error.ANError;
//import com.androidnetworking.interfaces.JSONObjectRequestListener;
//import com.example.hyponic.databinding.ActivityCreateDetailTanamanBinding;
//import com.example.hyponic.databinding.FragmentCreateDetailBinding;
//import com.example.hyponic.databinding.FragmentDetailplantfragmentBinding;
//import com.example.hyponic.model.DetailPlant;
//import com.example.hyponic.model.SharedPrefManager;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//
//public class CreateDetailFragment extends Fragment {
//
//    private FragmentCreateDetailBinding binding;
//    private String panjang, lebar, suhu, ph;
//    private DetailPlant detailplant;
//    SharedPrefManager pref;
//
//
//    public CreateDetailFragment() {
//        // Required empty public constructor
//    }
//
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             ViewGroup container, Bundle savedInstanceState) {
//
//        binding = FragmentCreateDetailBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();
//
//        return root;
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        pref = new SharedPrefManager(getContext());
//
//        binding.btnSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                saveDataPlant(view);
//
//            }
//        });
//    }
//
//    public void saveDataPlant(View view) {
//        panjang = binding.edPanjang.getText().toString();
//        lebar = binding.edLebar.getText().toString();
//        suhu = binding.edSuhu.getText().toString();
//        ph = binding.edPh.getText().toString();
//
//        if (panjang.equals("") || lebar.equals("") || suhu.equals("") || ph.equals("")) {
//            Toast.makeText(requireContext(), "Data tidak boleh kosong", Toast.LENGTH_LONG).show();
//        } else {
//            AndroidNetworking.post(BASE_URL + "growths")
//                    .addHeaders("Authorization", "Bearer " + pref.getSPToken())
//                    .addHeaders("Accept", "application/json")
//                    .addBodyParameter("plant_height", panjang)
//                    .addBodyParameter("leaf_width", lebar)
//                    .addBodyParameter("temperature", suhu)
//                    .addBodyParameter("acidity", ph)
//                    .addBodyParameter("plant_id", pref.getSPPlantId())
//                    .setPriority(Priority.MEDIUM)
//                    .setTag("test")
//                    .build()
//                    .getAsJSONObject(new JSONObjectRequestListener() {
//                        @Override
//                        public void onResponse(JSONObject response) {
//                            //showLoading(false);
//                            Log.d("TAG", String.valueOf(response));
//                            try {
//                                if (response.getJSONObject("meta").getString("status").equals("success")) {
//                                    Log.d("Data", "" + response.getJSONObject("data"));
//                                    Toast.makeText(requireContext(), "DATA BERHASIL DISIMPAN", Toast.LENGTH_SHORT).show();
//                                    DetailPlantFragment mCategoryFragment = new DetailPlantFragment();
//                                    FragmentManager mFragmentManager = getParentFragmentManager();
//                                    mFragmentManager
//                                            .beginTransaction()
//                                            .replace(R.id.nav_host_fragment_activity_main, mCategoryFragment, DetailPlantFragment.class.getSimpleName())
//                                            .addToBackStack(null)
//                                            .commit();
//
//                                } else {
//                                    Toast.makeText(requireContext(), "DATA GAGAl DISIMPAN", Toast.LENGTH_SHORT).show();
//                                }
//
//                            } catch (JSONException e) {
//                                Toast.makeText(requireContext(), "Login Gagal" + e, Toast.LENGTH_SHORT).show();
//                            }
//                        }
//
//                        @Override
//                        public void onError(ANError error) {
//                            //showLoading(false);
//                            // handle error
//                            Toast.makeText(requireContext(), "" + error, Toast.LENGTH_SHORT).show();
//                            Log.d("ERROR", String.valueOf(error));
//                        }
//                    });
//        }
//
//    }
//}