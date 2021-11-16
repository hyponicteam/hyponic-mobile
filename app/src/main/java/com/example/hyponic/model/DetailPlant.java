package com.example.hyponic.model;

public class DetailPlant {

    private String id;
    private String plant_height;
    private String leaf_widht;
    private String temperature;
    private String acidity;
    private User user;
    private Time time;
    private Plant plant;

    public DetailPlant(){

    }

    public DetailPlant(String id, String plant_height, String leaf_widht, String temperature, String acidity) {
        this.id = id;
        this.plant_height = plant_height;
        this.leaf_widht = leaf_widht;
        this.temperature = temperature;
        this.acidity = acidity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlant_height() {
        return plant_height;
    }

    public void setPlant_height(String plant_height) {
        this.plant_height = plant_height;
    }

    public String getLeaf_widht() {
        return leaf_widht;
    }

    public void setLeaf_widht(String leaf_widht) {
        this.leaf_widht = leaf_widht;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getAcidity() {
        return acidity;
    }

    public void setAcidity(String acidity) {
        this.acidity = acidity;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }
}
