package com.dangjang.dj2015.Manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;

import com.dangjang.dj2015.publicdata.AddressInfo;

import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Created by Tacademy on 2015-10-27.
 */
public class StorageManager {
    private static StorageManager manager;
    private SharedPreferences sharedPreferences;
    private SQLiteDatabase database;
    public final static int ADDRESS_SIZE = 5;
    public final static int SEARCH_SIZE = 5;
    public final static int PRODUCT_SIZE = 5;


    private StorageManager(Context context){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        database = context.openOrCreateDatabase("app.db",Context.MODE_WORLD_WRITEABLE,null);
        try{
            database.execSQL(
                    "create table HistoryAddress(" +
                            " id integer primary key AUTOINCREMENT, " +
                            " zoneid integer unique, " +
                            " address text" +
                            ");"
            );
            database.execSQL(
                    "create table HistorySearch(" +
                            " id integer primary key AUTOINCREMENT, " +
                            " search text unique" +
                            ");"
            );
            database.execSQL(
                    "create table HistoryProduct(" +
                            " id integer primary key AUTOINCREMENT, " +
                            " proid integer unique " +
                            ");"
            );
        }catch (Exception e){}
    }
    public static StorageManager getInstance(){
        if(manager == null) {
            manager = new StorageManager(MyApplication.getContext());
        }
        return manager;
    }
    public boolean setSettingValue(String key,String value){
        try {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key, value);
            editor.commit();
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public String getSettingValue(String key){
        return sharedPreferences.getString(key,null);
    }
    public ArrayList<AddressInfo> getHistoryAddress(){
        ArrayList<AddressInfo> arrayList = new ArrayList<>();
        Cursor cursor = null;
        try{
            cursor = database.rawQuery("select * from HistoryAddress order by id desc",null);
            while(cursor.moveToNext())
                arrayList.add(new AddressInfo(cursor.getInt(cursor.getColumnIndex("zoneid")),cursor.getString(cursor.getColumnIndex("address"))));
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            cursorClose(cursor);
        }
        return arrayList;
    }
    public void addHistoryAddress(int zoneid,String address){
        deleteHistoryAddress(zoneid);
        database.execSQL("insert into HistoryAddress(zoneid,address) values("+zoneid+",'"+address+"')");
        ArrayList<Integer> arrayList = null;
        Cursor cursor = null;
        try{
            cursor = database.rawQuery("select id from HistoryAddress order by id",null);
            if(cursor.getCount() > ADDRESS_SIZE){
                arrayList = new ArrayList<>();
                for(int i=0;i<(cursor.getCount()-ADDRESS_SIZE);i++){
                    cursor.moveToNext();
                    arrayList.add(cursor.getInt(0));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            cursorClose(cursor);
        }
        if(arrayList != null){
            for(int num:arrayList){
                database.execSQL("delete from HistoryAddress where id = "+num);
            }
        }
    }
    public void deleteHistoryAddress(int zoneid){
        database.execSQL("delete from HistoryAddress where zoneid = " + zoneid + "");
    }
    public ArrayList<String> getHistorySearch(){
        ArrayList<String> arrayList = new ArrayList<>();
        Cursor cursor = null;
        try{
            cursor = database.rawQuery("select * from HistorySearch order by id desc",null);
            while(cursor.moveToNext())
                arrayList.add(cursor.getString(cursor.getColumnIndex("search")));
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            cursorClose(cursor);
        }
        return arrayList;
    }
    public void addHistorySearch(String search){
        deleteHistorySearch(search);
        database.execSQL("insert into HistorySearch(search) values('"+search+"')");
        ArrayList<Integer> arrayList = null;
        Cursor cursor = null;
        try{
            cursor = database.rawQuery("select id from HistorySearch order by id",null);
            if(cursor.getCount() > SEARCH_SIZE){
                arrayList = new ArrayList<>();
                for(int i=0;i<(cursor.getCount()-SEARCH_SIZE);i++){
                    cursor.moveToNext();
                    arrayList.add(cursor.getInt(0));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            cursorClose(cursor);
        }
        if(arrayList != null){
            for(int num:arrayList){
                database.execSQL("delete from HistorySearch where id = "+num);
            }
        }
    }
    public void deleteHistorySearch(String search){
        database.execSQL("delete from HistorySearch where search = '" + search + "'");
    }
    public ArrayList<Integer> getHistoryProduct(){
        ArrayList<Integer> arrayList = new ArrayList<>();
        Cursor cursor = null;
        try{
            cursor = database.rawQuery("select * from HistoryProduct order by id desc",null);
            while(cursor.moveToNext())
                arrayList.add(cursor.getInt(cursor.getColumnIndex("proid")));
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            cursorClose(cursor);
        }
        return arrayList;
    }
    public void addHistoryProduct(int proid){
        deleteHistoryProduct(proid);
        database.execSQL("insert into HistoryProduct(proid) values("+proid+")");
        ArrayList<Integer> arrayList = null;
        Cursor cursor = null;
        try{
            cursor = database.rawQuery("select id from HistoryProduct order by id",null);
            if(cursor.getCount() > SEARCH_SIZE){
                arrayList = new ArrayList<>();
                for(int i=0;i<(cursor.getCount()-SEARCH_SIZE);i++){
                    cursor.moveToNext();
                    arrayList.add(cursor.getInt(0));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            cursorClose(cursor);
        }
        if(arrayList != null){
            for(int num:arrayList){
                database.execSQL("delete from HistoryProduct where id = "+num);
            }
        }
    }
    public void deleteHistoryProduct(int proid){
        database.execSQL("delete from HistoryProduct where proid = "+proid+"");
    }
    private void cursorClose(Cursor cursor){
        if(cursor!=null) {
            try {
                cursor.close();
            } catch (Exception e) {
            }
        }
    }
}
