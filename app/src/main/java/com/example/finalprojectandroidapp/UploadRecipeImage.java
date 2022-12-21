package com.example.finalprojectandroidapp;

public class UploadRecipeImage {

    private String mName;
    private String mImageUrl;
    private String mId;

    //empty constructor needed (to work with firebase) - DO NOT DELETE
    public UploadRecipeImage() {
    }

    public UploadRecipeImage(String id, String name, String imageUrl) {
        //If the user didn't give the image a name - save it in the database as "No Name"
        //Trim is for remove the empty spaces
        if (name.trim().equals("")) {
            name = "No Name";
        }
        mId = id;
        mName = name;
        mImageUrl = imageUrl;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mName = mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }
}