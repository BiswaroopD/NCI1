package com.example.biswaroop.nci;

import android.util.Log;

import java.util.ArrayList;

public class Result {

    /* Get the class name*/
    final String TAG = "TAG: "+ this.getClass().getSimpleName().toString();
    /* Get the class name*/

    String status;
    Integer totalResults;
    ArrayList<Article> articles;

    public String getStatus() {

        /* Get the method name*/
        class Local{};
        String strMethod = Local.class.getEnclosingMethod().getName();
        Log.e(TAG,strMethod);
        /* Get the method name*/

        return status;
    }

    public Integer getTotalResults() {

        /* Get the method name*/
        class Local{};
        String strMethod = Local.class.getEnclosingMethod().getName();
        Log.e(TAG,strMethod);
        /* Get the method name*/

        return totalResults;
    }

    public ArrayList<Article> getArticles() {

        /* Get the method name*/
        class Local{};
        String strMethod = Local.class.getEnclosingMethod().getName();
        Log.e(TAG,strMethod);
        /* Get the method name*/

        return articles;
    }

    public Result(String status, Integer totalResults, ArrayList<Article> articles) {

        /* Get the method name*/
        class Local{};
        String strMethod = Local.class.getEnclosingMethod().getName();
        Log.e(TAG,strMethod);
        /* Get the method name*/

        this.status = status;
        this.totalResults = totalResults;
        this.articles = articles;
    }
}
