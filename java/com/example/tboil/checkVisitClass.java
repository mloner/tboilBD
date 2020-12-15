package com.example.tboil;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tboil.tboilModels.eventsScheduleItem;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class checkVisitClass extends AppCompatActivity implements View.OnClickListener {
    String Request;
    static TableLayout table;

    private tboilRepository rep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_visit_activity);
        //databaseHelper = new DatabaseHelper(getApplicationContext());
        // создаем базу данных
        rep = new tboilRepository();
        rep.init(getApplicationContext());


        CheckVisitTableRequest();

    }
    public void kek(View v) {


    }
    public void CheckVisitTableRequest(){
        List<eventsScheduleItem> eventsSchedule = rep.getEventsSchedule();
        table = findViewById(R.id.table);

        for (eventsScheduleItem item:  eventsSchedule) {
            TableRow tr = new TableRow(this);
            tr.setBackgroundColor(Color.WHITE);

            View v = LayoutInflater.from(this).inflate(R.layout.eventrow_activity, tr, false);

            TextView id = (TextView)v.findViewById(R.id.id);
            id.setText(item.id);

            TextView eventName = (TextView)v.findViewById(R.id.eventName);
            eventName.setText(item.eventName);

            TextView hallName = (TextView)v.findViewById(R.id.hallName);
            hallName.setText(item.hallName);

            TextView startDatetime = (TextView)v.findViewById(R.id.startDateTime);
            try {
                DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(item.start_datetime);
                startDatetime.setText(dateFormat.format(date) + " (" + item.durationMins + " мин)");
            }catch (Exception ex){}

            tr.addView(v);

            table.addView(tr);
            table.findViewById(R.id.events_btn);
        }
    }




    @Override
    public void onClick(View view) {

    }
}
