package com.example.hyponic.model;

public class Artikel_Kategori {
    private int id;
    private String name;
    private String image_url;
    private Time time;

    public Artikel_Kategori() {

    }
    public Artikel_Kategori(int id, String name, String image_url, Time time) {
        this.id = id;
        this.name = name;
        this.image_url = image_url;
        this.time = time;
    }

    public Artikel_Kategori(int id, String name, String image_url) {
        this.id = id;
        this.name = name;
        this.image_url = image_url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }
}
