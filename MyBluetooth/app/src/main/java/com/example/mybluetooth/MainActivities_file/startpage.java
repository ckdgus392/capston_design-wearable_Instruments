package com.example.mybluetooth.MainActivities_file;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mybluetooth.Database_file.DatabaseHelper;
import com.example.mybluetooth.MainActivity;
import com.example.mybluetooth.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class startpage  extends AppCompatActivity {
    DatabaseHelper myDB;

    //timer declare
    TextView textView ;
    ImageButton start, pause;
    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L ;
    Handler handler;
    int Seconds, Minutes, MilliSeconds ;
    //

    ImageButton GO_calendar_view, GO_record_view, GO_setting_view;
    Button btn_studystart, btn_studyfinish, btn_studydelete, btn_vibrationinquiry, btn_vib_count_increment;
    TextView Show_Time_TextView, Viberation_count;
    long now = System.currentTimeMillis();
    Date date = new Date(now);

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm:ss");
    SimpleDateFormat sdf3 = new SimpleDateFormat("hh:mm:ss");
    String getTime = sdf.format(date);

    protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.startpage);
        myDB = new DatabaseHelper(this);

        textView = (TextView)findViewById(R.id.timer_textView);
        pause = (ImageButton)findViewById(R.id.button2);
        start = (ImageButton)findViewById(R.id.button3);

        handler = new Handler();

        Show_Time_TextView = (TextView) findViewById(R.id.Show_Time);
        ShowTimeMethod();
        //btn_vib_count_increment = (Button) findViewById(R.id.Viberation_increment);
        //Viberation_count = (TextView) findViewById(R.id.Viberation_count);
        //btn_vibrationinquiry = (Button) findViewById(R.id.Viberation_inquiry);
        btn_studystart = (Button)findViewById(R.id.study_start);
        btn_studyfinish = (Button)findViewById(R.id.study_finish);
        btn_studydelete = (Button)findViewById(R.id.study_delete);
        GO_calendar_view = (ImageButton)findViewById(R.id.btn_calenderview);
        GO_record_view = (ImageButton)findViewById(R.id.btn_recordview);

        startbtn_onclick();
        finishbtn_onclick();
        deletebtn_onclick();
        //Vibinquirybtn_onclick();
        //Vibrationincrement_onclick();
        //Log.v("오늘의 날짜와 현재시간", getTime + ", " + getTime2);
        //myDB.insertData_Study(getTime, getTime2);
        //myDB.updateData_Study(getTime, getTime2);
        //viewAll();

        /*
        myDB.insertData_meta("2022-11-17", "13:00:00","14:00:00", "60분 00초", 10);
        myDB.insertData_meta("2022-11-18", "14:00:00", "15:00:00", "60분 20초", 20);
        myDB.insertData_meta("2022-11-19", "20:00:00", "22:00:00", "120분 00초", 20);
        myDB.insertData_meta("2022-11-20", "17:00:00", "18:30:00", "90분 00초", 13);
        myDB.insertData_meta("2022-11-14", "10:00:00", "11:00:00", "60분 00초", 4);
        myDB.insertData_meta("2022-11-15", "13:00:00", "16:00:00", "180분 30초", 10);
        myDB.insertData_meta("2022-11-16", "12:00:00", "14:00:00", "43분 12초", 20);
        */
        //myDB.updateData_Vibration(getTime, "13:10:15"); //TODO 진동종료시간은 없애는게 어떤지?
        setGO_calendar_view();
        setGO_record_view();

        timer_start();
        timer_pause();
    }//oncreate() finish
    public void ShowTimeMethod() {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                Show_Time_TextView.setText(format.format(new Date()));
            }
        };
        Runnable task = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {}
                    handler.sendEmptyMessage(1);    //핸들러를 호출한다. 즉, 시간을 최신화 해준다.
                }
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }
    public void startbtn_onclick(){
        btn_studystart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //timer start
                StartTime = SystemClock.uptimeMillis();
                handler.postDelayed(runnable, 0);
                btn_studyfinish.setEnabled(false);
                btn_studydelete.setEnabled(false);
                btn_studystart.setEnabled(false);

                String getTime2 = sdf2.format(date);
                String add = Show_Time_TextView.getText().toString();
                myDB.insertData_Study(getTime, add);
                Toast.makeText(startpage.this, "학습시작완료" + getTime + " / " + add, Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void finishbtn_onclick(){
        btn_studyfinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btn_studystart.setEnabled(false);
                start.setEnabled(false);

                String getTime3 = sdf3.format(date);
                String add2 = Show_Time_TextView.getText().toString();
                myDB.updateData_Study(getTime, add2);
                String alltime = textView.getText().toString();
                myDB.updateData_Allstudytime(getTime, alltime);
                Toast.makeText(startpage.this, "학습종료되었습니다..\n" + getTime + " / " + add2 + " & 총학습시간:" + alltime, Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void  deletebtn_onclick(){
        btn_studydelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_studystart.setEnabled(true);
                start.setEnabled(true);

                //timer 초기화
                MillisecondTime = 0L ;
                StartTime = 0L ;
                TimeBuff = 0L ;
                UpdateTime = 0L ;
                Seconds = 0 ;
                Minutes = 0 ;
                MilliSeconds = 0 ;
                textView.setText("00분 00초");
                //
                myDB.deleteStudy(getTime);
                myDB.deleteVib(getTime);
                Toast.makeText(startpage.this, "\t\t\t\t\t오늘("+ getTime +") 기록된\n 학습내용과 진동기록이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    /*private void Vibinquirybtn_onclick(){
        btn_vibrationinquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                array_list = myDB.select_vibration();
                Log.v("testarray", array_list.toString());
                Viberation_count.setText(array_list.get(0).toString() + " 회");
            }
        });
    }*/

    public void setGO_calendar_view(){
        GO_calendar_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), calendar_MainActivity.class);
                startActivity(intent);
            }
        });
    }
    public void setGO_record_view(){
        GO_record_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), record_MainActivity.class);
                startActivity(intent);
            }
        });
    }


    public void timer_start() {
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartTime = SystemClock.uptimeMillis();
                handler.postDelayed(runnable, 0);
                btn_studydelete.setEnabled(false);
                btn_studyfinish.setEnabled(false);
            }
        });
    }

    /*
    public void Vibrationincrement_onclick(){
        btn_vib_count_increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String addtime = Show_Time_TextView.getText().toString();
                myDB.updateVibration_Study(getTime); //진동횟수+1
                myDB.insertData_Vibration(getTime, addtime);
            }
        });
    }
    */
    public void timer_pause(){
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimeBuff += MillisecondTime;
                handler.removeCallbacks(runnable);
                btn_studyfinish.setEnabled(true);
                btn_studydelete.setEnabled(true);
            }
        });
    }
    public Runnable runnable = new Runnable() {
        public void run() {
            MillisecondTime = SystemClock.uptimeMillis() - StartTime;
            UpdateTime = TimeBuff + MillisecondTime;
            Seconds = (int) (UpdateTime / 1000);
            Minutes = Seconds / 60;
            Seconds = Seconds % 60;
            //MilliSeconds = (int) (UpdateTime % 1000);

            textView.setText("" + Minutes + "분 "
                    + String.format("%02d", Seconds) + "초");

            handler.postDelayed(this, 0);
        }

    };
}