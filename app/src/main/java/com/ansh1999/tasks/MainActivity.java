package com.ansh1999.tasks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static java.lang.Boolean.TRUE;

public class MainActivity extends AppCompatActivity {
    boolean service_status=true;
    Intent startIntent;
    Intent stopIntent;
    EditText edit_task;
    TextView my_task;
    Button button;
    SharedPreferences sharedPreferences;
    Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edit_task=findViewById(R.id.edit_task);
        my_task=findViewById(R.id.my_task);
        button=findViewById(R.id.add_button);
        sharedPreferences = getSharedPreferences("mytask",MODE_PRIVATE);
        editor=sharedPreferences.edit();

        my_task.setText(sharedPreferences.getString("my_task",""));

        startIntent = new Intent(this,ForegroundService.class);
        stopIntent = new Intent(this,ForegroundService.class);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        stopService(startIntent);
                        String task=edit_task.getText().toString();
                        my_task.setText(task);
                        editor.putString("my_task",task);
                        editor.commit();
                        startIntent.putExtra("task",task);
                        startService(startIntent);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                });

                builder.setMessage("Add new task? This will delete old tasks.");
                builder.setTitle("Confirm Adding New Task");

                AlertDialog d = builder.create();
                d.show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notification_option,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.start_stop_service:{
                if (service_status==false){
                    startService(startIntent);
                    item.setIcon(R.drawable.ic_notifications_active_white_24dp);
                    service_status=true;
                }
                else{
                    stopService(stopIntent);
                    item.setIcon(R.drawable.ic_notifications_off_white_24dp);
                    service_status=false;
                }
            }
        }
        return true;
    }
}
