package com.ansh1999.tasks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class FirstActivity extends AppCompatActivity {
    private Button enter;
    private EditText name;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        enter=findViewById(R.id.enter);
        name=findViewById(R.id.name);
        sharedPreferences=getSharedPreferences("mypreferences",MODE_PRIVATE);
        editor=sharedPreferences.edit();
        final Intent intent = new Intent(this,MainActivity.class);
            enter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editor.putString("name", name.getText().toString());
                    editor.putBoolean("first",false);
                    editor.commit();
                    startActivity(intent);
                }
            });
        }
    }
