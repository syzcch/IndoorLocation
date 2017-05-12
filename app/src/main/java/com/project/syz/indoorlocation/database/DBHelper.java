package com.project.syz.indoorlocation.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.sql.Date;


/**
 * Created by SYZ on 2016/9/4.
 */
public class DBHelper {

    private static final String TAG = "DBHelper";
    private static DBHelper singleDBHelper = null;

    private static SQLiteDatabase db;

    public synchronized  static DBHelper getInstance() {
        if (singleDBHelper == null) {
            singleDBHelper = new DBHelper();
        }
        return singleDBHelper;
    }

    public void CreateDB(File file){
        db = SQLiteDatabase.openOrCreateDatabase(file,null);
//        db = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE,null);
        Log.d(TAG, "Create DB  ok");
    }

    public void CreateTable(){
        try {
            db.execSQL("CREATE TABLE location (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "finger int,"
                    + "locX float,"
                    + "locY float,"
                    + "time date"
                    + ");");
            db.execSQL("CREATE TABLE wifiinfo (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "locX float,"
                    + "locY float,"
                    + "info varchar(500)"
                    + ");");
            CreateIndex();

            Log.d(TAG, "Create Table location ok");
        } catch (Exception e) {
            Log.d(TAG, "Create Table location err,table exists.");
            e.printStackTrace();
        }
    }

    public void CreateIndex(){
        try {
            db.execSQL(" CREATE UNIQUE INDEX unique_index_loc ON location (locX,locY)");
            Log.d(TAG, "Create UNIQUE INDEX on location ok");
        } catch (Exception e) {
            Log.d(TAG, "Create UNIQUE INDEX on location Error");
            e.printStackTrace();
        }
    }

    public void ReplaceInsertLoc(int finger,float locx, float locy, Date date){
        //    String sql = "INSERT INTO income values ( " + type + "," + money + "," + meno + "," + date + ")";
        try {
            db.execSQL("REPLACE INTO location values (null,?,?,?,?)",new Object[]{finger,locx,locy,date});
            Log.d(TAG, "insert into Table location ok");
        } catch (Exception e) {
            Log.d(TAG, "insert into Table location err!");
        }
    }

    public void InsertLocInfo(float locx, float locy, String str){
        //    String sql = "INSERT INTO income values ( " + type + "," + money + "," + meno + "," + date + ")";
        try {
            db.execSQL("REPLACE INTO wifiinfo values (null,?,?,?)",new Object[]{locx,locy,str});
            Log.d(TAG, "insert into Table wifiinfo ok");
        } catch (Exception e) {
            Log.d(TAG, "insert into Table wifiinfo err!");
        }
    }

    public Cursor QueryBySql(String sql){
        Cursor res = null;
        try {
            res = db.rawQuery("select * from location" ,null);
            Log.d(TAG, "QueryBySql search ok");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "QueryBySql search err!");
        }
        return res;
    }

    public void DropTable(){
        try {
            db.execSQL("drop TABLE location;");
            Log.d(TAG, "DropTable ok");
        } catch (Exception e) {
            Log.d(TAG, "DropTable err!");
            e.printStackTrace();
        }
    }

    public void ResetTable(){
        try {
            DropTable();
            CreateTable();
            Log.d(TAG, "ResetTable ok");
        } catch (Exception e) {
            Log.d(TAG, "ResetTable err!");
            e.printStackTrace();
        }
    }
}
