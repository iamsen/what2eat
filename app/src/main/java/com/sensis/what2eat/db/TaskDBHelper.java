package com.sensis.what2eat.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.sensis.what2eat.TaskDTO;


public class TaskDBHelper extends SQLiteOpenHelper {

    public TaskDBHelper(Context context) {
        super(context, TaskContract.DB_NAME, null, TaskContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqlDB) {
        String sqlQuery =
                String.format("CREATE TABLE %s ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                "%s TEXT" +
                                "%s TEXT" +
                                "%s TEXT" +
                                ")",
                        TaskContract.TABLE, TaskContract.Columns.TASK_NAME, TaskContract.Columns.PHONE_NUM, TaskContract.Columns.ADDRESS);
        Log.d("TaskDBHelper", "Query to form table: " + sqlQuery);
        sqlDB.execSQL(sqlQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqlDB, int i, int i2) {
        sqlDB.execSQL("DROP TABLE IF EXISTS " + TaskContract.TABLE);
        onCreate(sqlDB);
    }

    public TaskDTO getOneTask(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TaskContract.TABLE,
                new String[]{TaskContract.Columns._ID, TaskContract.Columns.TASK_NAME},
                String.format("%s = '%s'", TaskContract.Columns._ID, id),
                null, null, null, null);
        cursor.moveToFirst();

        return new TaskDTO(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
    }

    public Cursor getAllTaskNames() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TaskContract.TABLE, new String[]{TaskContract.Columns.TASK_NAME}, null, null, null, null, null);
    }

    public Cursor getAllTaskNamesAndIds() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TaskContract.TABLE, new String[]{TaskContract.Columns._ID, TaskContract.Columns.TASK_NAME},
                null, null, null, null, null);
    }

    public void insertItem(String taskName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.clear();
        values.put(TaskContract.Columns.TASK_NAME, taskName);

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
