package com.example.lab4;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class Tag extends AppCompatActivity {

    DBHelper dbHelper;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);
        linearLayout = (LinearLayout) findViewById(R.id.lineTag);
        dbHelper = new DBHelper(this);

        SQLiteDatabase database = dbHelper.getWritableDatabase();

        Cursor cursor = database.query(DBHelper.TABLE_TAGS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID_TAG);
            int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME_TAG);
            do {

                Integer id = cursor.getInt(idIndex);
                Button button = new Button(this);
                button.setText(cursor.getString(nameIndex));
                button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Tag.this, UpdateTag.class);
                        intent.putExtra("id", id);
                        startActivity(intent);
                    }
                });
                linearLayout.addView(button);
            } while (cursor.moveToNext());
        } else
            Log.d("mLog", "0 rows");

        cursor.close();
        dbHelper.close();

    }

    public void goToCreateTagActivityTag(View view) {
        Intent intent = new Intent(this, CreateTag.class);
        startActivity(intent);
    }

    public void goToNoteActivityTag(View view) {
        Intent intent = new Intent(this, Note.class);
        startActivity(intent);
    }
}