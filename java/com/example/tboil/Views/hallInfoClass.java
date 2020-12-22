package com.example.tboil.Views;

import android.content.Context;
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

import com.example.tboil.Models.HallInfoModel;
import com.example.tboil.Models.eventVisitorsModel;
import com.example.tboil.R;
import com.example.tboil.Repos.tboilRepository;

import java.util.ArrayList;
import java.util.List;

public class hallInfoClass extends AppCompatActivity implements View.OnClickListener {
    private tboilRepository rep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hall_info);

        //get hall name
        String name = getIntent().getStringExtra("hallName");
        setTitle(name);

        rep = new tboilRepository();
        rep.init(getApplicationContext());
        Context c = getApplicationContext();

        loadHallInfo(name);
    }

    private void loadHallInfo(String hallName){
        try {
            HallInfoModel hallInfo = rep.getHallInfo(hallName);

            TextView avgEventsCountPerDay = findViewById(R.id.avgEventsCountPerDay);
            avgEventsCountPerDay.setText("Среднее кол-во мероприятий в день: " + hallInfo.avgEventsCountPerDay);

            TextView placeInTop = findViewById(R.id.placeInTop);
            placeInTop.setText("Место в топе залов: " + Integer.parseInt(hallInfo.placeInTop)+1);

            TextView top5Tematics = findViewById(R.id.top5Tematics);
            top5Tematics.setText("Топ 5 тематик в зале: " + String.join(", ", hallInfo.top5Tematics));
        }catch (Exception e) {}
    }




    @Override
    public void onClick(View view) {

    }
}
