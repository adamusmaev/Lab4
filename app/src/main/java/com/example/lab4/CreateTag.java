package com.example.lab4;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class CreateTag extends AppCompatActivity implements View.OnClickListener{

    DBHelper dbHelper;
    Button btnAdd, btnRead, btnClear;
    EditText etName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tag);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener((View.OnClickListener) this);

        etName = (EditText) findViewById(R.id.etName);

        dbHelper = new DBHelper(this);

    }

    @Override
    public void onClick(View v) {

        String name = etName.getText().toString();

        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        switch (v.getId()) {

            case R.id.btnAdd:
                contentValues.put(DBHelper.KEY_NAME_TAG, name);

                database.insert(DBHelper.TABLE_TAGS, null, contentValues);
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Тег создан",
                        Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                break;



            case R.id.btnClear:
                database.delete(DBHelper.TABLE_TAGS, null, null);
                break;
        }
        dbHelper.close();
    }

    public void goToTagActivity(View view) {
        Intent intent = new Intent(this, Tag.class);
        startActivity(intent);
    }

    public void goToNoteActivity(View view) {
        Intent intent = new Intent(this, Note.class);
        startActivity(intent);
    }
}