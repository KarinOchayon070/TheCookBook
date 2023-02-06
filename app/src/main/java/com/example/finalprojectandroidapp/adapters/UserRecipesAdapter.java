package com.example.finalprojectandroidapp.adapters;

import static java.security.AccessController.getContext;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import com.example.finalprojectandroidapp.R;
import com.example.finalprojectandroidapp.UploadRecipe;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UserRecipesAdapter extends RecyclerView.Adapter<UserRecipesAdapter.ImageViewHolder> {

    Context mContext;
    List<UploadRecipe> mUploads;
    private OnItemClickListener mListener;
    private Bundle mBundle, myBunble;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private Map<String, Boolean> favorite;



    public UserRecipesAdapter(Context context, List<UploadRecipe> uploads, Bundle mBundle) {
        this.mContext = context;
        this.mUploads = uploads;
        this.mBundle = mBundle;
        this.myBunble = mBundle;
        this.favorite = new HashMap<>();


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

                String IDUser = mBundle.getString("IDUser");


                // Update the favorite state for the corresponding UploadRecipe object
                Map<String, Boolean> favorite = mUploads.get(holder.getAdapterPosition()).getFavorite();
                if (favorite == null) {
                    favorite = new HashMap<>();
                }
                favorite.put(IDUser, !favorite.containsKey(IDUser));
                mUploads.get(holder.getAdapterPosition()).setFavorite(favorite);

                // Update the background of the button
                if (favorite.containsKey(IDUser)) {
                    favoriteBtn.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.ic_favorite_orange_24dp));
                } else {
                    favoriteBtn.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.ic_baseline_favorite_border_24));
                }

                // Add or remove the recipe from the list of favorite recipes in the database
                DatabaseReference mDatabaseRefFavoriteRecipes = FirebaseDatabase.getInstance().getReference("Favorite Recipes");
                DatabaseReference mDatabaseAllRecipes = FirebaseDatabase.getInstance().getReference("Recipes Images");

                if (IDUser != null) {
                    if (favorite.containsKey(IDUser)) {
                        Log.d("tag_ID_User", IDUser);
                        Map<String, Object> children = new HashMap<>();
                        children.put("recipeImageUrl", mUploads.get(holder.getAdapterPosition()).getRecipeImageUrl());
                        children.put("recipeInstructions", mUploads.get(holder.getAdapterPosition()).getRecipeInstructions());
                        children.put("recipeName", mUploads.get(holder.getAdapterPosition()).getRecipeName());
                        children.put("recipeSummary", mUploads.get(holder.getAdapterPosition()).getRecipeSummary());
                        children.put("idOfTheUserWhoUploadTheRecipe", mUploads.get(holder.getAdapterPosition()).getUserId());
                        mDatabaseRefFavoriteRecipes.child(IDUser).child(mUploads.get(holder.getAdapterPosition()).getKey()).updateChildren(children);
                        mDatabaseAllRecipes.child(mUploads.get(holder.getAdapterPosition()).getKey()).child("favorite").setValue(favorite);
                    } else {
                        Log.d("tagbla2", IDUser);
                        mDatabaseRefFavoriteRecipes.child(IDUser).child(mUploads.get(holder.getAdapterPosition()).getKey()).removeValue();
                        mDatabaseAllRecipes.child(mUploads.get(holder.getAdapterPosition()).getKey()).child("favorite").setValue(favorite);
                    }
                } else {
                    Log.e("tag_ID_User_Null", "IDUser is null");
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

        String id = myBunble.getString("IDUser");
        Log.d("myFucking", id);

        boolean isFavorite = false;

        if(uploadCurrent.getFavorite() != null){
            if (uploadCurrent.getFavorite().containsKey(id)) {
                isFavorite = uploadCurrent.getFavorite().get(id);
            }
        }
        if (isFavorite) {
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

        TextView textViewUserRecipeName, textViewUserSummary, textViewUserRecipeInstruction;
        ImageView imageViewUserRecipeImage;
        Button favoriteBtn;

        public void deleteRecipe(int position) {
            String recipeKey = mUploads.get(getAdapterPosition()).getKey();
            // Delete the recipe from the "recipe image" section
            DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference("Recipe Images").child(recipeKey);
            mDatabaseRef.removeValue();
            // Delete the recipe from the "favorite recipes" section
            DatabaseReference mDatabaseRefFavoriteRecipes = FirebaseDatabase.getInstance().getReference("Favorite Recipes");
            String IDUser = mBundle.getString("IDUser");
            DatabaseReference mDatabaseRefUsers = FirebaseDatabase.getInstance().getReference("Users");
            DatabaseReference mDatabaseRecipesKeyFavorite = FirebaseDatabase.getInstance().getReference("Recipe Images").child(recipeKey).child("favorite");
            mDatabaseRefUsers.child(IDUser).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild("isAdmin")) {
                        mDatabaseRefFavoriteRecipes.child(IDUser).child(recipeKey).removeValue();
                        //mDatabaseRecipesKeyFavorite.child(IDUser).removeValue();
                    } else {
                        Log.d("blabla2", IDUser);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

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
//            MenuItem doWhatever = menu.add(Menu.NONE, 1, 1, "Do whatever");
            MenuItem delete = menu.add(Menu.NONE, 1, 1, "Delete");
//            doWhatever.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);
        }



        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    switch (item.getItemId()) {
                        case 1:
                            mListener.onDeleteClick(position);
                            deleteRecipe(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }


    //What happened when the user long click
    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
}

