package com.example.hyponic.model;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    public static final String SP_USER = "spUser";
    public static final int SP_ID = 0;
    public static final String SP_NAMA = "spNama";
    public static final String SP_TOKEN = "spToken";


    SharedPreferences sp;
    SharedPreferences.Editor spEditor;

    public SharedPrefManager(Context context){
        sp = context.getSharedPreferences(SP_USER, Context.MODE_PRIVATE);
        spEditor = sp.edit();
    }

    public void saveSPString(String keySP, String value){
        spEditor.putString(keySP, value);
        spEditor.commit();
    }

    public void saveSPInt(String keySP, int value){
        spEditor.putInt(keySP, value);
        spEditor.commit();
    }

    public void saveSPBoolean(String keySP, boolean value){
        spEditor.putBoolean(keySP, value);
        spEditor.commit();
    }

    public String getSPNama(){
        return sp.getString(SP_NAMA, "");
    }
    public String getSPToken(){
        return sp.getString(SP_TOKEN, "");
    }
    public int getSPID(){
        return sp.getInt(String.valueOf(SP_ID), 0);
    }

}
