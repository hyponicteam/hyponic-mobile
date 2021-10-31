package com.example.hyponic;

import static com.example.hyponic.constant.ApiConstant.BASE_URL;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.hyponic.adapter.ArtikelAdapter;
import com.example.hyponic.interactor.ListArtikelInteractor;
import com.example.hyponic.model.Artikel;
import com.example.hyponic.model.Artikel_Kategori;
import com.example.hyponic.model.DailyActivity;
import com.example.hyponic.model.SharedPrefManager;
import com.example.hyponic.model.Time;
import com.example.hyponic.model.User;

import java.util.ArrayList;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BerandaActivity extends AppCompatActivity {
    private RecyclerView rvArtikel,rvDailyActivity;
    private static ArrayList<Artikel> listArtikel;
    private static ArrayList<DailyActivity> listDailyActivity;
    private static final String TAG ="data";
    SharedPrefManager pref;
    private TextView username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beranda);
        init();
        username.setText(pref.getSPNama());
        AndroidNetworking.initialize(getApplicationContext());
        getDataArtikelApi();
        //listDailyActivity.addAll(getListDailyActivity());
        //listArtikel.addAll(getListArtikel());
        showData();
    }
    public void init(){
        username=findViewById(R.id.username);
        //rvArtikel = findViewById(R.id.rv_artikel);
        rvArtikel.setHasFixedSize(true);
        listArtikel =new ArrayList<Artikel>();
        pref = new SharedPrefManager(this);
    }

    public void onClickBacaArtikel(View view) {
        Intent intent = new Intent(BerandaActivity.this,LihatArtikelActivity.class);
        startActivity(intent);
    }
    /*
    public ArrayList<DailyActivity> getListDailyActivity() {
        String[] dataTitle = getResources().getStringArray(R.array.data_title);
        String[] dataDescription = getResources().getStringArray(R.array.data_category);
        //TypedArray dataPhoto = getResources().obtainTypedArray(R.array.data_photo);
        ArrayList<DailyActivity> listHero = new ArrayList<>();
        for (int i = 0; i < dataTitle.length; i++) {
            DailyActivity dailyActivity = new DailyActivity();
            dailyActivity.setName(dataTitle[i]);
            dailyActivity.getTanaman().setName(dataDescription[i]);
            //hero.setPhoto(dataPhoto.getResourceId(i, -1));
           listHero.add(dailyActivity);
        }
        return listHero;
    }*/
    public void getDataArtikelApi(){
        AndroidNetworking.get(BASE_URL+"articles")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "respon: " + response);
                        {
                            try {
                                JSONArray dataArray = response.getJSONObject("data").getJSONArray("data");
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject data = dataArray.getJSONObject(i);
                                    Artikel artikel = new Artikel();
                                    artikel.setId(data.getInt("id"));
                                    artikel.setTitle(data.getString("title"));
                                    artikel.setContent(data.getString("content"));
                                    artikel.setImage_url(data.getString("image_url"));

                                    JSONObject kategoriArtikel = data.getJSONObject("article_category");
                                    Artikel_Kategori kategori = new Artikel_Kategori();
                                    kategori.setId(kategoriArtikel.getInt("id"));
                                    kategori.setName(kategoriArtikel.getString("name"));
                                    artikel.setCategory(kategori);

                                    JSONObject authorArtikel = data.getJSONObject("user");
                                    User author = new User();
                                    author.setId(authorArtikel.getInt("id"));
                                    author.setEmail(authorArtikel.getString("email"));
                                    author.setName(authorArtikel.getString("name"));

                                    artikel.setAuthor(author);
                                    artikel.setTime(new Time(kategoriArtikel.getString("created_at"),
                                            kategoriArtikel.getString("updated_at"),
                                            kategoriArtikel.getString("deleted_at")));

                                    //Log.d("artikel",i+": "+artikel);
                                    listArtikel.add(artikel);

                                   // Log.d("Data","ke -"+i);
                                   // Log.d("judul artikel ",": "+artikel.getTitle());
                                   // Log.d("kategori artikel ",":"+artikel.getCategory().getName());
                                }
                                Log.d("getartikel size ;",  ""+listArtikel.size());
                                showRecyclerList(listArtikel);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    } @Override
                    public void onError(ANError error) {
                        Log.d(TAG, "onError: " + error); //untuk log pada onerror

                    }
                });
    }
    private void showData(){
        Log.d("artikelsize",":"+listArtikel.size());
        for(int i=0; i<listArtikel.size(); i++){

            Log.d("Data","ke -"+i);
            Log.d("judul artikel ",": "+listArtikel.get(i).getTitle());
            Log.d("kategori artikel ",":"+listArtikel.get(i).getCategory().getName());
        }
    }

    private void showRecyclerList(ArrayList<Artikel> artikel){
        rvArtikel.setLayoutManager(new LinearLayoutManager(this));
        ArtikelAdapter listArtikelAdapter= new ArtikelAdapter(artikel);
        rvArtikel.setAdapter(listArtikelAdapter);
    }
}