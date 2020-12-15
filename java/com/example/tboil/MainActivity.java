package com.example.tboil;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    DatabaseHelper databaseHelper;

    public static int requestId;

    // нажатая кнопка
    public static final int ScheduleRequest = 1;
    public static final int CheckVisitRequest = 2;


    Button ScheduleRequestButton;
    Button CheckVisitButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ScheduleRequestButton = findViewById(R.id.events_btn);
        ScheduleRequestButton.setOnClickListener(this);
        CheckVisitButton = findViewById(R.id.check_visit_btn);
        CheckVisitButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.events_btn:
                Intent events = new Intent(MainActivity.this, eventsClass.class);
                startActivity(events);
                break;
            case R.id.check_visit_btn:
                Intent checkVisitors = new Intent(MainActivity.this, checkVisitClass.class);
                startActivity(checkVisitors);
                break;
        }
    }
}
