package com.example.biswaroop.nci;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface ArticleDAO {

    @Insert
    long insertArticle (Article article);

    @Insert
    void insertMultipleArticles (Article... articles);

    @Insert
    void insertArticles (List<Article> articleList);

    @Delete
    void deleteArticle (Article article);

    @Delete
    void deleteMultipleArticles(Article... articles);

    @Update
    void updateArticle (Article article);

    @Query("SELECT * FROM article_table")
    LiveData<List<Article>> getAllArticles();

//    @Query("DELETE FROM article_table")
//    long deleteAllArticles();

}
