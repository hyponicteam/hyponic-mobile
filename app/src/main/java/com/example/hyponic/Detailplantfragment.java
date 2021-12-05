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
//import androidx.recyclerview.widget.LinearLayoutManager;
//
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//
//import com.androidnetworking.AndroidNetworking;
//import com.androidnetworking.common.Priority;
//import com.androidnetworking.error.ANError;
//import com.androidnetworking.interfaces.JSONObjectRequestListener;
//import com.example.hyponic.adapter.GrowthsAdapter;
//import com.example.hyponic.databinding.FragmentDetailplantfragmentBinding;
//import com.example.hyponic.model.DetailPlant;
//import com.example.hyponic.model.Growths;
//import com.example.hyponic.model.SharedPrefManager;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//
//
//public class DetailPlantFragment extends Fragment {
//
//    private ArrayList<Growths> growths = new ArrayList<>();
//    private ArrayList<DetailPlant> detailPlantList = new ArrayList<>();
//    private FragmentDetailplantfragmentBinding binding;
//    SharedPrefManager pref;
//
//
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             ViewGroup container, Bundle savedInstanceState) {
//
//        binding = FragmentDetailplantfragmentBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();
//
////        Bundle args = getArguments();
////        String detailPlantFragment = args.getString("EXTRA_TITLE");
//
//        return root;
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        pref = new SharedPrefManager(getContext());
//        binding.rvlisttabeltanaman.setHasFixedSize(true);
//
//        //binding.rvListdetTanaman.setHasFixedSize(true);
//
//        getGrowth();
//        //getTabelGrowth();
//
//        binding.btnAddDataPantau.setOnClickListener(view1 -> {
//            CreateDetailFragment mCategoryFragment = new CreateDetailFragment();
//            FragmentManager mFragmentManager = getParentFragmentManager();
//            mFragmentManager
//                    .beginTransaction()
//                    .replace(R.id.nav_host_fragment_activity_main, mCategoryFragment, CreateDetailFragment.class.getSimpleName())
//                    .addToBackStack(null)
//                    .commit();
//        });
//
//    }
//
//    public void getGrowth(){
//        AndroidNetworking.get(BASE_URL+"plants/"+pref.getSPPlantId())
//                .addHeaders("Accept", "application/json")
//                .addHeaders("Authorization","Bearer "+pref.getSPToken())
//                .setPriority(Priority.LOW)
//                .build()
//                .getAsJSONObject(new JSONObjectRequestListener() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            Log.d("growths", "respon: " + response.getJSONObject("data").getJSONArray("growths"));
//                            JSONArray data = response.getJSONObject("data").getJSONArray("growths");
//                            for(int i=0; i<data.length(); i++){
//                                JSONObject jsontabelList = data.getJSONObject(i);
//                                Log.d("GROW","ke -"+i+" : "+data.getJSONObject(i));
//                                Growths tabellist = new Growths(
//                                        jsontabelList.getString("id"),
//                                        jsontabelList.getString("plant_height"),
//                                        jsontabelList.getString("leaf_width"),
//                                        jsontabelList.getString("temperature"),
//                                        jsontabelList.getString("acidity"),
//                                        jsontabelList.getString("created_at"));
//                                growths.add(tabellist);
//                            }
//                            Log.d("SIZEGROW: ",""+growths.size());
//                            showTabelList(growths);
//
//                        }catch (JSONException e){
//                            e.printStackTrace();
//                        }
//                    }
//                    @Override
//                    public void onError(ANError error) {
//                        Log.d("TAG", "onError: " + error); //untuk log pada onerror
//                    }
//                });
//    }
//
////    public void getTabelGrowth(){
////        AndroidNetworking.get(BASE_URL+"/plants/fb21a915-a119-464a-8b89-f2884388e1cb")
////                .addHeaders("Accept", "application/json")
////                .addHeaders("Authorization","Bearer "+pref.getSPToken())
////                .setPriority(Priority.LOW)
////                .build()
////                .getAsJSONObject(new JSONObjectRequestListener() {
////
////                    @Override
////                    public void onResponse(JSONObject response) {
////                        try {
////
////                            Log.d("TAG", "respon: " + response.getJSONObject("data").getJSONArray("growths"));
////                            JSONArray data = response.getJSONObject("data").getJSONArray("growths");
////                            for(int i=0; i<data.length(); i++){
////                                JSONObject jsontabelList = data.getJSONObject(i);
////                                Log.d("TAG","ke -"+i+" : "+data.getJSONObject(i));
////                                Growths tabellist = new Growths(jsontabelList.getString("id"),
////                                        jsontabelList.getString("plant_height"), jsontabelList.getString("leaf_width"), jsontabelList.getString("temperature"), jsontabelList.getString("acidity"), jsontabelList.getString("created_at"));
////
////                                tabelList.add(tabellist);
////                            }
////                            Log.d("SIZE: ",""+tabelList.size());
////                            showTabelList(tabelList);
////
////                        }catch (JSONException e){
////                            e.printStackTrace();
////                        }
////                    }
////                    @Override
////                    public void onError(ANError error) {
////                        Log.d("TAG", "onError: " + error); //untuk log pada onerror
////                    }
////                });
////
////    }
//
//    private void showTabelList(ArrayList<Growths> tabellist ){
//        Log.d("tabel", " "+tabellist.size());
//        binding.rvlisttabeltanaman.setLayoutManager(new LinearLayoutManager(getContext()));
//        GrowthsAdapter listTabel = new GrowthsAdapter(getContext(), getParentFragmentManager(), tabellist);
//        binding.rvlisttabeltanaman.setAdapter(listTabel);
//    }
//}