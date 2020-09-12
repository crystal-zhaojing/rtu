/*
package com.example.rtu_ble.util;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;

public class EditTextUtil {


    */
/**
     * 插入数据
     *//*

    private static void insertData(String tempName, SQLiteDatabase db, ) {
        db = helper.getWritableDatabase();
        db.execSQL("insert into records(name) values('" + tempName + "')");
        db.close();
    }

    */
/**
     * 模糊查询数据
     *//*

    private static void queryData(String tempName) {
        Cursor cursor = helper.getReadableDatabase().rawQuery("select id as _id,name from records where name like '%" + tempName + "%' order by id desc ", null);
        adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, cursor, new String[]{"name"}, new int[]{android.R.id.text1}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    */
/**
     * 检查数据库中是否已经有该条记录
     *//*

    private static boolean hasData(String tempName) {
        Cursor cursor = helper.getReadableDatabase().rawQuery("select id as _id,name from records where name =?", new String[]{tempName});
        return cursor.moveToNext();
    }

    */
/**
     * 清空数据
     *//*

    private static void deleteData() {
        db = helper.getWritableDatabase();
        db.execSQL("delete from records");
        db.close();
    }
}
*/
