package com.example.finalprojectandroidapp;

import com.google.firebase.database.Exclude;

public class UploadRecipe {

    String recipeImageUrl;
    String userId;
    String recipeName;
    String recipeSummary;

    String recipeInstructions;
    private String mKey;
    Boolean isFavorite;

    //empty constructor needed (to work with firebase) - DO NOT DELETE
    public UploadRecipe() {
    }

    public UploadRecipe(String userId, String recipeName, String recipeSummary,  String recipeInstructions, String recipeImageUrl) {
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
        this.isFavorite = false;


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

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }
}