package com.example.hyponic.DummyData;

import com.example.hyponic.R;
import com.example.hyponic.model.Artikel;
import com.example.hyponic.model.Plant;
import com.example.hyponic.model.Time;

import java.util.ArrayList;

public class PlantDummyData {
    private static String[] heroNames = {
            "Sawi Pertamaku",
            "Packcoy Yummy",
            "Hidro Melon",
            "Hidro Semangka",
            "Sawi Try Again",
            "Enak",
            "Timun",
            "Bayam",
            "Kubis",
            "Hidroponikku"
    };
    private static String[] heroDetails = {
            "24  Sept 2021, 14.00",
            "24  Sept 2021, 14.00",
            "24  Sept 2021, 14.00",
            "24  Sept 2021, 14.00",
            "24  Sept 2021, 14.00",
            "24  Sept 2021, 14.00",
            "24  Sept 2021, 14.00",
            "24  Sept 2021, 14.00",
            "24  Sept 2021, 14.00",
            "24  Sept 2021, 14.00"
    };

    public static ArrayList<Plant> getListData() {
        ArrayList<Plant> list = new ArrayList<>();
        for (int position = 0; position < 3; position++) {
            Plant hero = new Plant();
            hero.setId(String.valueOf(position));
            hero.setName(heroNames[position]);
            hero.setTime(new Time());
            hero.getTime().setUpdated_at(heroDetails[position]);
            list.add(hero);
        }
        return list;
    }
}
