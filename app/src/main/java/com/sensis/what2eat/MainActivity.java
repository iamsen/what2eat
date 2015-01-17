package com.sensis.what2eat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.sensis.what2eat.db.TaskContract;
import com.sensis.what2eat.db.TaskDBHelper;

import java.util.Random;


public class MainActivity extends ActionBarActivity {
    public final static String EXTRA_MESSAGE = "com.sensis.what2eat.MESSAGE";
    public final static String EXTRA_ID = "com.sensis.what2eat.ID";
    private TaskDBHelper helper;
    private ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper = TaskDBHelper.getInstance(MainActivity.this);
        setContentView(R.layout.activity_main);
        updateUI();
    }

    private void updateUI() {
        Cursor cursor = helper.getAllTaskNamesAndIds();

        listAdapter = new SimpleCursorAdapter(
                this,
                R.layout.task_view,
                cursor,
                new String[]{TaskContract.Columns.TASK_NAME, TaskContract.Columns._ID},
                new int[]{R.id.taskTextView, R.id.idHolder},
                0);

        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(listAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_task:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Add an Entry");
                builder.setMessage("What is the name?");
                final EditText inputField = new EditText(this);
                builder.setView(inputField);
                builder.setPositiveButton("Add",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String taskName = inputField.getText().toString();
//                                Log.d("MainActivity", taskName);
                                if (taskName.trim().equals("")) {
                                    new AlertDialog.Builder(MainActivity.this)
                                            .setTitle("Name can not be empty...")
                                            .setPositiveButton("OK", null)
                                            .show();
                                    return;
                                }
                                helper.insertItem(taskName.trim());
                                updateUI();
                            }
                        });
                builder.setNegativeButton("Cancel", null);
                builder.show();
                return true;
            default:
                return false;
        }
    }

    public void onReady2EatButtonClick(View view) {
        Cursor cursor = helper.getAllTaskNames();
        int numPlaces = cursor.getCount();

        if (numPlaces < 1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Can't generate the result...")
                    .setMessage("Please enter at least one place...")
                    .setPositiveButton("OK", null)
                    .show();
            return;
        }

        int pickedIndex = randInt(1, numPlaces);
        assert (pickedIndex >= 1);
        cursor.moveToFirst();
        cursor.move(pickedIndex - 1);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("This is the place to go today!")
                .setMessage(cursor.getString(0))
                .setPositiveButton("OK", null)
                .show();
    }

    public void onEditButtonClick(View view) {
        View v = (View) view.getParent();
        Intent intent = new Intent(this, EditTaskActivity.class);
        TextView taskTextView = (TextView) v.findViewById(R.id.idHolder);
        intent.putExtra(EXTRA_ID, taskTextView.getText().toString());
        startActivity(intent);
    }

    public static int randInt(int min, int max) {
        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

}
