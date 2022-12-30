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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalprojectandroidapp.R;
import com.example.finalprojectandroidapp.UploadRecipe;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.util.List;


public class UserRecipesAdapter extends RecyclerView.Adapter<UserRecipesAdapter.ImageViewHolder> {

    Context mContext;
    List<UploadRecipe> mUploads;
    private OnItemClickListener mListener;


    public UserRecipesAdapter(Context context, List<UploadRecipe> uploads) {
        this.mContext = context;
        this.mUploads = uploads;
    }


    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_recipes, parent, false);
        CardView cardView = view.findViewById(R.id.cardViewUserRecipes);
        final ImageViewHolder holder = new ImageViewHolder(view);
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
    }


    @Override
    public int getItemCount() {
        return mUploads.size();
    }


    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,  View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        TextView textViewUserRecipeName, textViewUserSummary, textViewUserRecipeIngredient, textViewUserRecipeInstruction;
        ImageView imageViewUserRecipeImage;

        public ImageViewHolder(View itemView) {

            super(itemView);
            textViewUserRecipeName = itemView.findViewById(R.id.textViewUserRecipeName);
            imageViewUserRecipeImage = itemView.findViewById(R.id.imageViewUserRecipeImage);

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

