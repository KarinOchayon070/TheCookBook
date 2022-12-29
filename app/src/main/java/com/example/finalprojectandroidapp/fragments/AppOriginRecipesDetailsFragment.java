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


public class AppOriginRecipesDetailsFragment extends Fragment {

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

        progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setTitle("Loading details");
        progressDialog.show();


        final AppOriginRecipesDetailsListener appOriginRecipesDetailsListener = new AppOriginRecipesDetailsListener() {
            @Override
            public void didFetch(RecipesDetailsResponse response, String msg) {
                progressDialog.dismiss();
                recipeNameDetailsFragment.setText(response.title);
//                recipeSummaryDetailsFragment.setText(response.summary);
//                recipeTestDetailsFragment.setText(response.instructions);
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