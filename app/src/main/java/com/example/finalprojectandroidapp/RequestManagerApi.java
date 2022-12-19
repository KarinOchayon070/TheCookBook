package com.example.finalprojectandroidapp;

import android.content.Context;

import com.example.finalprojectandroidapp.listeners.RecipeResponseListener;
import com.example.finalprojectandroidapp.models.RecipesApiResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;


public class RequestManagerApi{

    Context context;
    //Create retrofit object - to manage the api calls
    Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.spoonacular.com/").addConverterFactory(GsonConverterFactory.create()).build();

    public RequestManagerApi(Context context){
        this.context = context;
    }

    public void getRecipes(RecipeResponseListener recipeResponseListener){
        //Create an instance of the CallRecipesFromApi
        CallRecipesFromApi callRecipesFromApi = retrofit.create(CallRecipesFromApi.class);
        //Create a call object for the RecipesApiResponse
        //I passed the key from the api (I saved it in "strings.xml" under the name "api_key" and the number 100
        //That represent that I want my app to show 100 recipes from the api I used
        Call<RecipesApiResponse> call = callRecipesFromApi.callRandomRecipe(context.getString(R.string.api_key), "100");
        call.enqueue(new Callback<RecipesApiResponse>() {
            @Override
            public void onResponse(Call<RecipesApiResponse> call, Response<RecipesApiResponse> response) {
                if(!response.isSuccessful()){
                    recipeResponseListener.didError(response.message());
                    return;
                }
                recipeResponseListener.didFetch(response.body(), response.message());
            }
            @Override
            public void onFailure(Call<RecipesApiResponse> call, Throwable t) {
                recipeResponseListener.didError(t.getMessage());
            }
        });
    }

    //Create an interface for the api calls
    private interface CallRecipesFromApi{
        //The parameters I passed is the parameter from  the api I used
        //This call method is a get method (according the api)
        @GET("recipes/random")
        Call<RecipesApiResponse> callRandomRecipe(@Query("apiKey") String apiKey, @Query("number") String number);
    }
}