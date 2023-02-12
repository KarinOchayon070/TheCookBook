package com.example.finalprojectandroidapp;

import android.content.Context;
import com.example.finalprojectandroidapp.listeners.AppOriginRecipesDetailsListener;
import com.example.finalprojectandroidapp.listeners.AppOriginRecipesResponseListener;
import com.example.finalprojectandroidapp.models.RecipesApiResponse;
import com.example.finalprojectandroidapp.models.RecipesDetailsResponse;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

//This code uses Retrofit to make GET requests to an API and retrieve recipe information

public class RequestManagerApi{

    Context context;
    //Create retrofit object - to manage the api calls
    Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.spoonacular.com/").addConverterFactory(GsonConverterFactory.create()).build();

    //Constructor
    public RequestManagerApi(Context context){
        this.context = context;
    }

    //Method to access the "CallRecipesFromApi" interface
    public void getRecipes(AppOriginRecipesResponseListener recipeResponseListener, List<String>tags){
        //Create an instance of the CallRecipesFromApi
        CallRecipesFromApi callRecipesFromApi = retrofit.create(CallRecipesFromApi.class);
        //Create a call object for the RecipesApiResponse
        //I passed the key from the api (I saved it in "strings.xml" under the name "api_key" and the number 100
        //That represent that I want my app to show 100 recipes from the api I used
        Call<RecipesApiResponse> call = callRecipesFromApi.callRandomRecipe(context.getString(R.string.api_key), "100", tags);
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

    //Method to access the "CallRecipesDetailsFromApi" interface
    public void getRecipesDetails(AppOriginRecipesDetailsListener appOriginRecipesDetailsListener, int id){
        //Create an instance of the CallRecipesDetailsFromApi
        CallRecipesDetailsFromApi callRecipesDetailsFromApi = retrofit.create(CallRecipesDetailsFromApi.class);
        //Create a call object for the CallRecipesDetailsFromApi
        //I passed the id from the api and the api key (I saved it in "strings.xml" under the name "api_key"
        Call<RecipesDetailsResponse> call = callRecipesDetailsFromApi.callRecipesDetailsResponse(id, context.getString(R.string.api_key));
        call.enqueue(new Callback<RecipesDetailsResponse>() {
            @Override
            public void onResponse(Call<RecipesDetailsResponse> call, Response<RecipesDetailsResponse> response) {
                if(!response.isSuccessful()){
                    appOriginRecipesDetailsListener.didError(response.message());
                    return;
                }
                appOriginRecipesDetailsListener.didFetch(response.body(),response.message());
            }
            @Override
            public void onFailure(Call<RecipesDetailsResponse> call, Throwable t) {
                appOriginRecipesDetailsListener.didError(t.getMessage());
            }
        });
    }

    //Create an interface for the api calls (for the recipe)
    //This is for the name+image of the random recipe in the "AppOriginRecipesFragment"
    private interface CallRecipesFromApi{
        //The parameters I passed is the parameter from  the api I used
        //This call method is a get method (according the api)
        @GET("recipes/random")
        //The "RecipesApiResponse" model is where all the array list recipe is stored
        Call<RecipesApiResponse> callRandomRecipe(@Query("apiKey") String apiKey, @Query("number") String number, @Query("tags") List<String> tags);
    }

    //Create an interface for the api calls (for the details of each recipe)
    //This is for the details "AppOriginRecipesDetailsFragment"
    private interface CallRecipesDetailsFromApi{
        @GET("recipes/{id}/information")
        //The "RecipesDetailsResponse" model is where all the details of the recipe is stored
        Call<RecipesDetailsResponse> callRecipesDetailsResponse(@Path("id") int id, @Query("apiKey") String apiKey);
    }
}