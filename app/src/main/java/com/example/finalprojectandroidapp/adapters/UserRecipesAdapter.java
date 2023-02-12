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
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import com.example.finalprojectandroidapp.R;
import com.example.finalprojectandroidapp.UploadRecipe;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//  The purpose of the recyclerView is instead of loading all the objects from scratch, it loads only the objects currently on the screen.
//  They scroll down the screen, get thrown in the trash. This saves a lot of memory.
//        It has several important functions -
//        1. getItemCount = returns how many objects there are.
//        2. onBindViewHolder = the most important function, it is called every time the user scrolls the screen and it is necessary to create new objects.
//        3. onCreateViewHolder = a function that creates the layout that includes View objects and feeds the viewHolder object.

//   So why do you need the adapter?
//        We extracted information from the API and created an array of this information (mUploads).
//        This array will be sent into the adapter, the adapter will disassemble the array and create lists - it builds a cardView and then throws it to the
//        recyclerView.

public class UserRecipesAdapter extends RecyclerView.Adapter<UserRecipesAdapter.ImageViewHolder> {

    //Initial
    Context mContext;
    List<UploadRecipe> mUploads;
    private OnItemClickListener mListener;
    private Bundle mBundle, myBundle;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private Map<String, Boolean> favorite;

    //Constructor
    public UserRecipesAdapter(Context context, List<UploadRecipe> uploads, Bundle mBundle) {
        this.mContext = context;
        this.mUploads = uploads;
        this.mBundle = mBundle;
        this.myBundle = mBundle;
        this.favorite = new HashMap<>();
        mSharedPreferences = context.getSharedPreferences("favorite_button_state", Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_recipes_user_recipes, parent, false);
        Button favoriteBtn = view.findViewById(R.id.favoriteBtn);
        final ImageViewHolder holder = new ImageViewHolder(view);

        //If the user press on the "heart"
        favoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Getting the user is using bundle (Retrieve the ID of the user who is using the app)
                String IDUser = mBundle.getString("IDUser");

                //Update the favorite state for the corresponding UploadRecipe object
                //(Get the current "favorite" state of the recipe from the mUploads list and update it based on the
                // ID of the user)
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
                    //If in the "favoriteBy" in fireBase the id of the user is true, than this means this recipe is "favorite" for this user
                    //So I want to create "favorite" child with all this details:
                    if (favorite.containsKey(IDUser)) {
                        Log.d("TagIdUserIf", IDUser);
                        Map<String, Object> children = new HashMap<>();
                        children.put("recipeImageUrl", mUploads.get(holder.getAdapterPosition()).getRecipeImageUrl());
                        children.put("recipeInstructions", mUploads.get(holder.getAdapterPosition()).getRecipeInstructions());
                        children.put("recipeName", mUploads.get(holder.getAdapterPosition()).getRecipeName());
                        children.put("recipeSummary", mUploads.get(holder.getAdapterPosition()).getRecipeSummary());
                        children.put("idOfTheUserWhoUploadTheRecipe", mUploads.get(holder.getAdapterPosition()).getUserId());
                        mDatabaseRefFavoriteRecipes.child(IDUser).child(mUploads.get(holder.getAdapterPosition()).getKey()).updateChildren(children);
                        mDatabaseAllRecipes.child(mUploads.get(holder.getAdapterPosition()).getKey()).child("favorite").setValue(favorite);
                    } else {
                        Log.d("TagIdUserElse", IDUser);
                        mDatabaseRefFavoriteRecipes.child(IDUser).child(mUploads.get(holder.getAdapterPosition()).getKey()).removeValue();
                        mDatabaseAllRecipes.child(mUploads.get(holder.getAdapterPosition()).getKey()).child("favoriteBy").setValue(favorite);
                    }
                } else {
                    Log.e("TagIdUserNull", "IDUser is null");
                }
            }
        });
        //If the user press on the cardView - navigate him to the details fragment
        CardView cardView = view.findViewById(R.id.cardViewUserRecipes);
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
        Log.d("TagImageURLUserRecipesAdapter", "Image URL: " + uploadCurrent.getRecipeImageUrl());
        Picasso.get().load(recipeImage)
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(holder.imageViewUserRecipeImage);

        SharedPreferences sharedPreferences = holder.itemView.getContext().getSharedPreferences("favorite_button_state", Context.MODE_PRIVATE);

        String id = myBundle.getString("IDUser");
        Log.d("TestIdUserRecipesAdapter", id);

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

        //Initial
        TextView textViewUserRecipeName;
        ImageView imageViewUserRecipeImage;
        Button favoriteBtn;

        //Logic for delete recipes
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
                        mDatabaseRefFavoriteRecipes.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                    if (userSnapshot.hasChild(recipeKey)) {
                                        mDatabaseRefFavoriteRecipes.child(userSnapshot.getKey()).child(recipeKey).removeValue();
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    } else {
                        Log.d("TagIdUser", IDUser);
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
            MenuItem delete = menu.add(Menu.NONE, 1, 1, "Delete");
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