package com.example.tboil.Views;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.tboil.Models.eventsScheduleItemModel;
import com.example.tboil.R;
import com.example.tboil.Repos.tboilRepository;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import java.util.Dictionary;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

public class scheduleEventsClass extends AppCompatActivity implements View.OnClickListener {
    String Request;
    static TableLayout table;
    private tboilRepository rep;

    public TextView dateFrom;
    public TextView dateTo;
    DatePickerDialog picker;

    Dictionary<Integer, Integer> ids;

    private ArrayList<TableRow> rows;

    Calendar dateAndTime=Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_events_main);

        dateFrom = (TextView)findViewById(R.id.dateFrom);
        dateTo = (TextView)findViewById(R.id.dateTo);

        dateFrom.setInputType(InputType.TYPE_NULL);
        dateTo.setInputType(InputType.TYPE_NULL);

        dateFrom.setText("22/07/2020");
        dateTo.setText("23/07/2020");

        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);

        dateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] _dateFrom = ((TextView) findViewById(R.id.dateFrom)).getText().toString().split("/");
                int day = Integer.parseInt(_dateFrom[0]);
                int month = Integer.parseInt(_dateFrom[1]);
                int year = Integer.parseInt(_dateFrom[2]);
                // date picker dialog
                picker = new DatePickerDialog(scheduleEventsClass.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                Date date = new GregorianCalendar(year, monthOfYear - 1, dayOfMonth).getTime();
                                DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                                dateFrom.setText(originalFormat.format(date));
                                try {
                                    if(originalFormat.parse(dateFrom.getText().toString())
                                            .after(originalFormat.parse(dateTo.getText().toString()))){
                                        Date df = originalFormat.parse(dateFrom.getText().toString());
                                        Calendar cal = Calendar.getInstance();
                                        cal.setTime(df);
                                        cal.add(Calendar.DATE, 1);
                                        dateTo.setText(originalFormat.format(cal.getTime()));
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                eventsScheduleTableRequest();
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        dateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] _dateTo = ((TextView) findViewById(R.id.dateTo)).getText().toString().split("/");
                int day = Integer.parseInt(_dateTo[0]);
                int month = Integer.parseInt(_dateTo[1]);
                int year = Integer.parseInt(_dateTo[2]);

                // date picker dialog
                picker = new DatePickerDialog(scheduleEventsClass.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                Date date = new GregorianCalendar(year, monthOfYear - 1, dayOfMonth).getTime();
                                DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                                dateTo.setText(originalFormat.format(date));
                                try {
                                    if(originalFormat.parse(dateFrom.getText().toString())
                                            .after(originalFormat.parse(dateTo.getText().toString()))){
                                        Date dt = originalFormat.parse(dateTo.getText().toString());
                                        Calendar cal = Calendar.getInstance();
                                        cal.setTime(dt);
                                        cal.add(Calendar.DATE, -1);
                                        dateFrom.setText(originalFormat.format(cal.getTime()));
                                    }

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                eventsScheduleTableRequest();
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        rep = new tboilRepository();
        rep.init(getApplicationContext());
        eventsScheduleTableRequest();
    }

    public String formatDateToDatetime(String _date){
        DateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try {
            date = originalFormat.parse(_date);
        }catch (Exception e){}
        String formattedDate = targetFormat.format(date);
        formattedDate += " 00:00:00";
        return formattedDate;
    }

    public void eventsScheduleTableRequest(){
        ids = new Hashtable<>();
        String dateFrom = formatDateToDatetime(this.dateFrom.getText().toString());
        String dateTo = formatDateToDatetime(this.dateTo.getText().toString());

        final List<eventsScheduleItemModel> eventsSchedule = rep.getEventsSchedule(dateFrom, dateTo);
        table = findViewById(R.id.table);

        //remove rows
        if(rows != null) {
            ArrayList<TableRow> removeRows = new ArrayList<>();
            for(int i = 0; i < rows.size(); i++) {
                removeRows.add(rows.get(i));
            }

            for(TableRow remove : removeRows) {
                table.removeView(remove);
            }
        }
        rows = new ArrayList<TableRow>();

        for (eventsScheduleItemModel item:  eventsSchedule) {
            TableRow tr = new TableRow(this);
            rows.add(tr);
            tr.setBackgroundColor(Color.WHITE);

            View v = LayoutInflater.from(this).inflate(R.layout.activity_schedule_events_row, tr, false);

            TextView id = (TextView)v.findViewById(R.id.id);
            id.setText(item.id);


            TextView eventName = (TextView)v.findViewById(R.id.eventName);
            eventName.setText(item.eventName);
            int newId = View.generateViewId();

            eventName.setId(newId);
            ids.put(newId, Integer.parseInt(item.id));

            eventName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = v.getId();
                    String evName = ((TextView) v).getText().toString();

                    Intent checkVisitors = new Intent(scheduleEventsClass.this, checkVisitClass.class);
                    checkVisitors.putExtra("eventId", String.valueOf(ids.get(id)));
                    checkVisitors.putExtra("eventName", evName);
                    startActivity(checkVisitors);
                }
            });





            TextView hallName = (TextView)v.findViewById(R.id.hallName);
            hallName.setText(item.hallName);

            TextView startDatetime = (TextView)v.findViewById(R.id.startDateTime);
            try {
                DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(item.start_datetime);
                String txt = dateFormat.format(date) + " (" + item.durationMins + " мин)";
                if(date.before(Calendar.getInstance().getTime())){
                    txt += "    [ПРОШЛО]";
                }
                startDatetime.setText(txt);

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
