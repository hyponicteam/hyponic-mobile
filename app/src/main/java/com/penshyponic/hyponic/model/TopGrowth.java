package com.penshyponic.hyponic.model;

public class TopGrowth {
    private String name;
    private double growth_per_day;
    private String from;
    private String to;
    private String unit;

    public TopGrowth() {

    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getGrowth_per_day() {
        return growth_per_day;
    }

    public void setGrowth_per_day(double growth_per_day) {
        this.growth_per_day = growth_per_day;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}

