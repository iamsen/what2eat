package com.sensis.what2eat.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class TaskDBHelper extends SQLiteOpenHelper {

    public TaskDBHelper(Context context) {
        super(context, TaskContract.DB_NAME, null, TaskContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqlDB) {
        String sqlQuery =
                String.format("CREATE TABLE %s ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "%s TEXT)", TaskContract.TABLE, TaskContract.Columns.TASK);
        Log.d("TaskDBHelper", "Query to form table: " + sqlQuery);
        sqlDB.execSQL(sqlQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqlDB, int i, int i2) {
        sqlDB.execSQL("DROP TABLE IF EXISTS " + TaskContract.TABLE);
        onCreate(sqlDB);
    }

    public Cursor getOneTask(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TaskContract.TABLE,
                new String[]{TaskContract.Columns._ID, TaskContract.Columns.TASK},
                String.format("%s = '%s'", TaskContract.Columns._ID, id),
                null, null, null, null);
        cursor.moveToFirst();
        return cursor;
    }

    public Cursor getAllTaskNames() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TaskContract.TABLE, new String[]{TaskContract.Columns.TASK}, null, null, null, null, null);
    }

    public Cursor getAllTaskNamesAndIds() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TaskContract.TABLE, new String[]{TaskContract.Columns._ID, TaskContract.Columns.TASK},
                null, null, null, null, null);
    }

    public void insertItem(String taskName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.clear();
        values.put(TaskContract.Columns.TASK, taskName);

        db.insertWithOnConflict(TaskContract.TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public void deleteItem(String id) {
        String sql = String.format("DELETE FROM %s WHERE %s = '%s'",
                TaskContract.TABLE,
                TaskContract.Columns._ID,
                id);

        SQLiteDatabase sqlDB = this.getWritableDatabase();
        sqlDB.execSQL(sql);
    }
}
