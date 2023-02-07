package com.example.finalprojectandroidapp.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.finalprojectandroidapp.R;
import com.squareup.picasso.Picasso;

// This code creates a fragment that displays details of a user's personal recipe.
// When the fragment is created, it inflates a layout (R.layout.fragment_user_personal_recipes_details)
// and sets the text and image in the TextView and ImageView elements in the layout.
// The details are passed to the fragment through arguments passed in the getArguments() method.
// The fragment also includes a button that navigates back to the all users recipes fragment when clicked.


public class UserPersonalRecipesDetailsFragment extends Fragment {

    public UserPersonalRecipesDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //The current view is the user personal recipes with details fragment
        View view = inflater.inflate(R.layout.fragment_user_personal_recipes_details, container, false);

        //Identify the relevant elements by id and set it to the details of the recipe
        TextView recipeName = view.findViewById(R.id.TextViewRecipeNameUserDetailsFragment);
        recipeName.setText(getArguments().getString("name"));
        ImageView recipeImage = view.findViewById(R.id.recipeImageUserDetailsFragment);
        String imageUrl = getArguments().getString("image");
        Picasso.get().load(imageUrl).placeholder(R.mipmap.ic_launcher).fit().centerCrop().into(recipeImage);
        TextView recipeSummary = view.findViewById(R.id.recipeSummaryUserDetailsFragment);
        recipeSummary.setText(getArguments().getString("summary"));
        TextView recipeInstructions = view.findViewById(R.id.recipeTestUserDetailsFragment);
        recipeInstructions.setText(getArguments().getString("instructions"));
        TextView userRecipesDetailsFragmentGoBackText = view.findViewById(R.id.userRecipesDetailsFragmentGoBackText);

        //Go back to all users recipes
        userRecipesDetailsFragmentGoBackText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_userPersonalRecipesDetailsFragment_to_userUploadRecipesFragment);
            }
        });
        return view;
    }
}




