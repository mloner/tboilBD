package com.example.tboil.Models;

public class eventsScheduleItemModel {
    public String id;
    public String eventName;
    public String hallName;
    public String start_datetime;
    public String durationMins;

    public eventsScheduleItemModel(String id, String eventName, String hallName, String start_datetime, String durationMins) {
        this.id = id;
        this.eventName = eventName;
        this.hallName = hallName;
        this.start_datetime = start_datetime;
        this.durationMins = durationMins;
    }
}
