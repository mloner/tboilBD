package com.example.tboil.Models;

import java.util.List;

public class HallInfoModel {
    public String avgEventsCountPerDay;
    public String placeInTop;
    public List<String> top5Tematics;

    public HallInfoModel(String avgEventsCountPerDay, String placeInTop, List<String> top5Tematics) {
        this.avgEventsCountPerDay = avgEventsCountPerDay;
        this.placeInTop = placeInTop;
        this.top5Tematics = top5Tematics;
    }
}
