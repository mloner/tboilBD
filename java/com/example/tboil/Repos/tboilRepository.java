package com.example.tboil.Repos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.tboil.DBTools.DatabaseHelper;
import com.example.tboil.Models.eventVisitorsModel;
import com.example.tboil.Models.eventsScheduleItemModel;

import java.util.ArrayList;
import java.util.List;

public class tboilRepository {
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor cursor;
    public void init(Context ctx){
        databaseHelper = new DatabaseHelper(ctx);
    }

    public String getVisitorsCountByEventId(String eventId){
        String result = "0";
        databaseHelper.create_db();
        db = databaseHelper.open();
        String[] args = { eventId };
        try {
            cursor = db.rawQuery("select " +
                    "COUNT(schedule_records.id) " +
                    "from " +
                    "schedule_records " +
                    "inner join " +
                    "persons on persons.id = person_id " +
                    "where event_id = ?;", args);
        }catch (Exception ex){}
        cursor.moveToFirst();
        try {
            result = cursor.getString(0);
        }catch (Exception ex){}
        cursor.close();
        db.close();
        return result;
    }

    public List<eventsScheduleItemModel> getEventsSchedule(String dateFrom, String dateTo) {
        List<eventsScheduleItemModel> result = new ArrayList<eventsScheduleItemModel>();
        databaseHelper.create_db();
        db = databaseHelper.open();
        String[] args = { dateFrom, dateTo };
        try {
            cursor = db.rawQuery("select * from `eventsschedule` where start_datetime between ?" +
                    " and ?;", args);
        }catch (Exception ex){}
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String eventName = cursor.getString(cursor.getColumnIndex("eventName"));
            String hallName = cursor.getString(cursor.getColumnIndex("hallName"));
            String start_datetime = cursor.getString(cursor.getColumnIndex("start_datetime"));
            String durationMins = cursor.getString(cursor.getColumnIndex("durationMins"));
            result.add(new eventsScheduleItemModel(id, eventName, hallName, start_datetime, durationMins));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return result;
    }

    public List<eventVisitorsModel> getVisitorsByShcheduleEvent(String _id) {
        List<eventVisitorsModel> result = new ArrayList<eventVisitorsModel>();
        databaseHelper.create_db();
        db = databaseHelper.open();
        String[] args = { _id };
        try {
            cursor = db.rawQuery("select " +
                    "schedule_records.id, " +
                    "persons.fio, " +
                    "is_visited " +
                    "from " +
                    "schedule_records " +
                    "inner join " +
                    "persons on persons.id = person_id " +
                    "where event_id = ?;", args);
        }catch (Exception ex){}
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String fio = cursor.getString(cursor.getColumnIndex("fio"));
            String isVisited = cursor.getString(cursor.getColumnIndex("is_visited"));
            result.add(new eventVisitorsModel(id, fio, isVisited));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return result;
    }

    public boolean changeVisitedById(String id, boolean state){
        databaseHelper.create_db();
        db = databaseHelper.open();
        String[] args = { Boolean.toString(state), id };
        String sql = "UPDATE " +
                "schedule_records " +
                "SET is_visited = ? " +
                "where schedule_records.id = ?;";
        db.execSQL(sql, args);
        db.close();
        return true;
    }

    public boolean deleteVisit(String id){
        databaseHelper.create_db();
        db = databaseHelper.open();
        String[] args = { id };
        String sql = "DELETE " +
                "from schedule_records " +
                "where schedule_records.id = ?;";
        db.execSQL(sql, args);
        db.close();
        return true;
    }

    public List<String> getNonVisitorsFiosByEventid(String event_id){
        List<String> result = new ArrayList<String>();
        databaseHelper.create_db();
        db = databaseHelper.open();
        String[] args = { event_id };
        String sql = "select " +
                "fio " +
                "from " +
                "persons " +
                "where " +
                "id not in ( " +
                "select " +
                "person_id " +
                "FROM " +
                "schedule_records " +
                "where " +
                "event_id = ? " +
                ");";
        try {
            cursor = db.rawQuery(sql, args);
        }catch (Exception ex){
        }
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String fio = cursor.getString(cursor.getColumnIndex("fio"));
            result.add(fio);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return result;
    }

    public boolean addVisit(String event_id, String person_fio){
        databaseHelper.create_db();
        db = databaseHelper.open();
        String[] args = { event_id, person_fio };
        String sql = "INSERT INTO " +
                "schedule_records (event_id, is_visited, person_id) " +
                "VALUES(?, 'false', (select id from persons where fio = ?));";
        db.execSQL(sql, args);
        db.close();
        return true;
    }
}