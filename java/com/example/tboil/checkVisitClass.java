package com.example.tboil;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tboil.tboilModels.eventsScheduleItem;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.example.tboil.tboilModels.*;

public class checkVisitClass extends AppCompatActivity implements View.OnClickListener {
    String Request;
    static TableLayout table;

    private tboilRepository rep;
    private boolean update;
    private ArrayList<TableRow> rows;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.check_visit_activity);
        }catch (Exception ex){


        }
        //databaseHelper = new DatabaseHelper(getApplicationContext());
        // создаем базу данных
        rep = new tboilRepository();
        rep.init(getApplicationContext());


        CheckVisitTableRequest();

    }
    public void kek(View v) {


    }
    public void CheckVisitTableRequest(){
        table = findViewById(R.id.table);
        //remove rows
        if(rows != null) {
            ArrayList<TableRow> removeRows = new ArrayList<>();
            for(int i = 0; i < rows.size(); i++) {
             removeRows.add(rows.get(i));
            }

// now delete those rows
            for(TableRow remove : removeRows) {
                table.removeView(remove);
                //rows.remove(remove);
            }
        }
        rows = new ArrayList<TableRow>();
        String __id = getIntent().getStringExtra("eventId");
        List<eventVisitors> eventVisitorsItems = rep.getVisitorsByShcheduleEvent(__id);

        update = true;
        for (eventVisitors item: eventVisitorsItems) {
            TableRow tr = new TableRow(this);
            rows.add(tr);
            tr.setBackgroundColor(Color.WHITE);

            View v = LayoutInflater.from(this).inflate(R.layout.visitorsrow_activity, tr, false);

            TextView id = (TextView)v.findViewById(R.id.id);
            id.setText(item.id);

            TextView fio = (TextView)v.findViewById(R.id.fio);
            fio.setText(item.fio);

            CheckBox isVisited = (CheckBox)v.findViewById(R.id.isvisited);
            String kek = item.isVisited.toString();
            boolean state;
            try{
                int int_state = Integer.parseInt(kek);
                if(int_state == 1)
                    state = true;
                else
                    state = false;
            }catch (Exception ex){
                state = Boolean.parseBoolean(kek);
            }
            isVisited.setChecked(state);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (update) return;
                    TextView id = (TextView)view.findViewById(R.id.id);
                    String _id = id.getText().toString();

                    CheckBox state = (CheckBox)view.findViewById(R.id.isvisited);
                    boolean _state = state.isChecked();

                    rep.changeVisitedById(_id, !_state);
                    CheckVisitTableRequest();
                }
            });
            tr.addView(v);

            table.addView(tr);
        }
        update = false;
    }




    @Override
    public void onClick(View view) {

    }
}
