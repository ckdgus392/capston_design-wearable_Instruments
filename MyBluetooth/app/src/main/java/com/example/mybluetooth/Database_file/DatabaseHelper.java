package com.example.mybluetooth.Database_file;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DatabaseHelper extends SQLiteOpenHelper {
    final static String DATABASE_NAME = "capston.db";  //데이터베이스 명 선언

    private String get_today_date;
    long now = System.currentTimeMillis();
    Date date = new Date(now);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    //-----------TODO 테이블 및 컬럼 정의 START-----------//
    //TODO 테이블 선언
    final static String  TABLE_User = "User_table"; //사용자 테이블 명
    final static String  TABLE_Study = "Study_table"; //학습 테이블 명
    final static String  TABLE_Subject = "Subject_table"; //학습과목 테이블 명
    final static String  TABLE_Vibration = "Vibration_table"; //진동시간 저장 테이블 명

    //TODO 사용자 설정 테이블(스키마설계) -- 없어도 될듯함.
    public static final String USER_COL_1_PK = "USER_ID"; //Sequence(1,2,3 ...)
    public static final String USER_COL_2 = "기울기각도";
    public static final String USER_COL_3 = "진동세기";
    public static final String USER_COL_4 = "블루투스ON_OFF";
    //TODO 학습 테이블(스키마설계)
    public static final String STUDY_COL_1_PK = "학습날짜";
    public static final String STUDY_COL_2 = "학습시작시간";
    public static final String STUDY_COL_3 = "학습종료시간";
    public static final String STUDY_COL_4 = "총학습시간";
    public static final String STUDY_COL_5 = "총진동횟수";
    //TODO 학습과목 테이블(스키마설계)
    public static final String SUBJECT_COL_1_PK = "학습과목_PK"; //Sequence(1,2,3 ...)
    public static final String SUBJECT_COL_2_FK = "학습날짜_FK_Sub";
    public static final String SUBJECT_COL_3 = "과목명";
    public static final String SUBJECT_COL_4 = "목표시간";
    //TODO 진동시간 저장 테이블(스키마설계)
    public static final String VIBRATION_COL_1_PK = "진동_PK"; //Sequence(1,2,3 ...)
    public static final String VIBRATION_COL_2_FK = "학습날짜_FK_Vib";
    public static final String VIBRATION_COL_3 = "진동시작시간";
    public static final String VIBRATION_COL_4 = "진동종료시간";
    //-----------TODO 테이블 및 컬럼 정의 FINISH-----------//

    @Override
    //TODO TABLE구축
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_User + "(USER_ID INTEGER PRIMARY KEY AUTOINCREMENT, 기울기각도 INTEGER DEFAULT 0, 진동세기 INTEGER DEFAULT 0, 블루투스ON_OFF TEXT DEFAULT 'off')");
        db.execSQL("CREATE TABLE " + TABLE_Study + "(학습날짜 TEXT PRIMARY KEY, 학습시작시간 TEXT NOT NULL, 학습종료시간 TEXT default 'x', 총학습시간 TEXT default 0, 총진동횟수 INTEGER default 0)");
        db.execSQL("CREATE TABLE " + TABLE_Subject + "(학습과목_PK TEXT PRIMARY KEY, 학습날짜_FK_Sub TEXT NOT NULL, 과목명 TEXT NOT NULL, 목표시간 INTEGER NOT NULL, FOREIGN KEY (학습날짜_FK_Sub) REFERENCES TABLE_Study(학습날짜))");
        db.execSQL("CREATE TABLE " + TABLE_Vibration + "(진동_PK INTEGER PRIMARY KEY AUTOINCREMENT, 학습날짜_FK_Vib TEXT NOT NULL, 진동시작시간 TEXT, 진동종료시간 TEXT, FOREIGN KEY (학습날짜_FK_Vib) REFERENCES TABLE_Study(학습날짜))");
    }

    @Override
    //TODO TABLE UPGRADE
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_User);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Study);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Subject);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Vibration);
        onCreate(db);
    }
    //임시데이터 insert
    public boolean insertData_meta(String 학습날짜, String 학습시작시간, String 학습종료시간, String 총학습시간, int 총진동횟수) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(STUDY_COL_1_PK, 학습날짜);
        contentValues.put(STUDY_COL_2, 학습시작시간);
        contentValues.put(STUDY_COL_3, 학습종료시간);
        contentValues.put(STUDY_COL_4, 총학습시간);
        contentValues.put(STUDY_COL_5, 총진동횟수);
        long result = db.insert(TABLE_Study, null, contentValues);
        if (result == -1)
            return false;
        else{
            return true;
        }
    }



    //TODO 기본설정 INSERT
    public boolean insertData_Study(String 학습날짜, String 학습시작시간) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(STUDY_COL_1_PK, 학습날짜);
        contentValues.put(STUDY_COL_2, 학습시작시간);
        long result = db.insert(TABLE_Study, null, contentValues);
        if (result == -1)
            return false;
        else{
            return true;
        }
    }
    public boolean insertData_Vibration(String 학습날짜, String 진동시작시간) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(VIBRATION_COL_2_FK, 학습날짜);
        contentValues.put(VIBRATION_COL_3, 진동시작시간);
        long result = db.insert(TABLE_Vibration, null, contentValues);
        if (result == -1)
            return false;
        else{
            return true;
        }
    }

    //todo 학습종료 시, 학습종료 시간을 update시켜준다.
    public void updateData_Study(String studydate, String studyfinish) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(STUDY_COL_1_PK, studydate);
        contentValues.put(STUDY_COL_3, studyfinish);

        db.update(TABLE_Study, contentValues, "학습날짜=?", new String[]{studydate});
        db.close();
    }
    //todo 학습종료 시, 총학습시간을 update시켜준다.
    public void updateData_Allstudytime(String studydate, String allstudytime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(STUDY_COL_1_PK, studydate);
        contentValues.put(STUDY_COL_4, allstudytime);

        db.update(TABLE_Study, contentValues, "학습날짜=?", new String[]{studydate});
        db.close();
    }

    //todo 학습종료 시, 진동종료시간을 update시켜준다.
    public void updateData_Vibration(String studydate, String vibrationfinish) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(VIBRATION_COL_2_FK, studydate);
        contentValues.put(VIBRATION_COL_4, vibrationfinish);

        db.update(TABLE_Vibration, contentValues, "학습날짜_FK_Vib=?", new String[]{studydate});
        db.close();
    }
    //todo 진동발생 시, 데이터를 받고 총진동횟수를 +1 증가시켜준다.
    public void updateVibration_Study(String studydate) {
        SQLiteDatabase db = this.getReadableDatabase();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String getTime = sdf.format(date);
        Cursor res = db.rawQuery("SELECT 총진동횟수 FROM "+ TABLE_Study + " WHERE 학습날짜=" + "'"+ getTime + "';", null);
        ContentValues contentValues = new ContentValues();
        if (res.moveToFirst()){
            int vibe = res.getInt(0);
            contentValues.put(STUDY_COL_1_PK, studydate);
            contentValues.put(STUDY_COL_5, vibe + 1);
        }
        db.update(TABLE_Study, contentValues, "학습날짜=?", new String[]{studydate});
        db.close();
    }

    public void deleteStudy(String studydate){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM Study_table WHERE 학습날짜=" + "'" + studydate + "'");
        db.close();
    }
    public void deleteVib(String studydate){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM Vibration_table WHERE 학습날짜_FK_Vib=" + "'" + studydate + "'");
    }

    public ArrayList select_vibration() {
        SQLiteDatabase db = this.getReadableDatabase();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String getTime = sdf.format(date);
        ArrayList<String> array_list = new ArrayList<String>();
        Cursor res = db.rawQuery("SELECT 총진동횟수 FROM "+ TABLE_Study + " WHERE 학습날짜=" + "'"+ getTime + "';", null);
        res.moveToFirst();
        while(res.isAfterLast() == false) {
            array_list.add(res.getString(0));
            res.moveToNext();
        }
        return array_list;
    }
}