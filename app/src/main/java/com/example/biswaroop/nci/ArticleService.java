package com.example.biswaroop.nci;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ArticleService {

    String URL = PreferenceUtils.PREFERENCE_KEY_NEWSAPI_URL_VALUE;

    @GET("top-headlines")
    Call<Article> getArticles(@Query("sources") String source);
}
