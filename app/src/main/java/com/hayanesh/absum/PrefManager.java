package com.hayanesh.absum;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Hayanesh on 16-Apr-17.
 */

public class PrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "aspect-store";
    private static final String ASPECT_ID = "AspectId";
    private static final String ASPECT_TOTAL = "AspectTotal";
    private static final String ASPECT_POS = "AspectPos";
    private static final String ASPECT_NEG = "AspectNeg";
    private static final String ASPECT_SCORE = "AspectScore";
    public PrefManager(Context context)
    {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor = pref.edit();
    }
    public void setAspectScore(int score)
    {
        editor.putInt(ASPECT_SCORE,score);
        editor.commit();
    }
    public void setAspectId(String id)
    {
        editor.putString(ASPECT_ID,id);
        editor.commit();
    }
    public void setAspectTotal(String total)
    {
        editor.putString(ASPECT_TOTAL,total);
        editor.commit();
    }
    public void setAspectPos(String pos)
    {
        editor.putString(ASPECT_POS,pos);
        editor.commit();
    }
    public void setAspectNeg(String neg)
    {
        editor.putString(ASPECT_NEG,neg);
        editor.commit();
    }
    public String getAspectId()
    {
        return pref.getString(ASPECT_ID,"101");
    }
    public String getAspectTotal()
    {
        return pref.getString(ASPECT_TOTAL,"6000");

    }
    public String getAspectPos()
    {
        return pref.getString(ASPECT_POS,"4000");
    }
    public String getAspectNeg()
    {
        return pref.getString(ASPECT_NEG,"3000");
    }

    public int getAspectScore()
    {
        return pref.getInt(ASPECT_SCORE,0);
    }
}
