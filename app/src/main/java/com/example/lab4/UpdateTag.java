package com.example.lab4;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;


public class UpdateTag extends AppCompatActivity{

    SQLiteDatabase database;
    DBHelper dbHelper;
    ContentValues contentValues;
    String strId;
    LinearLayout layoutNotes;

    EditText etName;

    List<String> listNotes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_tag);
        dbHelper = new DBHelper(this);

        database = dbHelper.getWritableDatabase();
        etName = (EditText) findViewById(R.id.etName);

        Bundle arguments = getIntent().getExtras();

        strId = String.valueOf(arguments.get("id"));

        layoutNotes = (LinearLayout) findViewById(R.id.layoutNotes);

        Cursor cursorTag = database.rawQuery("select * from " + DBHelper.TABLE_TAGS + " where " +
                DBHelper.KEY_ID_TAG + "=?", new String[]{strId});

        final int idIndexTag = cursorTag.getColumnIndex(DBHelper.KEY_ID_TAG);
        int nameIndexTag = cursorTag.getColumnIndex(DBHelper.KEY_NAME_TAG);
        cursorTag.moveToNext();
        etName.setText(cursorTag.getString(nameIndexTag));

        Cursor cursorTagNote = database.rawQuery("select * from " + DBHelper.TABLE_TAGS_NOTES + " where " +
                DBHelper.KEY_TAG_ID + "=?", new String[]{strId});
        int indexTag = cursorTagNote.getColumnIndex(DBHelper.KEY_TAG_ID);
        if (cursorTagNote.moveToFirst()){
            do{
                listNotes.add(cursorTagNote.getString(idIndexTag));
            }while (cursorTagNote.moveToNext());
        }

        final Cursor cursor = database.query(DBHelper.TABLE_NOTES, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            final int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID_NOTE);
            int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME_NOTE);
            int dateIndex = cursor.getColumnIndex(DBHelper.KEY_DATE_NOTE);
            int descriptionIndex = cursor.getColumnIndex(DBHelper.KEY_DESCRIPTION_NOTE);
            do {
                if (listNotes.contains(cursor.getString(idIndex))) {
                    LinearLayout linearLayout = new LinearLayout(this);
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                    linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                    TextView textViewName = new TextView(this);
                    textViewName.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    textViewName.setText(cursor.getString(nameIndex));
                    linearLayout.addView(textViewName);

                    TextView textViewDate = new TextView(this);
                    textViewDate.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    textViewDate.setText(cursor.getString(dateIndex));
                    linearLayout.addView(textViewDate);

                    TextView textViewDescription = new TextView(this);
                    textViewDescription.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    textViewDescription.setText(cursor.getString(descriptionIndex));
                    linearLayout.addView(textViewDescription);

                    final String id = cursor.getString(idIndex);

                    layoutNotes.addView(linearLayout);
                }

            } while (cursor.moveToNext());
        } else
            Log.d("mLog", "0 rows");


    }

    public void goToNoteActivityUpdateTag(View view) {
        Intent intent = new Intent(this, Note.class);
        startActivity(intent);
    }

    public void goToTagActivityUpdateTag(View view) {
        Intent intent = new Intent(this, Tag.class);
        startActivity(intent);
    }


    public void addNewTag(View view){
        database = dbHelper.getWritableDatabase();

        contentValues = new ContentValues();
        contentValues.put(DBHelper.KEY_NAME_TAG, etName.getText().toString());

        database.update(DBHelper.TABLE_TAGS, contentValues, DBHelper.KEY_ID_TAG + "=" + strId, null);

        Toast toast = Toast.makeText(getApplicationContext(),
                "Тэг обновлён",
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public void deleteTag(View view){
        database.delete(DBHelper.TABLE_TAGS_NOTES, DBHelper.KEY_TAG_ID + "=" + strId, null);
        database.delete(DBHelper.TABLE_TAGS, DBHelper.KEY_ID_TAG + "=" + strId, null);
        Toast toastDelete = Toast.makeText(getApplicationContext(),
                "Тэг удален",
                Toast.LENGTH_SHORT);
        toastDelete.setGravity(Gravity.CENTER, 0, 0);
        toastDelete.show();
        Intent intent = new Intent(this, Tag.class);
        startActivity(intent);
    }
    /*@Override
    public void onClick(View v) {
        database = dbHelper.getWritableDatabase();

        contentValues = new ContentValues();
        Toast toast1 = Toast.makeText(getApplicationContext(),
                "Тэг ",
                Toast.LENGTH_SHORT);
        toast1.setGravity(Gravity.CENTER, 0, 0);
        toast1.show();

        switch (v.getId()) {

            case R.id.btnAdd:

                contentValues.put(DBHelper.KEY_NAME_TAG, etName.getText().toString());

                database.update(DBHelper.TABLE_TAGS, contentValues, DBHelper.KEY_ID_TAG + "=" + strId, null);

                Toast toast = Toast.makeText(getApplicationContext(),
                        "Тэг обновлён",
                        Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                break;
            case R.id.btnClear:
                Toast toastDelete = Toast.makeText(getApplicationContext(),
                        "Тэг удален",
                        Toast.LENGTH_SHORT);
                toastDelete.setGravity(Gravity.CENTER, 0, 0);
                toastDelete.show();

                break;
        }

        dbHelper.close();
    }*/
}