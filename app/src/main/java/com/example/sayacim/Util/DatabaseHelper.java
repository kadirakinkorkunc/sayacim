package com.example.sayacim.Util;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "sayacim.db";// non sensitive for capital letters
    public static final String H_TABLE_NAME = "tatiller_table";
    public static final String R_TABLE_NAME = "hatirlaticilar_table";
    public static final String COL_ID = "id";
    public static final String COL_TITLE = "title";
    public static final String COL_DESC = "description";
    public static final String COL_DATE = "date";
    public static final String COL_TIME = "time";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 9);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_TATIL_TABLE = "CREATE TABLE " +
                H_TABLE_NAME + " ("+
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TITLE + " TEXT, " +
                COL_DESC +" TEXT, " +
                COL_TIME+ " TEXT, " +
                COL_DATE+ " TEXT" +
                ");";
        final String SQL_CREATE_HATIRLATICI_TABLE = "CREATE TABLE " +
                R_TABLE_NAME + " ("+
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TITLE + " TEXT, " +
                COL_DESC +" TEXT, " +
                COL_TIME+ " TEXT, " +
                COL_DATE+ " TEXT" +
                ");";
        db.execSQL(SQL_CREATE_TATIL_TABLE);
        db.execSQL(SQL_CREATE_HATIRLATICI_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+H_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+R_TABLE_NAME);
        onCreate(db);
    }

}

