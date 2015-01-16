package com.sensis.what2eat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.sensis.what2eat.db.TaskContract;
import com.sensis.what2eat.db.TaskDBHelper;


public class EditTaskActivity extends ActionBarActivity {

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        id = intent.getStringExtra(MainActivity.EXTRA_ID);
        setContentView(R.layout.edit_task_view);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItemId = item.getItemId();

        if (menuItemId == R.id.action_delete) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("DELETE")
                    .setMessage("Do you want to delete it?")
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            TaskDBHelper helper = new TaskDBHelper(EditTaskActivity.this);
                            helper.deleteItem(helper.getOneTask(id).getId());
                            Intent intent = new Intent(EditTaskActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onSaveButtonClick(View view) {

    }

    public void onCancelButtonClick(View view) {
        Intent intent = new Intent(EditTaskActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
