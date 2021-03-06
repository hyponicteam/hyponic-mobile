package com.penshyponic.hyponic.model;

public class Artikel  {
    private int id;
    private String title;
    private String content;
    private String image_url;
    private User author;
    private String author2;
    private Artikel_Kategori category;
    private Time time;
    public Artikel(){

    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor2() {
        return author2;
    }

    public void setAuthor2(String author2) {
        this.author2 = author2;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Artikel_Kategori getCategory() {
        return category;
    }

    public void setCategory(Artikel_Kategori category) {
        this.category = category;
    }

    public Time getTime() {
        return time;
    }


    public void setTime(Time time) {
        this.time = time;
    }
}
