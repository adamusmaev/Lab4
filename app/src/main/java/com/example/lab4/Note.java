package com.example.lab4;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class Note extends AppCompatActivity {

    DBHelper dbHelper;
    LinearLayout linearLayoutNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        linearLayoutNote = (LinearLayout) findViewById(R.id.lineNote);
        dbHelper = new DBHelper(this);

        final SQLiteDatabase database = dbHelper.getWritableDatabase();

        final Cursor cursor = database.query(DBHelper.TABLE_NOTES, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            final int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID_NOTE);
            int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME_NOTE);
            int dateIndex = cursor.getColumnIndex(DBHelper.KEY_DATE_NOTE);
            int descriptionIndex = cursor.getColumnIndex(DBHelper.KEY_DESCRIPTION_NOTE);
            do {

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

                Button button = new Button(this);
                button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                button.setText("Редактировать");
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Note.this, UpdateNote.class);
                        intent.putExtra("idIndex", id);
                        startActivity(intent);
                    }
                });
                linearLayout.addView(button);

                linearLayoutNote.addView(linearLayout);


            } while (cursor.moveToNext());
        } else
            Log.d("mLog", "0 rows");

        cursor.close();
        dbHelper.close();
    }

    public void goToCreateNoteActivityNote(View view) {
        Intent intent = new Intent(this, CreateNote.class);
        startActivity(intent);
    }

    public void goToTagActivityNote(View view) {
        Intent intent = new Intent(this, Tag.class);
        startActivity(intent);
    }

}