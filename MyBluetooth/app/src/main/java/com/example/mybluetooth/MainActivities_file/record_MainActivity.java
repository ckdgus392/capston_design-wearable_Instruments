package com.example.mybluetooth.MainActivities_file;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mybluetooth.Database_file.DatabaseHelper;
import com.example.mybluetooth.ListviewAdapter_file.ListViewAdapter;
import com.example.mybluetooth.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class record_MainActivity extends AppCompatActivity {

    DatabaseHelper myDB;
    ArrayList RC_array_list;


    long now = System.currentTimeMillis();
    Date date = new Date(now);
    SimpleDateFormat ABC = new SimpleDateFormat("yyyy-MM-dd");
    String RC_getTime = ABC.format(date);

    TextView RC_Viberation_count;
    Button RC_btn_vibrationinquiry;
    ListView viblist;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.record_view);

        myDB = new DatabaseHelper(this);

        RC_Viberation_count = (TextView) findViewById(R.id.RC_viberation_count);
        RC_btn_vibrationinquiry = (Button) findViewById(R.id.RC_viberation_inquiry);
        viblist = (ListView)findViewById(R.id.lv_list);
        displayList();


        RC_Vibinquirybtn_onclick();
        displayList();
    }

    private void RC_Vibinquirybtn_onclick(){
        RC_btn_vibrationinquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RC_array_list = myDB.select_vibration();
                Log.v("testarray", RC_array_list.toString());
                RC_Viberation_count.setText("   총진동횟수 : " + RC_array_list.get(0).toString() + "회");
            }
        });
    }

    public void displayList(){
        SQLiteDatabase db = myDB.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT 진동시작시간 FROM Vibration_table WHERE 학습날짜_FK_Vib=" +"'" + RC_getTime + "'", null);
        //리스트뷰에 목록 채워주는 도구인 adapter준비
        ListViewAdapter adapter = new ListViewAdapter();
        while(res.moveToNext()){
            //num 행은 가장 첫번째에 있으니 0번이 되고, name은 1번
            adapter.addItemToList(res.getString(0));
        }

        //리스트뷰의 어댑터 대상을 여태 설계한 adapter로 설정
        viblist.setAdapter(adapter);
    }
}