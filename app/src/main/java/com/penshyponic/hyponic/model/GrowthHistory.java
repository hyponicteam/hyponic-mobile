package com.penshyponic.hyponic.model;

import java.util.Calendar;

public class GrowthHistory {
    String plantId;
    String timeCreated;

    public GrowthHistory(String id, String time){
        this.plantId=id;
        this.timeCreated=time;
    }

    public String getPlantId() {
        return plantId;
    }

    public void setPlantId(String plantId) {
        this.plantId = plantId;
    }

    public String getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(String timeCreated) {
        this.timeCreated = timeCreated;
    }
}
