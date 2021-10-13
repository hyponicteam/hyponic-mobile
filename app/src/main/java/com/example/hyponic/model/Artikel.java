package com.example.hyponic.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Artikel implements Parcelable {
    private int id;
    private String title;
    private String content;
    private String image_url;
    private String author;
    private String article_categories_id;

    protected Artikel(Parcel in) {
        id = in.readInt();
        title = in.readString();
        content = in.readString();
        image_url = in.readString();
        author = in.readString();
        article_categories_id = in.readString();
    }
    public Artikel(){

    }
    public static final Creator<Artikel> CREATOR = new Creator<Artikel>() {
        @Override
        public Artikel createFromParcel(Parcel in) {
            return new Artikel(in);
        }

        @Override
        public Artikel[] newArray(int size) {
            return new Artikel[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getArticle_categories_id() {
        return article_categories_id;
    }

    public void setArticle_categories_id(String article_categories_id) {
        this.article_categories_id = article_categories_id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(content);
        parcel.writeString(image_url);
        parcel.writeString(author);
        parcel.writeString(article_categories_id);
    }
}
