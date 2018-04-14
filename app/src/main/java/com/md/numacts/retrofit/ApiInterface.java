package com.md.numacts.retrofit;

import com.md.numacts.data.FactResponse;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Danish Ansari on 24-09-2017.
 */

public interface ApiInterface
{
    @GET("/random/{type}?json")
    Call<FactResponse> getRandomFact(@Path("type") String type);

    @GET("{number}/trivia?json")
    Call<FactResponse> getTriviaFact(@Path("number") int number);

    @GET("{number}/math?json")
    Call<FactResponse> getMathFact(@Path("number") int number);

    @GET("{year}/year?json")
    Call<FactResponse> getYearFact(@Path("year") int year);

    @GET("{mm}/{dd}/date?json")
    Call<FactResponse> getDateFact(@Path("dd") int dd, @Path("mm") int mm);

    @GET("random?json")
    Call<FactResponse> getFactInRange(@Query("min") int min, @Query("max") int max);

    @GET("{firstNumber}..{secondNumber}?json")
    Call<HashMap<String,FactResponse>> getSeriesFact(@Path("firstNumber") int firstNumber, @Path("secondNumber") int secondNumber);

}
