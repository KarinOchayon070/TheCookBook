package com.example.finalprojectandroidapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.finalprojectandroidapp.R;
import com.example.finalprojectandroidapp.models.Recipe;
import com.squareup.picasso.Picasso;
import java.util.List;

public class RecipesAdapter extends RecyclerView.Adapter<RecipeViewHolder>{

    Context context;
    List<Recipe> recipesList;

    public RecipesAdapter(Context context, List<Recipe> recipesList){
        this.context = context;
        this.recipesList = recipesList;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_recipes, parent, false);
        return new RecipeViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        holder.recipeNameTitleUserFragment.setText(recipesList.get(position).title);
        //Now the title will have horizontal scrolling effect
        holder.recipeNameTitleUserFragment.setSelected(true);
        holder.textViewTimeInMinUserFragment.setText(recipesList.get(position).readyInMinutes + " Minutes");
        holder.textViewNumOfServingUserFragment.setText(recipesList.get(position).servings + " Servings");
        Picasso.get().load(recipesList.get(position).image).into(holder.imageviewRecipeUserFragment);
    }

    @Override
    public int getItemCount() {
        return recipesList.size();
    }
}


class RecipeViewHolder extends RecyclerView.ViewHolder{

    CardView cardViewUserFragment;
    TextView recipeNameTitleUserFragment, textViewTimeInMinUserFragment, textViewNumOfServingUserFragment;
    ImageView imageviewRecipeUserFragment;

    public RecipeViewHolder(@NonNull View itemView) {
        //Must create the super - unless, it's showing an error
        super(itemView);
        cardViewUserFragment = itemView.findViewById(R.id.cardViewUserFragment);
        recipeNameTitleUserFragment = itemView.findViewById(R.id.recipeNameTitleUserFragment);
        textViewTimeInMinUserFragment = itemView.findViewById(R.id.textViewTimeInMinUserFragment);
        textViewNumOfServingUserFragment = itemView.findViewById(R.id.textViewNumOfServingUserFragment);
        imageviewRecipeUserFragment = itemView.findViewById(R.id.imageviewRecipeUserFragment);
    }
}