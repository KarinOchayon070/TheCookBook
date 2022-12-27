package com.example.finalprojectandroidapp.listeners;

import com.example.finalprojectandroidapp.models.RecipesApiResponse;

public interface AppOriginRecipesResponseListener {
    void didFetch(RecipesApiResponse recipesApiResponse, String msg);
    //Error handling - the msg is the message I got from the api
    void didError(String msg);

}
