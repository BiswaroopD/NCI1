package com.example.biswaroop.nci;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ArticleService2 {

    @GET("top-headlines?country=in&apikey=f65ce17dd71542dea38fd024b6c9c974")
    Call <Result> getAllArticles();

}
