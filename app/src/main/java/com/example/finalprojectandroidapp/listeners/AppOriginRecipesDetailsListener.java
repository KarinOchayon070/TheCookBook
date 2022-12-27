package com.example.finalprojectandroidapp.listeners;

import com.example.finalprojectandroidapp.models.RecipesDetailsResponse;

public interface AppOriginRecipesDetailsListener {
    void didFetch(RecipesDetailsResponse response, String msg);
    void didError(String msg);
}
