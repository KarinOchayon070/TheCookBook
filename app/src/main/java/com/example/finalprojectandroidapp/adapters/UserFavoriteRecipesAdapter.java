package com.example.finalprojectandroidapp.adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectandroidapp.R;
import com.example.finalprojectandroidapp.UploadRecipe;
import com.example.finalprojectandroidapp.listeners.AppOriginRecipesClickListener;
import com.example.finalprojectandroidapp.models.Recipe;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserFavoriteRecipesAdapter extends RecyclerView.Adapter<UserFavoriteRecipesAdapter.FavoriteRecipesViewHolder>{

    Context mContext;
    List<UploadRecipe> mUploads;
    private UserRecipesAdapter.OnItemClickListener mListener;


    public UserFavoriteRecipesAdapter(Context context, List<UploadRecipe> uploads) {
        this.mContext = context;
        this.mUploads = uploads;
    }


    public FavoriteRecipesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_recipes_user_favorite, parent, false);
        final UserFavoriteRecipesAdapter.FavoriteRecipesViewHolder holder = new UserFavoriteRecipesAdapter.FavoriteRecipesViewHolder(view);

        CardView cardView = view.findViewById(R.id.cardViewUserFavoriteRecipes);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString("image", mUploads.get(holder.getAdapterPosition()).getRecipeImageUrl());
                bundle.putString("name",mUploads.get(holder.getAdapterPosition()).getRecipeName());
                bundle.putString("summary",mUploads.get(holder.getAdapterPosition()).getRecipeSummary());
                bundle.putString("ingredient",mUploads.get(holder.getAdapterPosition()).getRecipeIngredient());
                bundle.putString("instructions",mUploads.get(holder.getAdapterPosition()).getRecipeInstructions());
                Navigation.findNavController(view).navigate(R.id.action_userFavoriteRecipesFragment_to_userPersonalRecipesDetailsFragment, bundle);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteRecipesViewHolder holder, int position) {

        TextView name =  holder.textViewUserFavoriteRecipeName;
        ImageView image = holder.imageViewUserFavoriteRecipeImage;
        Button favoriteBtn = holder.favoriteBtn;

        UploadRecipe uploadCurrent = mUploads.get(position);
        String recipeName = uploadCurrent.getRecipeName();
        String recipeImageUrl = uploadCurrent.getRecipeImageUrl();


        holder.textViewUserFavoriteRecipeName.setText(recipeName);

        Picasso.get()
                .load(recipeImageUrl)
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(image);



    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }


    public static class FavoriteRecipesViewHolder extends RecyclerView.ViewHolder {

        TextView textViewUserFavoriteRecipeName;
        ImageView imageViewUserFavoriteRecipeImage;
        CardView cardViewUserFavoriteRecipes;
        Button favoriteBtn;

        public FavoriteRecipesViewHolder(View itemView) {
            super(itemView);
            textViewUserFavoriteRecipeName = (TextView) itemView.findViewById(R.id.textViewUserFavoriteRecipeName);
            imageViewUserFavoriteRecipeImage = itemView.findViewById(R.id.imageViewUserFavoriteRecipeImage);
            cardViewUserFavoriteRecipes = itemView.findViewById(R.id.cardViewUserFavoriteRecipes);
            favoriteBtn = itemView.findViewById(R.id.favoriteBtn);

        }
    }
}



