package com.example.tboil;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.tboil.tboilModels.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class tboilRepository {
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor cursor;
    public void init(Context ctx){
        databaseHelper = new DatabaseHelper(ctx);
    }
    public List<eventsScheduleItem> getEventsSchedule() {
        List<eventsScheduleItem> result = new ArrayList<eventsScheduleItem>();
        databaseHelper.create_db();
        db = databaseHelper.open();
        String today = "2020-07-22 00:00:00";
        String tomorrow = "2020-07-23 00:00:00";
        try {
            cursor = db.rawQuery("select * from `eventsschedule` where start_datetime between \'"+today+"\'" +
                    " and \'"+tomorrow+"\';", null);
        }catch (Exception ex){

        }
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String eventName = cursor.getString(cursor.getColumnIndex("eventName"));
            String hallName = cursor.getString(cursor.getColumnIndex("hallName"));
            String start_datetime = cursor.getString(cursor.getColumnIndex("start_datetime"));
            String durationMins = cursor.getString(cursor.getColumnIndex("durationMins"));
            result.add(new eventsScheduleItem(id, eventName, hallName, start_datetime, durationMins));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return result;
    }

    public List<eventsScheduleItem> getVisitorsByShcheduleEvent() {
        return new ArrayList<>();
        //доделать
//        List<eventsScheduleItem> result = new ArrayList<eventsScheduleItem>();
//        databaseHelper.create_db();
//        db = databaseHelper.open();
//        try {
//            cursor = db.rawQuery("select * from `eventsschedule`;", null);
//        }catch (Exception ex){
//            int ff =12;
//        }
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            String eventName = cursor.getString(cursor.getColumnIndex("eventName"));
//            String hallName = cursor.getString(cursor.getColumnIndex("hallName"));
//            String start_datetime = cursor.getString(cursor.getColumnIndex("start_datetime"));
//            String durationMins = cursor.getString(cursor.getColumnIndex("durationMins"));
//            result.add(new eventsScheduleItem(eventName, hallName, start_datetime, durationMins));
//            cursor.moveToNext();
//        }
//        cursor.close();
//        db.close();
//        return result;
    }
}
