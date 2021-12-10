package com.penshyponic.hyponic;

import static com.penshyponic.hyponic.constant.ApiConstant.BASE_URL;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.penshyponic.hyponic.databinding.ActivityLihatArtikelBinding;
import com.penshyponic.hyponic.model.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

public class LihatArtikelActivity extends AppCompatActivity {

    ActivityLihatArtikelBinding binding;
    SharedPrefManager pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLihatArtikelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.logo);

        pref = new SharedPrefManager(this);
        Log.d("PLANT ID",pref.getSPPlantId());
        //pref= new SharedPrefManager(this);
        getArticleById();
    }
    private void getArticleById() {
        AndroidNetworking.get(BASE_URL+"articles/"+pref.getSPPlantId())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        showLoading(false);
                        Log.d("VIEW ARTIKEL", String.valueOf(response));

                        try {
                            JSONObject dataUser = response.getJSONObject("data");
                            binding.title.setText(dataUser.getString("title"));
                            binding.category.setText(dataUser.getJSONObject("article_category").getString("name"));
                            binding.editedBy.setText(binding.editedBy.getText().toString()+" : "+dataUser.getJSONObject("user").getString("name"));
                            binding.isiArtikel.setText(dataUser.getString("content"));
                            Glide.with(LihatArtikelActivity.this)
                                    // LOAD URL DARI INTERNET
                                    .load(dataUser.getString("image_url"))
                                    // LOAD GAMBAR AWAL SEBELUM GAMBAR UTAMA MUNCUL, BISA DARI LOKAL DAN INTERNET
                                    .placeholder(R.drawable.ic_image_default)
                                    //. LOAD GAMBAR SAAT TERJADI KESALAHAN MEMUAT GMBR UTAMA
                                    .error(R.drawable.ic_image_default)
                                    .into(binding.imageArtikelView);

                        }catch (JSONException e){
                            Toast.makeText(getApplicationContext(),"Data gagal diakses"+e,Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onError(ANError error) {
//                        showLoading(false);
                        // handle error
                        Toast.makeText(getApplicationContext(),"eror", Toast.LENGTH_SHORT).show();
                        Log.d("ERROR", String.valueOf(error));
                    }
                });
    }
}