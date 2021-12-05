package com.example.hyponic.model;

import javax.xml.transform.sax.SAXResult;

public class Growths {

    private String id;
    private String plant_height;
    private String leaf_widht;
    private String temperature;
    private String acidity;

    private String date;
    private Time time;

    public Growths(String id, String plant_height, String leaf_widht, String temperature, String acidity, String date) {
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

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }


}
