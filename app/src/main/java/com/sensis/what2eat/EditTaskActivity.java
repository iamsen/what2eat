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
import android.widget.EditText;
import android.widget.TextView;

import com.sensis.what2eat.db.TaskContract;
import com.sensis.what2eat.db.TaskDBHelper;


public class EditTaskActivity extends ActionBarActivity {

    private String id;
    private TaskDTO task;
    private TaskDBHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper = TaskDBHelper.getInstance(EditTaskActivity.this);

        Intent intent = getIntent();
        id = intent.getStringExtra(MainActivity.EXTRA_ID);
        task = helper.getOneTask(id);

        setContentView(R.layout.edit_task_view);
        ((EditText)findViewById(R.id.editName)).setText(task.getName());
        ((EditText)findViewById(R.id.editPhone)).setText(task.getPhone());
        ((EditText)findViewById(R.id.editAddress)).setText(task.getAddress());
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
                            helper.deleteItem(id);
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
        String newName = ((EditText)findViewById(R.id.editName)).getText().toString();
        if(newName.trim().equals("")) {
            new AlertDialog.Builder(EditTaskActivity.this)
                    .setTitle("Name can not be empty...")
                    .setPositiveButton("OK", null)
                    .show();
            return;
        }
        String newPhone = ((EditText)findViewById(R.id.editPhone)).getText().toString();
        String newAddress = ((EditText)findViewById(R.id.editAddress)).getText().toString();
        helper.updateOneTask(new TaskDTO(id, newName, newPhone, newAddress));
        new AlertDialog.Builder(EditTaskActivity.this)
                .setTitle("Successfully Saved!")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(EditTaskActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                })
                .show();
    }

    public void onCancelButtonClick(View view) {
        Intent intent = new Intent(EditTaskActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
