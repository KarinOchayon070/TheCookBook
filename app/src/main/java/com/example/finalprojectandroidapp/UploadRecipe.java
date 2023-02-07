package com.example.finalprojectandroidapp;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

//This is the "UploadRecipes" class. When a user uploads his own recipe - an instance of this class is created and uploaded to FireBase

public class UploadRecipe {

    //Initial
    String recipeImageUrl;
    String userId;
    String recipeName;
    String recipeSummary;
    String recipeInstructions;
    private String mKey;
    Map<String, Boolean> favorite;

    //empty constructor needed (to work with firebase) - DO NOT DELETE
    public UploadRecipe() {
    }

    public UploadRecipe(String userId, String recipeName, String recipeSummary,  String recipeInstructions, String recipeImageUrl, Map<String, Boolean> favorite) {
        //If the user didn't give the image a name - save it in the database as "No Name"
        //Trim is for remove the empty spaces
        if (recipeName.trim().equals("")) {
            recipeName = "No Name";
        }
        this.recipeSummary = recipeSummary;
        this.recipeInstructions = recipeInstructions;
        this.userId = userId;
        this.recipeName = recipeName;
        this.recipeImageUrl = recipeImageUrl;
        this.favorite = new HashMap<>();
    }

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRecipeName() {
        return recipeName;
    }
    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getRecipeSummary() {
        return recipeSummary;
    }
    public void setRecipeSummary(String recipeSummary) {
        this.recipeSummary = recipeSummary;
    }

    public String getRecipeInstructions() {
        return recipeInstructions;
    }
    public void setRecipeInstructions(String recipeInstructions) {
        this.recipeInstructions = recipeInstructions;
    }

    public String getRecipeImageUrl() {
        return recipeImageUrl;
    }
    public void setRecipeImageUrl(String recipeImageUrl) {
        this.recipeImageUrl = recipeImageUrl;
    }

    @Exclude
    public String getKey() {
        return mKey;
    }
    @Exclude
    public void setKey(String key) {
        mKey = key;
    }

    public Map<String, Boolean> getFavorite() {
        return favorite;
    }
    public void setFavorite(Map<String, Boolean> favorite) {
        this.favorite = favorite;
    }
}