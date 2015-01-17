package com.sensis.what2eat.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.sensis.what2eat.TaskDTO;


public class TaskDBHelper extends SQLiteOpenHelper {
    private static TaskDBHelper dbHelper;

    public static TaskDBHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (dbHelper == null) {
            dbHelper = new TaskDBHelper(context.getApplicationContext());
        }
        return dbHelper;
    }

    private TaskDBHelper(Context context) {
        super(context, TaskContract.DB_NAME, null, TaskContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqlDB) {
        String sqlQuery =
                String.format("CREATE TABLE %s ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                "%s TEXT," +
                                "%s TEXT" +
                                ");",
                        TaskContract.TABLE, TaskContract.Columns.TASK_NAME, TaskContract.Columns.DESCRIPTION);
        Log.d("TaskDBHelper", "Query to form table: " + sqlQuery);
        sqlDB.execSQL(sqlQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqlDB, int oldVersion, int newVersion) {
        sqlDB.execSQL("DROP TABLE IF EXISTS " + TaskContract.TABLE);
        onCreate(sqlDB);
    }

    public TaskDTO getOneTask(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TaskContract.TABLE,
                new String[]{TaskContract.Columns._ID, TaskContract.Columns.TASK_NAME, TaskContract.Columns.DESCRIPTION},
                String.format("%s = '%s'", TaskContract.Columns._ID, id),
                null, null, null, null);
        cursor.moveToFirst();

        return new TaskDTO(cursor.getString(0), cursor.getString(1), cursor.getString(2));
    }

    public void updateOneTask(TaskDTO task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.clear();
        values.put(TaskContract.Columns.TASK_NAME, task.getName());
        values.put(TaskContract.Columns.DESCRIPTION, task.getDescription());

        db.updateWithOnConflict(TaskContract.TABLE, values,
                String.format("%s = '%s'", TaskContract.Columns._ID, task.getId()),
                null, SQLiteDatabase.CONFLICT_IGNORE);
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
