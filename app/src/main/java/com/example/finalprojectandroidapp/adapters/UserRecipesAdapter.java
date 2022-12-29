package com.example.finalprojectandroidapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.finalprojectandroidapp.R;
import com.example.finalprojectandroidapp.UploadRecipeImage;
import com.squareup.picasso.Picasso;
import java.util.List;


public class UserRecipesAdapter extends RecyclerView.Adapter<UserRecipesAdapter.ImageViewHolder> {

    Context mContext;
    List<UploadRecipeImage> mUploads;


    public UserRecipesAdapter(Context context, List<UploadRecipeImage> uploads) {
        this.mContext = context;
        this.mUploads = uploads;
    }
    @Override
    public ImageViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_recipes, parent, false);

        return new ImageViewHolder(v);

    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {

        UploadRecipeImage uploadCurrent = mUploads.get(position);
        String recipeName = uploadCurrent.getRecipeName();
        String recipeImage = uploadCurrent.getRecipeImageUrl();
        holder.textViewUserRecipeName.setText(recipeName);
        Log.d("MYTAG", "Image URL: " + uploadCurrent.getRecipeImageUrl());
        Picasso.get().load(recipeImage)
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(holder.imageViewUserRecipeImage);
    }


    @Override
    public int getItemCount() {
        return mUploads.size();
    }


    public class ImageViewHolder extends RecyclerView.ViewHolder {

        TextView textViewUserRecipeName, textViewUserSummary, textViewUserRecipeIngredient,  textViewUserRecipeInstruction;
        ImageView imageViewUserRecipeImage;
        public ImageViewHolder(View itemView) {
            super(itemView);
            textViewUserRecipeName = itemView.findViewById(R.id.textViewUserRecipeName);
            imageViewUserRecipeImage = itemView.findViewById(R.id.imageViewUserRecipeImage);

//            textViewUserSummary = itemView.findViewById(R.id.textViewUserSummary);
//            textViewUserRecipeIngredient = itemView.findViewById(R.id.textViewUserRecipeIngredient);
//            textViewUserRecipeInstruction = itemView.findViewById(R.id.textViewUserRecipeInstruction);
        }
    }
}

