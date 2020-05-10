package com.ansh1999.tasks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class Start extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        getSupportActionBar().hide();
        SharedPreferences sharedPreferences=getSharedPreferences("mypreferences",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        if(sharedPreferences.getBoolean("first",true)){
            Intent intent = new Intent(this,FirstActivity.class);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }
    }
}
