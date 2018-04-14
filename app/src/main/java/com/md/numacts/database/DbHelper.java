package com.md.numacts.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Danish Ansari on 12-Oct-17.
 */

public class DbHelper extends SQLiteOpenHelper
{
    Context context;
    public static final String TABLE_NAME_RANDOM = "random";
    public static final String TABLE_NAME_QUEST = "quest";
    public static final String COLUMN_NAME_TYPE = "type";
    public static final String COLUMN_NAME_NUMBER = "number";
    public static final String COLUMN_NAME_FACT = "fact";


    public DbHelper(Context context)
    {
        super(context, "history.db", null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String create_random_table = "CREATE TABLE " + TABLE_NAME_RANDOM + "(id number PRIMARY KEY, type TEXT, number TEXT, fact TEXT);";
        String create_quest_table = "CREATE TABLE " + TABLE_NAME_QUEST + "(id number PRIMARY KEY, type TEXT, number TEXT, fact TEXT);";
        db.execSQL(create_random_table);
        db.execSQL(create_quest_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }
}
