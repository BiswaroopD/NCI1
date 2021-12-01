package com.theappfactory.ChattrBox.SimpleNewsChat;

public interface PreferenceUtils{

    String SHARED_PREFERENCE_FILE_NAME = "NCI_Shared_Preferences";

    //sundries
    String PREFERENCE_KEY_DEFAULT_COUNTRY = "default_country_code";
    String PREFERENCE_KEY_DEFAULT_COUNTRY_VALUE = "US";
    String PREFERENCE_KEY_SELECTED_COUNTRY = "selected_country_code";
    String PREFERENCE_KEY_SELECTED_COUNTRY_VALUE = "";
    String PREFERENCE_KEY_NEWSAPI_URL = "url_newsapi";
    String PREFERENCE_KEY_NEWSAPI_URL_VALUE = "https://newsapi.org/v2/top-headlines?country=";
    String PREFERENCE_KEY_FOCUSSED_ARTICLE = "focussed_article";
    String PREFERENCE_KEY_FOCUSSED_ARTICLE_ID = "";


    String PREFERENCE_KEY_COMMENTID = "comment_id";
    int PREFERENCE_KEY_COMMENTID_VALUE_DEFAULT = 0;

    //Firebase database
    String FIREBASE_TABLE_COMMENTS = "comments";

    //NCI intent key value pair
    String KEY_SELECTED_ARTICLE_ID = "SELECTED_ARTICLE_ID";
    String KEY_SELECTED_ARTICLE_IMAGE_URL = "SELECTED_ARTICLE_IMAGE_URL";
    String KEY_SELECTED_ARTICLE_TITLE = "SELECTED_ARTICLE_IMAGE_TITLE";
    String KEY_SELECTED_ARTICLE_URL = "SELECTED_ARTICLE_URL";

    //REM: Do not write these to the pref file. Storing these here -in lieu of a better place
    String NOWRITE_NEWSAPI_KEY = "f65ce17dd71542dea38fd024b6c9c974";

}
