package com.example.biswaroop.nci;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ArticleRepository {
//    /* Get the class name*/
//    final String TAG = "TAG: "+ this.getClass().getSimpleName();
//    /* Get the class name*/

    private ArticleService articleService;
//    private static ArticleRepository projectArticleRepository;
    LiveData<List<Article>> articleList;
    ArticleDAO articleDAO;

    public ArticleRepository(Application application) {

//        /* Get the method name*/
//        class Local{};
//        String strMethod = Local.class.getEnclosingMethod().getName();
//        Log.e(TAG,strMethod);
//        /* Get the method name*/

        OkHttpClient.Builder httpClient =
                new OkHttpClient.Builder();

        httpClient.addInterceptor(new Interceptor() {

            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                HttpUrl originalHttpUrl = original.url();

                HttpUrl url = originalHttpUrl.newBuilder()
                        .addQueryParameter("apikey", PreferenceUtils.NOWRITE_NEWSAPI_KEY)
                        .build();

                Request request = original.newBuilder()
                        .url(url)
                        .build();

                return chain.proceed(request);
                }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ArticleService.URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        articleService = retrofit.create(ArticleService.class);

        NCIRoomDatabase nciRoomDatabase = NCIRoomDatabase.getDatabase(application);
        ArticleDAO articleDAO = nciRoomDatabase.getArticleDAO();
        articleList = articleDAO.getAllArticles();
    }

    public synchronized static  ArticleRepository getInstance() {

////        /* Get the method name*/
////        class Local{};
////        String strMethod = Local.class.getEnclosingMethod().getName();
////        Log.e("TAG",strMethod);
////        /* Get the method name*/
//
//        if (projectArticleRepository == null) {
//            projectArticleRepository = new ArticleRepository();
//        }
//        return projectArticleRepository;

        return null;
    }

    public LiveData<List<Article>> getArticlesss(String source) {

//        /* Get the method name*/
//        class Local{};
//        String strMethod = Local.class.getEnclosingMethod().getName();
//        Log.e(TAG,strMethod);
//        /* Get the method name*/

        final MutableLiveData<List<Article>> data = new MutableLiveData<>();
        articleService.getArticles(source).enqueue(new Callback<Article>() {
            @Override
            public void onResponse(Call<Article> call, retrofit2.Response<Article> response) {
//                for (int i=0, i<data.getSize(),i++){
//                    data.setValue(response.body());
//                }
            }

            @Override
            public void onFailure(Call<Article> call, Throwable t) {

            }
        });
        return data;
    }

    public void save(List<Article> articleList) {
        articleDAO.insertArticles(articleList);
    }

}
