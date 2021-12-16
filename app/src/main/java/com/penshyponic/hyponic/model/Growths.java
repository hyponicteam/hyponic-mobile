package com.penshyponic.hyponic.model;

public class Growths {

    private String id;
    private double plant_height;
    private double leaf_widht;
    private double temperature;
    private double acidity;

    private String date;
    private Time time;

    public Growths(String id, double plant_height, double leaf_widht, double temperature, double acidity, String date) {
        this.id = id;
        this.plant_height = plant_height;
        this.leaf_widht = leaf_widht;
        this.temperature = temperature;
        this.acidity = acidity;
        this.date = date;
    }

    public Growths(){

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getPlant_height() {
        return plant_height;
    }

    public void setPlant_height(double plant_height) {
        this.plant_height = plant_height;
    }

    public double getLeaf_widht() {
        return leaf_widht;
    }

    public void setLeaf_widht(double leaf_widht) {
        this.leaf_widht = leaf_widht;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getAcidity() {
        return acidity;
    }

    public void setAcidity(double acidity) {
        this.acidity = acidity;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }


}
