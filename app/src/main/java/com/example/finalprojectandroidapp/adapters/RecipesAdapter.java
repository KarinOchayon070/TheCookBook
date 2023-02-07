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
import com.example.finalprojectandroidapp.listeners.AppOriginRecipesClickListener;
import com.example.finalprojectandroidapp.models.Recipe;
import com.squareup.picasso.Picasso;
import java.util.List;


//  The purpose of the recyclerView is instead of loading all the objects from scratch, it loads only the objects currently on the screen.
//  They scroll down the screen, get thrown in the trash. This saves a lot of memory.
//        It has several important functions -
//        1. getItemCount = returns how many objects there are.
//        2. onBindViewHolder = the most important function, it is called every time the user scrolls the screen and it is necessary to create new objects.
//        3. onCreateViewHolder = a function that creates the layout that includes View objects and feeds the viewHolder object.

//   So why do you need the adapter?
//        We extracted information from the API and created an array of this information (recipesList).
//        This array will be sent into the adapter, the adapter will disassemble the array and create lists - it builds a cardView and then throws it to the recyclerView.

public class RecipesAdapter extends RecyclerView.Adapter<RecipeViewHolder>{

    //Initial
    Context context;
    List<Recipe> recipesList;
    AppOriginRecipesClickListener appOriginRecipesClickListener;

    //Constructor
    public RecipesAdapter(Context context, List<Recipe> recipesList, AppOriginRecipesClickListener appOriginRecipesClickListener){
        this.context = context;
        this.recipesList = recipesList;
        //This is the listener of when the user click on one of the app recipe
        this.appOriginRecipesClickListener = appOriginRecipesClickListener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_recipes_app_origin_recipes, parent, false);
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

        //Go to the card view
        holder.cardViewUserFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Call the listener (the id is an int so I convert it)
                appOriginRecipesClickListener.onAppOriginRecipeClicked(String.valueOf(recipesList.get(holder.getAdapterPosition()).id));
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipesList.size();
    }
}

class RecipeViewHolder extends RecyclerView.ViewHolder{

    //Initial
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