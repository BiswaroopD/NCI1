package com.theappfactory.ChattrBox.SimpleNewsChat;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Ignore;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

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

    @Query("DELETE FROM article_table")
    void deleteAllArticles();

    @Update
    void updateArticle (Article article);

    @Query("UPDATE article_table SET commentCount = :commentCount WHERE id = :articleId" )
    void updateArticleCommentCount (String articleId, int commentCount);

    @Query("SELECT * FROM article_table")
    LiveData<List<Article>> getAllArticles();

    @Query("SELECT * FROM article_table ORDER BY publishedAt DESC")
    LiveData<List<Article>> getAllArticlesOrderByLatest();

    @Query("SELECT * FROM article_table ORDER BY publishedAt DESC, commentCount DESC")
    LiveData<List<Article>> getAllArticlesOrderByLatestCommented();

    @Query("SELECT * FROM article_table WHERE countryCode LIKE :countryCode ORDER BY publishedAt DESC, commentCount DESC")
    LiveData<List<Article>> getCountryArticlesOrderByCommentedLatest(String countryCode);

    @Query("SELECT * FROM article_table WHERE countryCode LIKE :countryCode ORDER BY commentCount DESC")
    LiveData<List<Article>> getCountryArticlesOrderByCommented(String countryCode);

    @Query("SELECT * FROM article_table WHERE description LIKE :searchText")
    LiveData<List<Article>> searchArticles(String searchText);

//    @Query("SELECT * FROM article_table WHERE description LIKE :searchText")
//    LiveData<List<Article>> searchArticles(String searchText);

}
