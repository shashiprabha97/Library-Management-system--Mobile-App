package com.example.indika.myapplication.http_handlers;

import android.content.SharedPreferences;
import android.os.AsyncTask;

public class TokenStore {
    private static SharedPreferences sharedPreferences ;
    public static final String TOKEN = "token";
    public static final String SHARED_PREFERENCE_NAME="token_preference";


    public static void setToken(String token,SharedPreferences sharedPreferenc){
        sharedPreferences = sharedPreferenc;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TOKEN,token);
        editor.commit();

    }
    public static String getToken(SharedPreferences sharedPreferenc){
        String token = null;
        sharedPreferences = sharedPreferenc;
        if (sharedPreferences.contains(TOKEN)) {
            token = sharedPreferences.getString(TOKEN, "");
        }
        return token;
    }
    public static void removeToken(SharedPreferences sharedPreferenc){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(TOKEN);
        editor.commit();
        editor.clear();
    }


}
