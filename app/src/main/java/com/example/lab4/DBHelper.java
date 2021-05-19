package com.example.lab4;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "mydb";
    public static final String TABLE_TAGS = "tags";
    public static final String TABLE_NOTES = "notes";
    public static final String TABLE_TAGS_NOTES = "tagsnotes";

    public static final String KEY_ID_TAG = "_idtag";
    public static final String KEY_NAME_TAG = "nametag";

    public static final String KEY_ID_NOTE = "_idnote";
    public static final String KEY_NAME_NOTE = "namenote";
    public static final String KEY_DATE_NOTE = "datenote";
    public static final String KEY_DESCRIPTION_NOTE = "descriptionote";

    public static final String KEY_NOTE_ID = "noteid";
    public static final String KEY_TAG_ID = "tagid";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_TAGS + "(" + KEY_ID_TAG
                + " integer primary key," + KEY_NAME_TAG + " text)");

        db.execSQL("create table " + TABLE_NOTES + "(" + KEY_ID_NOTE
                + " integer primary key," + KEY_NAME_TAG + " text, " +
                KEY_NAME_NOTE + " text," + KEY_DATE_NOTE + " text, " +
                KEY_DESCRIPTION_NOTE + " text)");

        db.execSQL("create table " + TABLE_TAGS_NOTES + "(" + KEY_NOTE_ID
                + " integer," + KEY_TAG_ID + " integer)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
