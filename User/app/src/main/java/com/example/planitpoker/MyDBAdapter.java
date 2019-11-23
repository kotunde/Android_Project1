package com.example.planitpoker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;


//SOURCE: http://www.codebind.com/android-tutorials-and-examples/android-sqlite-tutorial-example/

public class MyDBAdapter extends SQLiteOpenHelper
{
    public static final String DATABASE_NAME = "PlanItPoker.db";

    public MyDBAdapter(Context context)
    {
        super(context,DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE  Titles (id INTEGER PRIMARY KEY, title VARCHAR)");
        db.execSQL("CREATE TABLE  Votes (name VARCHAR, title VARCHAR, vote VARCHAR)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS Titles");
        db.execSQL("DROP TABLE IF EXISTS Votes");
        onCreate(db);
    }

    public boolean insertTitle(String title)
    {
        SQLiteDatabase db =  this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title",title);
        db.insert("Titles",null,contentValues);
        return true;
    }

    public boolean insertVote(String name, String title, String vote)
    {
        SQLiteDatabase db =  this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",name);
        contentValues.put("title",title);
        contentValues.put("vote",vote);
        db.insert("Votes",null,contentValues);
        return true;
    }

    public Cursor getDataTitles()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM Titles",null);
        return res;
    }

    public Cursor getDataVotes()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM Votes",null);
        return res;
    }

}
