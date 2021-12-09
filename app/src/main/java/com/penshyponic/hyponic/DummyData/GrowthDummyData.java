package com.penshyponic.hyponic.DummyData;

import com.penshyponic.hyponic.model.TopGrowth;

import java.util.ArrayList;

public class GrowthDummyData {

    private static String[] name = {
            "Enak",
            "Timun",
            "Bayam"
    };
    private static double[] grow = {
            2.0, 2.0, 2.0
    };
    public static ArrayList<TopGrowth> getListData() {
        ArrayList<TopGrowth> list = new ArrayList<>();
        for (int position = 0; position < name.length; position++) {
            TopGrowth text = new TopGrowth();
            text.setName(name[position]);
            text.setGrowth_per_day(grow[position]);
            list.add(text);
        }
        return list;
    }
}
