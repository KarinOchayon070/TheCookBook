package com.example.finalprojectandroidapp;

public class UploadRecipeImage {

    String recipeImageUrl;
    String userId;
    String recipeName;
    String recipeSummary;
    String recipeIngredient;
    String recipeInstructions;

    //empty constructor needed (to work with firebase) - DO NOT DELETE
    public UploadRecipeImage() {
    }

    public UploadRecipeImage(String userId, String recipeName, String recipeSummary, String recipeIngredient, String recipeInstructions, String recipeImageUrl) {
        //If the user didn't give the image a name - save it in the database as "No Name"
        //Trim is for remove the empty spaces
        if (recipeName.trim().equals("")) {
            recipeName = "No Name";
        }
        this.recipeSummary = recipeSummary;
        this.recipeIngredient = recipeIngredient;
        this.recipeInstructions = recipeInstructions;
        this.userId = userId;
        this.recipeName = recipeName;
        this.recipeImageUrl = recipeImageUrl;


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

    public String getRecipeIngredient() {
        return recipeIngredient;
    }

    public void setRecipeIngredient(String recipeIngredient) {
        this.recipeIngredient = recipeIngredient;
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
}