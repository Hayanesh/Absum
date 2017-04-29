package com.hayanesh.absum;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.widget.ActionBarContextView;
import android.telecom.Call;

import java.util.ArrayList;

/**
 * Created by Hayanesh on 16-Apr-17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    final static int DATABASE_VERSION = 1;
    final static String DATABASE_NAME = "AspectList";

    //Table
    final static String ASPECT_TABLE = "Aspects";

    //Column
    final static String KEY_ID = "id";
    final static String KEY_NAME = "name";
    final static String KEY_SUMMARY = "summary";
    final static String KEY_SCORE = "score";
    final static String KEY_POS = "pos";
    final static String KEY_NEG = "neg";


    final static String CREATE_TABLE_ASPECATS = "CREATE TABLE "+ ASPECT_TABLE +"(" + KEY_ID + " TEXT, "
    + KEY_NAME +" TEXT, "+ KEY_SCORE + " TEXT, "+ KEY_SUMMARY + " TEXT, " + KEY_POS + " TEXT, " + KEY_NEG + " TEXT "+")";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ASPECATS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ ASPECT_TABLE);
    }

    public DatabaseHelper(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    public long createDetails(Aspects aspects)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID,aspects.getId());
        values.put(KEY_NAME, aspects.getName());
        values.put(KEY_SCORE,aspects.getScore());
        values.put(KEY_SUMMARY,aspects.getSummary());
        values.put(KEY_POS,aspects.getPos());
        values.put(KEY_NEG,aspects.getNeg());
        long aspects_id = db.insert(ASPECT_TABLE,null,values);
        db.close();
        return aspects_id;
    }

    public Aspects getAspects(String id)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(ASPECT_TABLE,new String[]{KEY_ID,KEY_NAME,KEY_SCORE,KEY_SUMMARY,KEY_POS,KEY_NEG},KEY_ID + "=?",
                new String[]{id},null,null,null,null);
        if(cursor!=null)
            cursor.moveToFirst();

        Aspects aspects = new Aspects(cursor.getString(0),cursor.getString(1),
                cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5));
        return aspects;
    }

    public ArrayList<Aspects> getAspectList(String id)
    {
        ArrayList<Aspects> aspects = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String SELECT_ASPECTS = "SELECT * FROM "+ASPECT_TABLE;
        Cursor c = db.rawQuery(SELECT_ASPECTS,null);
        if(c.moveToFirst())
        {
            do {
                Aspects app = new Aspects();
                app.setId(c.getString(c.getColumnIndex(KEY_ID)));
                app.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                app.setScore(c.getString(c.getColumnIndex(KEY_SCORE)));
                app.setNeg(c.getString(c.getColumnIndex(KEY_NEG)));
                app.setPos(c.getString(c.getColumnIndex(KEY_POS)));
                app.setSummary(c.getString(c.getColumnIndex(KEY_SUMMARY)));
                aspects.add(app);
            }while (c.moveToNext());
        }
        return aspects;
    }
    public void DeleteAspects()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ASPECT_TABLE,null,null);
    }
}