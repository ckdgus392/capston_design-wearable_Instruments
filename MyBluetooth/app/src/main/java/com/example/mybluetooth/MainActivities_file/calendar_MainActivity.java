package com.example.mybluetooth.MainActivities_file;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mybluetooth.Database_file.DatabaseHelper;
import com.example.mybluetooth.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class calendar_MainActivity extends AppCompatActivity {

    DatabaseHelper myDB;

    private CalendarView calendarview;
    private Button study_list_btn;
    private SimpleDateFormat simpleDateFormat;
    private String s_date;
    private Calendar c_day;
    private TextView text_date_input, text_studystart_input, text_studyfinish_input, text_allstudytime_input, text_allVibration_input;

    private int year_data, month_data, day_data;
    private String year_data_st, month_data_st, day_data_st, concat_date;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.calendar_view);

        myDB = new DatabaseHelper(this);

        calendarview = (CalendarView)findViewById(R.id.calendarView_xml);
        study_list_btn = (Button)findViewById(R.id.study_list_btn);
        text_date_input = (TextView)findViewById(R.id.text_date_input);
        text_studystart_input = (TextView)findViewById(R.id.text_studystart_input);
        text_studyfinish_input = (TextView)findViewById(R.id.text_studyfinish_input);
        text_allstudytime_input = (TextView)findViewById(R.id.text_allstudytime_input);
        text_allVibration_input = (TextView)findViewById(R.id.text_allVibration_input);

        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);



        Calendar calendar = Calendar.getInstance();
        s_date = simpleDateFormat.format(calendar.getTime()).toString();

        calendarview.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                year_data = year;
                year_data_st = String.valueOf(year);
                month_data = month + 1;
                month_data_st = String.valueOf(month_data);
                day_data = dayOfMonth;
                day_data_st = String.valueOf(day_data);
                concat_date = year_data_st + "-" + month_data_st + "-" + day_data_st;
            }
        });

        study_list_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("test창입니다 : ", year_data_st+ "년도 " + month_data_st + "월 " + day_data_st +"일 / " + concat_date);
                //text_date_input.setText(year_data +"-" + month_data + "-"+ day_data);

                SQLiteDatabase db = myDB.getReadableDatabase();
                Cursor res = db.rawQuery("SELECT * FROM Study_table WHERE 학습날짜=" + "'" + concat_date + "'", null);
                while(res.moveToNext()){
                    //num 행은 가장 첫번째에 있으니 0번이 되고, name은 1번

                    text_date_input.setText(res.getString(0));
                    text_studystart_input.setText(res.getString(1));
                    text_studyfinish_input.setText(res.getString(2));
                    text_allstudytime_input.setText(res.getString(3));
                    int bobo = res.getInt(4);
                    String bobo2 = String.valueOf(bobo);
                    text_allVibration_input.setText(bobo2 + "회");
                }
            }
        });
    }


}