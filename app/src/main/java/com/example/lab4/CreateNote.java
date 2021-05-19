package com.example.lab4;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CreateNote extends AppCompatActivity implements View.OnClickListener{

    TextView currentDateTime, ed;
    Calendar dateAndTime=Calendar.getInstance();

    DBHelper dbHelper;
    Button btnAdd, btnRead, btnClear;
    EditText etName, etDescription;

    LinearLayout linearLayoutTags;

    SQLiteDatabase database;

    Map<Integer, Boolean> tagsValue = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);
        currentDateTime=(TextView)findViewById(R.id.currentDateTime);
        setInitialDateTime();


        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);
        
        btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener((View.OnClickListener) this);

        etName = (EditText) findViewById(R.id.etName);
        etDescription = (EditText) findViewById(R.id.etDescription);

        dbHelper = new DBHelper(this);

        linearLayoutTags = findViewById(R.id.layoutTags);

        database = dbHelper.getWritableDatabase();

        Cursor cursorTag = database.query(DBHelper.TABLE_TAGS, null, null, null, null, null, null);

        if (cursorTag.moveToFirst()) {
            final int idIndexTag = cursorTag.getColumnIndex(DBHelper.KEY_ID_TAG);
            int nameIndexTag = cursorTag.getColumnIndex(DBHelper.KEY_NAME_TAG);
            do {
                final Button button = new Button(this);
                button.setText(cursorTag.getString(nameIndexTag));
                button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                if (tagsValue.containsKey(Integer.valueOf(cursorTag.getString(idIndexTag))) && tagsValue.get(Integer.valueOf(cursorTag.getString(idIndexTag)))) {
                    Log.d("containsKey and idIndexTag", cursorTag.getString(idIndexTag));
                    button.setBackgroundColor(Color.GREEN);
                } else {
                    button.setBackgroundColor(Color.RED);
                }
                final String idTag = cursorTag.getString(idIndexTag);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ColorDrawable colorDrawable = (ColorDrawable) button.getBackground();
                        if (colorDrawable.getColor() == Color.RED) {
                            tagsValue.put(Integer.valueOf(idTag), true);
                            button.setBackgroundColor(Color.GREEN);
                        } else if (colorDrawable.getColor() == Color.GREEN) {
                            tagsValue.put(Integer.valueOf(idTag), false);
                            button.setBackgroundColor(Color.RED);
                        }
                    }
                });
                linearLayoutTags.addView(button);
            } while (cursorTag.moveToNext());
        } else
            Log.d("mLog", "0 rows");


        cursorTag.close();
        dbHelper.close();

    }


    @Override
    public void onClick(View v) {

        String name = etName.getText().toString();

        String date = currentDateTime.getText().toString();

        String description = etDescription.getText().toString();

        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        ContentValues contentValuesTag = new ContentValues();

        switch (v.getId()) {

            case R.id.btnAdd:

                contentValues.put(DBHelper.KEY_NAME_NOTE, name);
                contentValues.put(DBHelper.KEY_DATE_NOTE, date);
                contentValues.put(DBHelper.KEY_DESCRIPTION_NOTE, description);
                Integer idNote = Math.toIntExact(database.insert(DBHelper.TABLE_NOTES, null, contentValues));

                for (Map.Entry<Integer, Boolean> entry : tagsValue.entrySet()) {

                    if (entry.getValue() == true) {
                        contentValuesTag.put(DBHelper.KEY_TAG_ID, entry.getKey().toString());
                        contentValuesTag.put(DBHelper.KEY_NOTE_ID, idNote);
                        database.insert(DBHelper.TABLE_TAGS_NOTES, null, contentValuesTag);
                    }
                }
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Заметка создана",
                        Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                break;

            case R.id.btnClear:
                database.delete(DBHelper.TABLE_NOTES, null, null);
                break;
        }
        dbHelper.close();

    }
    public void setDate(View v) {
        new DatePickerDialog(CreateNote.this, d,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    DatePickerDialog.OnDateSetListener d=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTime();
        }
    };

    public void setTime(View v) {
        new TimePickerDialog(CreateNote.this, t,
                dateAndTime.get(Calendar.HOUR_OF_DAY),
                dateAndTime.get(Calendar.MINUTE), true)
                .show();
    }
    private void setInitialDateTime() {

        currentDateTime.setText(DateUtils.formatDateTime(this,
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR
                        | DateUtils.FORMAT_SHOW_TIME));
    }
    TimePickerDialog.OnTimeSetListener t=new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateAndTime.set(Calendar.MINUTE, minute);
            setInitialDateTime();
        }
    };

    public void goToNoteActivityCreateActivity(View view) {
        Intent intent = new Intent(this, Note.class);
        startActivity(intent);
    }

    public void goToTagActivityCreateActivity(View view) {
        Intent intent = new Intent(this, Tag.class);
        startActivity(intent);
    }

}