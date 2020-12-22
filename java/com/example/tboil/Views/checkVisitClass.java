package com.example.tboil.Views;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.example.tboil.Models.eventVisitorsModel;
import com.example.tboil.R;
import com.example.tboil.Repos.tboilRepository;

public class checkVisitClass extends AppCompatActivity implements View.OnClickListener {
    String Request;
    static TableLayout table;

    private tboilRepository rep;
    private boolean update;
    private ArrayList<TableRow> rows;
    private String __id;
    private List<eventVisitorsModel> eventVisitorsItems;

    private Button addVisitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_check_visit_main);
        }catch (Exception ex){
            int f = 123;
        }
        rep = new tboilRepository();
        rep.init(getApplicationContext());


        CheckVisitTableRequest();
        fillFiosList();

        //get event name
        String name = getIntent().getStringExtra("eventName");
        setTitle(name);
        addVisitButton = (Button)findViewById(R.id.addVisitButton);
        addVisitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Spinner fio = (Spinner) findViewById(R.id.fios);
                String _fio = fio.getSelectedItem().toString();


                if(rep.addVisit(__id, _fio)){
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Посетитель добавлен", Toast.LENGTH_SHORT);
                    toast.show();
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Что-то пошло не так", Toast.LENGTH_SHORT);
                    toast.show();
                }
                CheckVisitTableRequest();
                fillFiosList();
            }
        });
    }

    private void fillFiosList(){
        List<String> lst = rep.getNonVisitorsFiosByEventid(__id);
        Spinner dropdown = findViewById(R.id.fios);
        String[] items = lst.toArray(new String[0]);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
    }
    public void CheckVisitTableRequest(){
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
        __id = getIntent().getStringExtra("eventId");
        eventVisitorsItems = rep.getVisitorsByShcheduleEvent(__id);


        int visitedCount = 0;
        update = true;
        for (eventVisitorsModel item: eventVisitorsItems) {
            TableRow tr = new TableRow(this);
            rows.add(tr);
            tr.setBackgroundColor(Color.WHITE);

            View v = LayoutInflater.from(this).inflate(R.layout.activity_check_visit_row, tr, false);

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
            if(state){
                visitedCount++;
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

                    if(rep.changeVisitedById(_id, !_state)){
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Запись изменена", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    else{
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Что-то пошло не так", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    CheckVisitTableRequest();
                }
            });

            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (update) return true;
                    TextView id = (TextView)view.findViewById(R.id.id);
                    String _id = id.getText().toString();

                    if(rep.deleteVisit(_id)){
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Посетитель удален", Toast.LENGTH_SHORT);
                        toast.show();
                    }else{
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Что-то пошло не так", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    CheckVisitTableRequest();
                    fillFiosList();
                    return true;
                }
            });
            tr.addView(v);

            table.addView(tr);
        }
        //update total people count
        TextView tv = findViewById(R.id.totalPeopleCount);
        tv.setText("Пришло " + visitedCount + " из "  + rep.getVisitorsCountByEventId(__id));

        update = false;
    }




    @Override
    public void onClick(View view) {

    }
}
