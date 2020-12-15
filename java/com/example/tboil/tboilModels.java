package com.example.tboil;

public class tboilModels {
    public static class eventsScheduleItem{
        public String id;
        public String eventName;
        public String hallName;
        public String start_datetime;
        public String durationMins;

        public eventsScheduleItem(String id, String eventName, String hallName, String start_datetime, String durationMins) {
            this.id = id;
            this.eventName = eventName;
            this.hallName = hallName;
            this.start_datetime = start_datetime;
            this.durationMins = durationMins;
        }
    }
}
