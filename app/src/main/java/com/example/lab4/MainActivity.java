package com.example.lab4;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    @Override
    public void onClick(View v) {
    }

    public void goToNoteActivityMain(View view) {
        Intent intent = new Intent(this, Note.class);
        startActivity(intent);
    }
    public void goToTagActivityMain(View view) {
        Intent intent = new Intent(this, Tag.class);
        startActivity(intent);
    }
}