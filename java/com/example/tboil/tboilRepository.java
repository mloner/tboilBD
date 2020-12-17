package com.example.tboil;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.tboil.tboilModels.*;

import java.text.SimpleDateFormat;
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
        String today = "2020-07-22 00:00:00";//new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String tomorrow = "2020-07-23 00:00:00";// new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(today.plusDays(1));
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

    public List<eventVisitors> getVisitorsByShcheduleEvent(String _id) {
        List<eventVisitors> result = new ArrayList<eventVisitors>();
        databaseHelper.create_db();
        db = databaseHelper.open();
        try {
            cursor = db.rawQuery("select " +
                    "schedule_records.id, " +
                    "persons.fio, " +
                    "is_visited " +
                    "from " +
                    "schedule_records " +
                    "inner join " +
                    "persons on persons.id = person_id " +
                    "where event_id = "+  _id +";", null);
        }catch (Exception ex){
        }
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String fio = cursor.getString(cursor.getColumnIndex("fio"));
            String isVisited = cursor.getString(cursor.getColumnIndex("is_visited"));
            result.add(new eventVisitors(id, fio, isVisited));
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return result;
    }

    public void changeVisitedById(String id, boolean state, Context ctx){
        databaseHelper.create_db();
        db = databaseHelper.open();
        String crt_table_sql = "UPDATE " +
                "schedule_records " +
                "SET is_visited = '"+state+"' " +
                "where schedule_records.id = "+ id +";";
        db.execSQL(crt_table_sql);
        Toast toast = Toast.makeText(ctx,
                "Запись изменена", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void deleteVisit(String id, Context ctx){
        databaseHelper.create_db();
        db = databaseHelper.open();
        String crt_table_sql = "DELETE " +
                "from schedule_records " +
                "where schedule_records.id = "+ id +";";
        db.execSQL(crt_table_sql);
        Toast toast = Toast.makeText(ctx,
                "Посетитель удален", Toast.LENGTH_SHORT);
        toast.show();
    }
    public List<String> dropDownFios(String event_id){
        List<String> result = new ArrayList<String>();
        databaseHelper.create_db();
        db = databaseHelper.open();
        String crt_table_sql = "select " +
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
                "event_id = "+ event_id +" " +
                ");";
        try {
            cursor = db.rawQuery(crt_table_sql, null);
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

    public void addVisit(String event_id, String person_fio, Context ctx){
        databaseHelper.create_db();
        db = databaseHelper.open();
        String crt_table_sql = "INSERT INTO " +
                "schedule_records (event_id, is_visited, person_id) " +
                "VALUES("+event_id+", 'false', (select id from persons where fio = '"+person_fio+"'));";
        db.execSQL(crt_table_sql);
        Toast toast = Toast.makeText(ctx,
                "Посетитель добавлен", Toast.LENGTH_SHORT);
        toast.show();
    }
}