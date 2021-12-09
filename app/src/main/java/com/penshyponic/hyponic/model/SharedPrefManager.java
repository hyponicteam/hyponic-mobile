package com.penshyponic.hyponic.model;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    public static final String SP_USER = "spUser";
    public static final int SP_ID = 0;
    public static final String SP_NAMA = "spNama";
    public static final String SP_EMAIL = "spEmail";
    public static final String SP_TOKEN = "spToken";
    public static final String SP_PLANT_ID="";
    public static final String SP_GROWTH_ID="";
    public static final String SP_IS_LOGIN = "isLogin";
    public static final String SP_Created_At= "spCreatedAt";
    public static final String SP_Edited_At="spEditedAt";

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
        return sp.getString(SP_NAMA, "Username");
    }
    public String getSPEmail(){
        return sp.getString(SP_EMAIL, "email@gmail.com");
    }
    public String getSPToken(){
        return sp.getString(SP_TOKEN, "");
    }
    public int getSPID(){
        return sp.getInt(String.valueOf(SP_ID), 0);
    }
    public String getSPPlantId(){
        return sp.getString(SP_PLANT_ID, "0");
    }
    public String getSPGrowthsId(){
        return sp.getString(SP_GROWTH_ID, "0");
    }
    public Boolean getSPIsLogin(){
        return sp.getBoolean(SP_IS_LOGIN, false);
    }
    public String getSP_Created_At() {
        return sp.getString(SP_Created_At,"null");
    }
    public String getSP_Edited_At() {
        return sp.getString(SP_Edited_At,"null");
    }
}
