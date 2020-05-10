package com.ansh1999.tasks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

public class MainActivity extends AppCompatActivity {
    boolean service_status=false;
    Intent startIntent;
    Intent stopIntent;
    EditText edit_task;
    TextView my_task;
    Button button;
    SharedPreferences sharedPreferences;
    Editor editor;
    TextView header;
    private FirebaseAnalytics mFirebaseAnalytics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        service_status=isServiceRunning(ForegroundService.class);
        invalidateOptionsMenu();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        /*Bundle extras = getIntent().getExtras();
        if (extras!=null) {
            String status = extras.getString("notification_status", "0");
            if (status.equals("0"))
                service_status = false;
            else if (status.equals("1"))
                service_status = true;
            invalidateOptionsMenu();
        }*/
        edit_task=findViewById(R.id.edit_task);
        my_task=findViewById(R.id.my_task);
        button=findViewById(R.id.add_button);
        header=findViewById(R.id.header);
        sharedPreferences = getSharedPreferences("mypreferences",MODE_PRIVATE);
        editor=sharedPreferences.edit();

        my_task.setText(sharedPreferences.getString("my_task",""));
        header.setText(sharedPreferences.getString("name","My Tasks")+"'s Tasks");
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
                        Log.e("TASK",task);
                        String arr[]=task.split("\n");
                        Log.e("TASK ARR",arr[0]);
                        my_task.setText(task);
                        editor.putString("my_task",task);
                        editor.commit();
                        startIntent.putExtra("task",task);
                        startService(startIntent);
                        Toast.makeText(MainActivity.this, "New tasks added", Toast.LENGTH_SHORT).show();
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
                    String task=sharedPreferences.getString("my_task","");
                    startIntent.putExtra("task",task);
                    startService(startIntent);
                    item.setIcon(R.drawable.ic_notifications_active_white_24dp);
                    service_status=true;
                }
                else{
                    stopService(stopIntent);
                    item.setIcon(R.drawable.ic_notifications_off_white_24dp);
                    service_status=false;
                }
                break;
            }
            case R.id.edit_heading:{
                Intent intent = new Intent(this,Start.class);
                editor.putBoolean("first",true);
                editor.commit();
                startActivity(intent);
            }
        }
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item=menu.findItem(R.id.start_stop_service);
        if (service_status==false)
            item.setIcon(R.drawable.ic_notifications_off_white_24dp);
        else
            item.setIcon(R.drawable.ic_notifications_active_white_24dp);
        return true;
    }
    private boolean isServiceRunning(Class<?> serviceClass){
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo serviceInfo : manager.getRunningServices(Integer.MAX_VALUE)){
            if (serviceClass.getName().equals(serviceInfo.service.getClassName())) return true;
        }
        return false;
    }

}
