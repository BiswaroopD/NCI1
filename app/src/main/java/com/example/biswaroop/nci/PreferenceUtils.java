package com.example.biswaroop.nci;

import android.content.Context;
import android.util.Log;

public class PreferenceUtils {

    /* Get the class name*/
    private String TAG = "TAG: "+ this.getClass().getSimpleName();
    /* Get the class name*/

    //TODO: write to preference utils file
    public static final String PREFERENCE_KEY_DEFAULT_COUNTRY = "default_country_code";
    public static final String PREFERENCE_KEY_DEFAULT_COUNTRY_VALUE = "us";
    public static final String PREFERENCE_KEY_NEWSAPI_URL = "url_newsapi";
    public static final String PREFERENCE_KEY_NEWSAPI_URL_VALUE = "https://newsapi.org/v2/top-headlines?country=";


    //REM: Do not write these to the pref file. Storing these here -in lieu of a better place
    public static final String NOWRITE_NEWSAPI_KEY = "f65ce17dd71542dea38fd024b6c9c974";


    public void init(Context applicationContext) {

        /* Get the method name*/
        class Local{};
        String strMethod = Local.class.getEnclosingMethod().getName();
        Log.e(TAG,strMethod);
        /* Get the method name*/

    }

}
