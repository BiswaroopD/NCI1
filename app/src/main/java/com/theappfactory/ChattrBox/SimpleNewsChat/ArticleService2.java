package com.theappfactory.ChattrBox.SimpleNewsChat;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ArticleService2 {

    //https://newsapi.org/sources

    @GET("top-headlines?country=AR&apikey=f65ce17dd71542dea38fd024b6c9c974")
    Call <Result> getAllArArticlesFromApi();

    @GET("top-headlines?country=AU&apikey=f65ce17dd71542dea38fd024b6c9c974")
    Call <Result> getAllAuArticlesFromApi();

    @GET("top-headlines?country=AT&apikey=f65ce17dd71542dea38fd024b6c9c974")
    Call <Result> getAllAtArticlesFromApi();

    @GET("top-headlines?country=BE&apikey=f65ce17dd71542dea38fd024b6c9c974")
    Call <Result> getAllBeArticlesFromApi();

    @GET("top-headlines?country=BR&apikey=f65ce17dd71542dea38fd024b6c9c974")
    Call <Result> getAllBrArticlesFromApi();

    @GET("top-headlines?country=BG&apikey=f65ce17dd71542dea38fd024b6c9c974")
    Call <Result> getAllBgArticlesFromApi();

    @GET("top-headlines?country=CA&apikey=f65ce17dd71542dea38fd024b6c9c974")
    Call <Result> getAllCaArticlesFromApi();

    @GET("top-headlines?country=CO&apikey=f65ce17dd71542dea38fd024b6c9c974")
    Call <Result> getAllCoArticlesFromApi();

    @GET("top-headlines?country=CN&apikey=f65ce17dd71542dea38fd024b6c9c974")
    Call <Result> getAllCnArticlesFromApi();

    @GET("top-headlines?country=CU&apikey=f65ce17dd71542dea38fd024b6c9c974")
    Call <Result> getAllCuArticlesFromApi();

    @GET("top-headlines?country=CZ&apikey=f65ce17dd71542dea38fd024b6c9c974")
    Call <Result> getAllCzArticlesFromApi();

    @GET("top-headlines?country=EG&apikey=f65ce17dd71542dea38fd024b6c9c974")
    Call <Result> getAllEgArticlesFromApi();

    @GET("top-headlines?country=FR&apikey=f65ce17dd71542dea38fd024b6c9c974")
    Call <Result> getAllFrArticlesFromApi();

    @GET("top-headlines?country=DE&apikey=f65ce17dd71542dea38fd024b6c9c974")
    Call <Result> getAllDeArticlesFromApi();

    @GET("top-headlines?country=GR&apikey=f65ce17dd71542dea38fd024b6c9c974")
    Call <Result> getAllGrArticlesFromApi();

    @GET("top-headlines?country=HK&apikey=f65ce17dd71542dea38fd024b6c9c974")
    Call <Result> getAllHkArticlesFromApi();

    @GET("top-headlines?country=HU&apikey=f65ce17dd71542dea38fd024b6c9c974")
    Call <Result> getAllHuArticlesFromApi();

    @GET("top-headlines?country=IN&apikey=f65ce17dd71542dea38fd024b6c9c974")
    Call <Result> getAllInArticlesFromApi();

    @GET("top-headlines?country=ID&apikey=f65ce17dd71542dea38fd024b6c9c974")
    Call <Result> getAllIdArticlesFromApi();

    @GET("top-headlines?country=IE&apikey=f65ce17dd71542dea38fd024b6c9c974")
    Call <Result> getAllIeArticlesFromApi();

    @GET("top-headlines?country=IL&apikey=f65ce17dd71542dea38fd024b6c9c974")
    Call <Result> getAllIlArticlesFromApi();

    @GET("top-headlines?country=IT&apikey=f65ce17dd71542dea38fd024b6c9c974")
    Call <Result> getAllItArticlesFromApi();

    @GET("top-headlines?country=LV&apikey=f65ce17dd71542dea38fd024b6c9c974")
    Call <Result> getAllLvArticlesFromApi();

    @GET("top-headlines?country=LT&apikey=f65ce17dd71542dea38fd024b6c9c974")
    Call <Result> getAllLtArticlesFromApi();

    @GET("top-headlines?country=MY&apikey=f65ce17dd71542dea38fd024b6c9c974")
    Call <Result> getAllMyArticlesFromApi();

    @GET("top-headlines?country=MX&apikey=f65ce17dd71542dea38fd024b6c9c974")
    Call <Result> getAllMxArticlesFromApi();

    @GET("top-headlines?country=MA&apikey=f65ce17dd71542dea38fd024b6c9c974")
    Call <Result> getAllMaArticlesFromApi();

    @GET("top-headlines?country=NL&apikey=f65ce17dd71542dea38fd024b6c9c974")
    Call <Result> getAllNlArticlesFromApi();

    @GET("top-headlines?country=NZ&apikey=f65ce17dd71542dea38fd024b6c9c974")
    Call <Result> getAllNzArticlesFromApi();

    @GET("top-headlines?country=NG&apikey=f65ce17dd71542dea38fd024b6c9c974")
    Call <Result> getAllNgArticlesFromApi();

    @GET("top-headlines?country=NO&apikey=f65ce17dd71542dea38fd024b6c9c974")
    Call <Result> getAllNoArticlesFromApi();

    @GET("top-headlines?country=PH&apikey=f65ce17dd71542dea38fd024b6c9c974")
    Call <Result> getAllPhArticlesFromApi();

    @GET("top-headlines?country=PL&apikey=f65ce17dd71542dea38fd024b6c9c974")
    Call <Result> getAllPlArticlesFromApi();

    @GET("top-headlines?country=PT&apikey=f65ce17dd71542dea38fd024b6c9c974")
    Call <Result> getAllPtArticlesFromApi();

    @GET("top-headlines?country=RO&apikey=f65ce17dd71542dea38fd024b6c9c974")
    Call <Result> getAllRoArticlesFromApi();

    @GET("top-headlines?country=RU&apikey=f65ce17dd71542dea38fd024b6c9c974")
    Call <Result> getAllRuArticlesFromApi();

    @GET("top-headlines?country=SA&apikey=f65ce17dd71542dea38fd024b6c9c974")
    Call <Result> getAllSaArticlesFromApi();

    @GET("top-headlines?country=RS&apikey=f65ce17dd71542dea38fd024b6c9c974")
    Call <Result> getAllRsArticlesFromApi();

    @GET("top-headlines?country=SG&apikey=f65ce17dd71542dea38fd024b6c9c974")
    Call <Result> getAllSgArticlesFromApi();

    @GET("top-headlines?country=SK&apikey=f65ce17dd71542dea38fd024b6c9c974")
    Call <Result> getAllSkArticlesFromApi();

    @GET("top-headlines?country=SI&apikey=f65ce17dd71542dea38fd024b6c9c974")
    Call <Result> getAllSiArticlesFromApi();

    @GET("top-headlines?country=ZA&apikey=f65ce17dd71542dea38fd024b6c9c974")
    Call <Result> getAllZaArticlesFromApi();

    @GET("top-headlines?country=KR&apikey=f65ce17dd71542dea38fd024b6c9c974")
    Call <Result> getAllKrArticlesFromApi();

    @GET("top-headlines?country=SE&apikey=f65ce17dd71542dea38fd024b6c9c974")
    Call <Result> getAllSeArticlesFromApi();

    @GET("top-headlines?country=CH&apikey=f65ce17dd71542dea38fd024b6c9c974")
    Call <Result> getAllChArticlesFromApi();

    @GET("top-headlines?country=TW&apikey=f65ce17dd71542dea38fd024b6c9c974")
    Call <Result> getAllTwArticlesFromApi();

    @GET("top-headlines?country=TH&apikey=f65ce17dd71542dea38fd024b6c9c974")
    Call <Result> getAllThArticlesFromApi();

    @GET("top-headlines?country=TR&apikey=f65ce17dd71542dea38fd024b6c9c974")
    Call <Result> getAllTrArticlesFromApi();

    @GET("top-headlines?country=AE&apikey=f65ce17dd71542dea38fd024b6c9c974")
    Call <Result> getAllAeArticlesFromApi();

    @GET("top-headlines?country=UA&apikey=f65ce17dd71542dea38fd024b6c9c974")
    Call <Result> getAllUaArticlesFromApi();

    @GET("top-headlines?country=GB&apikey=f65ce17dd71542dea38fd024b6c9c974")
    Call <Result> getAllGbArticlesFromApi();

    @GET("top-headlines?country=US&apikey=f65ce17dd71542dea38fd024b6c9c974")
    Call <Result> getAllUsArticlesFromApi();

    @GET("top-headlines?country=VE&apikey=f65ce17dd71542dea38fd024b6c9c974")
    Call <Result> getAllVeArticlesFromApi();

}
