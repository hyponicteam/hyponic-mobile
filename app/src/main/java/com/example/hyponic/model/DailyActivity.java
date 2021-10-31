package com.example.hyponic.model;

public class DailyActivity {
    private int id;
    private String name;
    private Plant plant;
    private Time time;

    public DailyActivity() {
    }

    public DailyActivity(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public DailyActivity(int id, String name, Plant plant) {
        this.id = id;
        this.name = name;
        this.plant = plant;
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

    public Plant getTanaman() {
        return plant;
    }

    public void setTanaman(Plant plant) {
        this.plant = plant;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }
}
