package com.example.hyponic.view.Tutorial;

import static com.example.hyponic.constant.ApiConstant.BASE_URL;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.example.hyponic.DummyData.ArticleDummyData;
import com.example.hyponic.LihatArtikelActivity;
import com.example.hyponic.adapter.ArticleAdapter;
import com.example.hyponic.databinding.FragmentHomeTutorialBinding;
import com.example.hyponic.model.Artikel;
import com.example.hyponic.model.Artikel_Kategori;
import com.example.hyponic.model.Plant;
import com.example.hyponic.model.SharedPrefManager;
import com.example.hyponic.model.Time;
import com.example.hyponic.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class HomeTutorialFragment extends Fragment {

    ArrayList<Artikel> articles= new ArrayList<>();
    SharedPrefManager pref;
    FragmentHomeTutorialBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeTutorialBinding.inflate(inflater, container, false);
        return  binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pref = new SharedPrefManager(getContext());
        //articles.addAll(ArticleDummyData.getListData());
        for(int i=0; i<articles.size(); i++){
            Log.d("ARTIKEL", articles.get(i).getTitle()+" "+articles.get(i).getAuthor2()
                    + " "+articles.get(i).getContent());
        }
        binding.rvarticle.setHasFixedSize(true);
        //showRecyclerList(articles);
        getData();

    }
//    private void getData() {
//        AndroidNetworking.get("http://18.221.184.74:8088/articles")
//                .setPriority(Priority.LOW)
//                .build()
//                .getAsString(new StringRequestListener() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.d("RESPONSE ARTICLE", response);
//                    }
//
//                    @Override
//                    public void onError(ANError anError) {
//                        Log.d("ERROR", "onError: " + anError); //untuk log pada onerror
//                    }
//                });
//
//    }

    private void getData() {
        AndroidNetworking.get("http://18.221.184.74:8088/api/articles")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("RESPONSE ARTICLE", String.valueOf(response));

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

                                    Log.d("artikel",i+": "+artikel);
                                    articles.add(artikel);

                                     Log.d("Data","ke -"+i);
                                     Log.d("judul artikel ",": "+artikel.getTitle());
                                     Log.d("kategori artikel ",":"+artikel.getCategory().getName());
                                }
                                Log.d("getartikel size ;",  ""+articles.size());
                                showRecyclerList(articles);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                    } @Override
                    public void onError(ANError error) {
                        Log.d("ERROR", "onError: " + error); //untuk log pada onerror
                        Toast.makeText(getContext(),String.valueOf(error),Toast.LENGTH_SHORT).show();

                    }
                });

    }

    private void showSelectedArticle(Artikel artikel) {
        Toast.makeText(getContext(), "Kamu memilih " + artikel.getTitle(), Toast.LENGTH_SHORT).show();
    }
    public void showRecyclerList(ArrayList<Artikel> listArtikel){
        binding.rvarticle.setLayoutManager(new LinearLayoutManager(getContext()));
        ArticleAdapter listArticleAdapter = new ArticleAdapter(listArtikel);
        binding.rvarticle.setAdapter(listArticleAdapter);

        listArticleAdapter.setOnItemClickCallback(new ArticleAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Artikel data) {
                //showSelectedArticle(data);
                pref.saveSPString(pref.SP_PLANT_ID,String.valueOf(data.getId()));
                Intent moveIntent = new Intent(getContext(), LihatArtikelActivity.class);
                startActivity(moveIntent);
            }
        });
    }

}