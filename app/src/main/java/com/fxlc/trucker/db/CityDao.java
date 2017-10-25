package com.fxlc.trucker.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.fxlc.trucker.bean.City;
import com.fxlc.trucker.bean.Truck;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * Created by cyd on 2017/7/19.
 */

public class CityDao {
    MySqliteHelper helper;
    SQLiteDatabase db;

    public CityDao(Context context) {
        helper = new MySqliteHelper(context);
    }

    public void readExcel(InputStream is) {
        db = helper.getWritableDatabase();
        try {

            Workbook book = Workbook.getWorkbook(is);
            Sheet sheet = book.getSheet(0);

            int rows = sheet.getRows();
            int cols = sheet.getColumns();
            // getCell(Col,Row)获得单元格的值
            Log.d("excel", "rows:" + rows);
            for (int i = 1; i < rows; i++) {

                String id = sheet.getCell(0, i).getContents();
                String parentId = sheet.getCell(1, i).getContents();
                String name = sheet.getCell(2, i).getContents();
                String type = sheet.getCell(3, i).getContents();

                String sql = "insert into city(id,name,parent_id,type) values (?,?,?,?)";
                db.execSQL(sql, new String[]{id, name, parentId, type});

            }
            book.close();
            db.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void del() {

        String sql = "DELETE FROM city";
        db = helper.getWritableDatabase();
        db.execSQL(sql);

    }

    public boolean hasData() {
        db = helper.getWritableDatabase();

        String sql = "select count(*) c from CITY";
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToNext()) {
            if (cursor.getInt(cursor.getColumnIndex("c")) > 0)
                return true;
        }
        return false;


    }

    public List<City> getCity(String  pid) {
        db = helper.getWritableDatabase();
        String sql = "select * from city where parent_id = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{pid });
        List<City> cities = new ArrayList<>();
        while (cursor.moveToNext()) {
            City city = new City();
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String id = cursor.getString(cursor.getColumnIndex("id"));

            city.setId(id);
            city.setName(name);
            city.setParentId(pid);
            cities.add(city);
        }
        cursor.close();
        return cities;
    }
    public List<City> getAllCity() {
        db = helper.getWritableDatabase();
        String sql = "select * from city";
        Cursor cursor = db.rawQuery(sql, null);
        List<City> cities = new ArrayList<>();
        while (cursor.moveToNext()) {
            City city = new City();
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String pid = cursor.getString(cursor.getColumnIndex("parent_id"));
            city.setId(id);
            city.setName(name);
            city.setParentId(pid);
            cities.add(city);
        }
        cursor.close();
        return cities;
    }
}
