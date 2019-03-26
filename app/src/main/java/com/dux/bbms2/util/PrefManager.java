package com.dux.bbms2.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PrefManager {

    private String accountType = "accountType";
    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public PrefManager(Context context) {
        this.context = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
    }


    public String getAccountType() {
        return sharedPreferences.getString(accountType,"");
    }

    public void setAccountType(String accountType) {
        editor.putString(this.accountType,accountType);
        editor.commit();
    }
}
