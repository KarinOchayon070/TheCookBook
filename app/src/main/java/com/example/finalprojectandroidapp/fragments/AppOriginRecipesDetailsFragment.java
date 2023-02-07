package com.example.finalprojectandroidapp.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.finalprojectandroidapp.R;
import com.example.finalprojectandroidapp.RequestManagerApi;
import com.example.finalprojectandroidapp.listeners.AppOriginRecipesDetailsListener;
import com.example.finalprojectandroidapp.models.RecipesDetailsResponse;
import com.squareup.picasso.Picasso;

//        When this fragment is created, it retrieves a recipe ID passed as an argument and makes an
//        API request to retrieve details about the recipe using RequestManagerApi.
//        The details are displayed in a progress dialog until they are fetched and then they are displayed in text views and an image view.
//        The details include the recipe name, summary, instructions, and an image.

public class AppOriginRecipesDetailsFragment extends Fragment {

    //Initial
    int RecipeId;
    TextView recipeNameDetailsFragment, recipeSummaryDetailsFragment, recipeTestDetailsFragment;
    ImageView recipeImageDetailsFragment;
    RequestManagerApi requestManagerApi;
    ProgressDialog progressDialog;

    public AppOriginRecipesDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_app_origin_recipes_details, container, false);

        //This is how I get the recipe id from the "AppOriginRecipesFragment"
        RecipeId = Integer.parseInt(getArguments().getString("recipeID"));

        //Identify the relevant elements by id
        recipeNameDetailsFragment = view.findViewById(R.id.TextViewRecipeNameUserDetailsFragment);
        recipeSummaryDetailsFragment = view.findViewById(R.id.recipeSummaryUserDetailsFragment);
        recipeImageDetailsFragment = view.findViewById(R.id.recipeImageUserDetailsFragment);
        recipeTestDetailsFragment = view.findViewById(R.id.recipeTestUserDetailsFragment);

        //ProgressDialog for show the user the details are loading
        progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setTitle("Loading details");
        progressDialog.show();

        final AppOriginRecipesDetailsListener appOriginRecipesDetailsListener = new AppOriginRecipesDetailsListener() {
            @Override
            public void didFetch(RecipesDetailsResponse response, String msg) {
                progressDialog.dismiss();
                recipeNameDetailsFragment.setText(response.title);
                Picasso.get().load(response.image).into(recipeImageDetailsFragment);
                String summaryWithoutHtml = Html.fromHtml(response.summary).toString();
                String instructionsWithoutHtml = Html.fromHtml(response.instructions).toString();
                recipeSummaryDetailsFragment.setText(summaryWithoutHtml);
                recipeTestDetailsFragment.setText(instructionsWithoutHtml);
            }
            @Override
            public void didError(String msg) {
            }
        };
        requestManagerApi = new RequestManagerApi(view.getContext());
        requestManagerApi.getRecipesDetails(appOriginRecipesDetailsListener, RecipeId);
        return view;
    }
}