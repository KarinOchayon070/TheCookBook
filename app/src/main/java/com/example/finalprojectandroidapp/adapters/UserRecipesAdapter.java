package com.example.finalprojectandroidapp.adapters;

import android.content.Context;
import android.content.SharedPreferences;
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
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import com.example.finalprojectandroidapp.R;
import com.example.finalprojectandroidapp.UploadRecipe;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UserRecipesAdapter extends RecyclerView.Adapter<UserRecipesAdapter.ImageViewHolder> {

    Context mContext;
    List<UploadRecipe> mUploads;
    private OnItemClickListener mListener;
    private Bundle mBundle;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;


    public UserRecipesAdapter(Context context, List<UploadRecipe> uploads, Bundle mBundle) {
        this.mContext = context;
        this.mUploads = uploads;
        this.mBundle = mBundle;
        mSharedPreferences = context.getSharedPreferences("favorite_button_state", Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }


    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_recipes_user_recipes, parent, false);
        Button favoriteBtn = view.findViewById(R.id.favoriteBtn);
        final ImageViewHolder holder = new ImageViewHolder(view);

        favoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Toggle the value of isFavorite for the corresponding UploadRecipe object
                mUploads.get(holder.getAdapterPosition()).setIsFavorite(!mUploads.get(holder.getAdapterPosition()).isFavorite());

                // Update shared preferences
                if (mUploads.get(holder.getAdapterPosition()).isFavorite()) {
                    mEditor.putBoolean(mUploads.get(holder.getAdapterPosition()).getKey(), true);
                } else {
                    mEditor.putBoolean(mUploads.get(holder.getAdapterPosition()).getKey(), false);
                }
                mEditor.apply();

                // Update the background of the button
                if (mUploads.get(holder.getAdapterPosition()).isFavorite()) {
                    favoriteBtn.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.ic_favorite_orange_24dp));
                } else {
                    favoriteBtn.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.ic_baseline_favorite_border_24));
                }
                // Add or remove the recipe from the list of favorite recipes in the database
                DatabaseReference mDatabaseRefFavoriteRecipes = FirebaseDatabase.getInstance().getReference("Favorite Recipes");
                String IDUser = mBundle.getString("IDUser");
                if (mUploads.get(holder.getAdapterPosition()).isFavorite()) {
                    Map<String, Object> children = new HashMap<>();
                    children.put("recipeImageUrl", mUploads.get(holder.getAdapterPosition()).getRecipeImageUrl());
                    children.put("recipeIngredient", mUploads.get(holder.getAdapterPosition()).getRecipeIngredient());
                    children.put("recipeInstructions", mUploads.get(holder.getAdapterPosition()).getRecipeInstructions());
                    children.put("recipeName", mUploads.get(holder.getAdapterPosition()).getRecipeName());
                    children.put("recipeSummary", mUploads.get(holder.getAdapterPosition()).getRecipeSummary());
                    children.put("idOfTheUserWhoUploadTheRecipe", mUploads.get(holder.getAdapterPosition()).getUserId());
                    mDatabaseRefFavoriteRecipes.child(IDUser).child(mUploads.get(holder.getAdapterPosition()).getKey()).updateChildren(children);
                }
                else {
                    mDatabaseRefFavoriteRecipes.child(IDUser).child(mUploads.get(holder.getAdapterPosition()).getKey()).removeValue();
                }

            }

        });


        CardView cardView = view.findViewById(R.id.cardViewUserRecipes);
//        final ImageViewHolder holder = new ImageViewHolder(view);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();


                bundle.putString("image", mUploads.get(holder.getAdapterPosition()).getRecipeImageUrl());
                bundle.putString("name",mUploads.get(holder.getAdapterPosition()).getRecipeName());
                bundle.putString("summary",mUploads.get(holder.getAdapterPosition()).getRecipeSummary());
                bundle.putString("ingredient",mUploads.get(holder.getAdapterPosition()).getRecipeIngredient());
                bundle.putString("instructions",mUploads.get(holder.getAdapterPosition()).getRecipeInstructions());


                Navigation.findNavController(view).navigate(R.id.action_userUploadRecipesFragment_to_userPersonalRecipesDetailsFragment, bundle);
            }
        });
        return holder;

    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {

        UploadRecipe uploadCurrent = mUploads.get(position);
        String recipeName = uploadCurrent.getRecipeName();
        String recipeImage = uploadCurrent.getRecipeImageUrl();
        holder.textViewUserRecipeName.setText(recipeName);
        Log.d("MYTAG", "Image URL: " + uploadCurrent.getRecipeImageUrl());
        Picasso.get().load(recipeImage)
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(holder.imageViewUserRecipeImage);

        SharedPreferences sharedPreferences = holder.itemView.getContext().getSharedPreferences("favorite_button_state", Context.MODE_PRIVATE);
        boolean isFavorite = sharedPreferences.getBoolean(uploadCurrent.getKey(), false);
        uploadCurrent.setIsFavorite(isFavorite);

        // Update the background of the button
        if (uploadCurrent.isFavorite()) {
            holder.favoriteBtn.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.ic_favorite_orange_24dp));
        } else {
            holder.favoriteBtn.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.ic_baseline_favorite_border_24));
        }

    }


    @Override
    public int getItemCount() {
        return mUploads.size();
    }


    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,  View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        TextView textViewUserRecipeName, textViewUserSummary, textViewUserRecipeIngredient, textViewUserRecipeInstruction;
        ImageView imageViewUserRecipeImage;
        Button favoriteBtn;

        public ImageViewHolder(View itemView) {

            super(itemView);
            textViewUserRecipeName = itemView.findViewById(R.id.textViewUserRecipeName);
            imageViewUserRecipeImage = itemView.findViewById(R.id.imageViewUserRecipeImage);
            favoriteBtn = itemView.findViewById(R.id.favoriteBtn);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);

        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position);
                }
            }

        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select Action");
            MenuItem doWhatever = menu.add(Menu.NONE, 1, 1, "Do whatever");
            MenuItem delete = menu.add(Menu.NONE, 2, 2, "Delete");

            doWhatever.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {

                    switch (item.getItemId()) {
                        case 1:
                            mListener.onWhatEverClick(position);
                            return true;
                        case 2:
                            mListener.onDeleteClick(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }


    //What happened when the user long cilck
    public interface OnItemClickListener {
        void onItemClick(int position);

        void onWhatEverClick(int position);

        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


}

