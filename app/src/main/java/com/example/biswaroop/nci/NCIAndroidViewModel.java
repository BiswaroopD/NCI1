package com.example.biswaroop.nci;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NCIAndroidViewModel extends AndroidViewModel {

    private final NCIRoomDatabase nciRoomDatabase;
    private LiveData<List<Article>> articleArrayListLiveData;
    private ArticleRepository articleRepository;

    public NCIAndroidViewModel(@NonNull Application application) {
        super(application);

//        String source = "";

//        articleRepository = new ArticleRepository(application);
//        articleArrayListLiveData = articleRepository.getArticlesss(source);

//        Context context = application.getApplicationContext();

        nciRoomDatabase = Room.databaseBuilder(
                application.getApplicationContext(),
                NCIRoomDatabase.class,
                "nc_iDatabase")
                .allowMainThreadQueries()
                .build();

    }

//    LiveData<List<Article>> getArticlesFromDb() {
//        if (articleArrayListLiveData == null) {
//
//            articleArrayListLiveData = nciRoomDatabase.getArticleDAO().getAllArticles();
//        }
//        return articleArrayListLiveData;
//    }

    private void ConnectOKHttp() {

        OkHttpClient okHttpClientFetchNews = new OkHttpClient();
        PreferenceUtils preferenceUtils;

        //TODO: Get this finally from shared preferences
        String strCountryCode = PreferenceUtils.PREFERENCE_KEY_DEFAULT_COUNTRY_VALUE;
        //

        String url_NewsApi = PreferenceUtils.PREFERENCE_KEY_NEWSAPI_URL_VALUE
                + strCountryCode
                + "&apikey="
                + PreferenceUtils.NOWRITE_NEWSAPI_KEY;

        Request requestNews = new Request.Builder()
                .url(url_NewsApi)
                .build();

        //pass the request to the okhttp client
        okHttpClientFetchNews.newCall(requestNews).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String strNewsResponse = response.body().string();
                Log.d("TAG", "onResponse: " + strNewsResponse);

                Gson gsonNews = new Gson();
                Result resultNews = gsonNews.fromJson(strNewsResponse, Result.class);
                final List<Article> articleArrayList = resultNews.getArticles();

            }
        });


    }


}
