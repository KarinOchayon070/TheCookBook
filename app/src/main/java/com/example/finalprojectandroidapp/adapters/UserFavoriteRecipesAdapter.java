package com.example.finalprojectandroidapp.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
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
import com.squareup.picasso.Picasso;
import java.util.List;

//  The purpose of the recyclerView is instead of loading all the objects from scratch, it loads only the objects currently on the screen.
//  They scroll down the screen, get thrown in the trash. This saves a lot of memory.
//        It has several important functions -
//        1. getItemCount = returns how many objects there are.
//        2. onBindViewHolder = the most important function, it is called every time the user scrolls the screen and it is necessary to create new objects.
//        3. onCreateViewHolder = a function that creates the layout that includes View objects and feeds the viewHolder object.

//   So why do you need the adapter?
//        We extracted information from the API and created an array of this information (mUploads).
//        This array will be sent into the adapter, the adapter will disassemble the array and create lists - it builds a cardView and then throws it to the recyclerView.

public class UserFavoriteRecipesAdapter extends RecyclerView.Adapter<UserFavoriteRecipesAdapter.FavoriteRecipesViewHolder>{

    //Initial
    Context mContext;
    List<UploadRecipe> mUploads;
    private UserRecipesAdapter.OnItemClickListener mListener;

    //Constructor
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

        //Initial
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



