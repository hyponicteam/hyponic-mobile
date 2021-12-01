package com.example.hyponic.view.Tutorial;

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

import com.example.hyponic.DummyData.ArticleDummyData;
import com.example.hyponic.LihatArtikelActivity;
import com.example.hyponic.adapter.ArticleAdapter;
import com.example.hyponic.databinding.FragmentHomeTutorialBinding;
import com.example.hyponic.model.Artikel;
import com.example.hyponic.model.SharedPrefManager;

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
        articles.addAll(ArticleDummyData.getListData());
        for(int i=0; i<articles.size(); i++){
            Log.d("ARTIKEL", articles.get(i).getTitle()+" "+articles.get(i).getAuthor2()
                    + " "+articles.get(i).getContent());
        }
        binding.rvarticle.setHasFixedSize(true);
        showRecyclerList(articles);

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
                Intent moveIntent = new Intent(getContext(), LihatArtikelActivity.class);
                startActivity(moveIntent);
            }
        });
    }

}