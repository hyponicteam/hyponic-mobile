package com.example.hyponic.DummyData;


import com.example.hyponic.model.Artikel;

import java.util.ArrayList;

public class ArticleDummyData {
    private static  String content="Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod\n" +
            "        tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud\n" +
            "        exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor\n" +
            "        in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.\n" +
            "        Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit\n" +
            "        anim id est laborum.";
    private static String[] title = {
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
    private static String[] author2 = {
            "Admin1",
            "Admin2",
            "Admin3",
            "Admin4",
            "Admin5",
            "Admin6",
            "Admin7",
            "Admin8",
            "Admin9",
            "Admin10"
    };
    public static ArrayList<Artikel> getListData() {
        ArrayList<Artikel> list = new ArrayList<>();
        for (int position = 0; position < 5; position++) {
            Artikel text = new Artikel();
            text.setId(position);
            text.setTitle(title[position]);
            text.setAuthor2(author2[position]);
            text.setContent(content);
            list.add(text);
        }
        return list;
    }
}
