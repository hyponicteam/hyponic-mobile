package com.penshyponic.hyponic.model;

public class Time {
    private String created_at;
    private String updated_at;
    private String deleted_at;

    public Time(){

    }
    public Time(String created_at, String updated_at) {
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public Time(String created_at, String updated_at, String deleted_at) {
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }
}
