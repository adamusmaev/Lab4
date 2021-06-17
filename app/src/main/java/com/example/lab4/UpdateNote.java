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

public class UpdateNote extends AppCompatActivity implements View.OnClickListener {

    TextView textView;
    DBHelper dbHelper;

    String name;
    String date;
    String description;
    String strId;

    EditText etName;
    EditText etDescription;
    TextView currentDateTime;

    LinearLayout linearLayoutTags;
    SQLiteDatabase database;
    ContentValues contentValues;
    Calendar dateAndTime = Calendar.getInstance();

    Button btnAdd;
    Button btnRead;
    Button btnClear;

    Map<Integer, Boolean> tagsValue = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_note);

        Bundle arguments = getIntent().getExtras();

        strId = (String) arguments.get("idIndex");

        dbHelper = new DBHelper(this);

        etDescription = (EditText) findViewById(R.id.etDescription);

        currentDateTime = (TextView) findViewById(R.id.currentDateTime);

        etName = (EditText) findViewById(R.id.etName);

        database = dbHelper.getWritableDatabase();

        contentValues = new ContentValues();

        Cursor cursor = database.rawQuery("select * from " + DBHelper.TABLE_NOTES + " where " +
                DBHelper.KEY_ID_NOTE + "=?", new String[]{strId});

        final int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID_NOTE);
        int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME_NOTE);
        int dateIndex = cursor.getColumnIndex(DBHelper.KEY_DATE_NOTE);
        int descriptionIndex = cursor.getColumnIndex(DBHelper.KEY_DESCRIPTION_NOTE);

        cursor.moveToNext();

        etName.setText(cursor.getString(nameIndex));

        etDescription.setText(cursor.getString(descriptionIndex));

        currentDateTime.setText(cursor.getString(dateIndex));

        name = etName.getText().toString();

        date = currentDateTime.getText().toString();

        description = etDescription.getText().toString();

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);


        btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener((View.OnClickListener) this);

        linearLayoutTags = findViewById(R.id.layoutTags);

        Cursor cursorTag1 = database.rawQuery("select * from " + DBHelper.TABLE_TAGS_NOTES + " where " +
                DBHelper.KEY_NOTE_ID + "=?", new String[]{strId});
        if (cursorTag1.moveToFirst()) {
            int idTag = cursorTag1.getColumnIndex(DBHelper.KEY_TAG_ID);
            int idNote = cursorTag1.getColumnIndex(DBHelper.KEY_NOTE_ID);
            do {
                Log.d("Map", tagsValue.toString());
                Log.d("Log:", "idNote: " + cursorTag1.getString(idNote) + ", idTag:" + cursorTag1.getString(idTag));
                tagsValue.put(Integer.valueOf(cursorTag1.getString(idTag)), true);
            } while (cursorTag1.moveToNext());
        } else
            Log.d("mLog", "0 rows!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");


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
                final Cursor finalCursorTag = cursorTag;
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

        database = dbHelper.getWritableDatabase();

        contentValues = new ContentValues();

        ContentValues contentValuesTag = new ContentValues();

        switch (v.getId()) {

            case R.id.btnAdd:

                contentValues.put(DBHelper.KEY_NAME_NOTE, etName.getText().toString());
                contentValues.put(DBHelper.KEY_DATE_NOTE, currentDateTime.getText().toString());
                contentValues.put(DBHelper.KEY_DESCRIPTION_NOTE, etDescription.getText().toString());

                for (Map.Entry<Integer, Boolean> entry : tagsValue.entrySet()) {

                    if (entry.getValue() == true) {
                        contentValuesTag.put(DBHelper.KEY_TAG_ID, entry.getKey().toString());
                        contentValuesTag.put(DBHelper.KEY_NOTE_ID, strId);
                        database.insert(DBHelper.TABLE_TAGS_NOTES, null, contentValuesTag);
                    }
                    if (entry.getValue() == false) {
                        database.delete(DBHelper.TABLE_TAGS_NOTES, DBHelper.KEY_NOTE_ID + "=" + String.valueOf(strId) + " AND " + DBHelper.KEY_TAG_ID + "=" + String.valueOf(entry.getKey()), null);
                    }
                }

                database.update(DBHelper.TABLE_NOTES, contentValues, DBHelper.KEY_ID_NOTE + "=" + String.valueOf(strId), null);

                Toast toast = Toast.makeText(getApplicationContext(),
                        "Заметка обновлена",
                        Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                break;

            case R.id.btnClear:
                database.delete(DBHelper.TABLE_TAGS_NOTES, DBHelper.KEY_NOTE_ID + "=" + strId, null);
                database.delete(DBHelper.TABLE_NOTES, DBHelper.KEY_ID_NOTE + "=" + strId, null);
                Toast toastDelete = Toast.makeText(getApplicationContext(),
                        "Заметка удалена",
                        Toast.LENGTH_SHORT);
                toastDelete.setGravity(Gravity.CENTER, 0, 0);
                toastDelete.show();
                Intent intent = new Intent(this, Note.class);
                startActivity(intent);
                break;
        }

        dbHelper.close();


    }

    public void goToNoteActivityUpdateActivity(View view) {
        Intent intent = new Intent(this, Note.class);
        startActivity(intent);
    }

    public void goToTagActivityUpdateActivity(View view) {
        Intent intent = new Intent(this, Tag.class);
        startActivity(intent);
    }

    public void setDate(View v) {
        new DatePickerDialog(UpdateNote.this, d,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTime();
        }
    };

    public void setTime(View v) {
        new TimePickerDialog(UpdateNote.this, t,
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

    TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateAndTime.set(Calendar.MINUTE, minute);
            setInitialDateTime();
        }
    };
}