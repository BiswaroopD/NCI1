package com.example.biswaroop.nci;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

//    /* Get the class name*/
//    final String TAG = "TAG: "+ this.getClass().getSimpleName().toString();
//    /* Get the class name*/

    List<Article> retrievedArticleList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NCIRoomDatabase nciRoomDatabase = NCIRoomDatabase.getDatabase(getApplicationContext());
        final ArticleDAO articleDAO = nciRoomDatabase.getArticleDAO();
        final ArticleAdapter articleAdapter = new ArticleAdapter(MainActivity.this,
                retrievedArticleList);

        /*Create handle for the RetrofitInstance interface*/
        ArticleService2 service = ArticleRepository2.getRetrofitInstance().create(
                ArticleService2.class);

        Call<Result> call = service.getAllArticles();

        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                retrievedArticleList.clear();
                retrievedArticleList.addAll(response.body().getArticles());
                articleAdapter.notifyDataSetChanged();
                Log.e("TAG", "onResponse: " + retrievedArticleList.size() );

                for (Article article : retrievedArticleList){
                    long id = articleDAO.insertArticle(article);
                    Log.e("TAG", "onResponse: " + id);
                }
//                articleDAO.insertArticles(retrievedArticleList);
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Log.e("TAG", "onFailure" + t.getMessage() );
                Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();

            }
        });


        RecyclerView rvNewsList = findViewById(R.id.rvNewsList);
        RecyclerView.LayoutManager newsLayoutManager = new LinearLayoutManager(MainActivity.this);
        rvNewsList.setLayoutManager(newsLayoutManager);
        rvNewsList.setAdapter(articleAdapter);


        NCIAndroidViewModel nciAndroidViewModel = ViewModelProviders.of(this)
                .get(NCIAndroidViewModel.class);

        articleDAO.getAllArticles().observe(this, new Observer<List<Article>>() {
            @Override
            public void onChanged(@Nullable List<Article> articleList) {
                Log.e("TAG", "onChanged: " + articleList.size());
                retrievedArticleList.clear();
                retrievedArticleList.addAll(articleList);
                articleAdapter.notifyDataSetChanged();
            }
        });
//        nciAndroidViewModel.getArticlesFromDb().observe(this, new Observer<List<Article>>() {
//            @Override
//            public void onChanged(@Nullable List<Article> articles) {
//                articleAdapter.setArticleList(articles);
//            }
//        });
    }

}
