package com.example.finalprojectandroidapp.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.finalprojectandroidapp.R;
import com.example.finalprojectandroidapp.RequestManagerApi;
import com.example.finalprojectandroidapp.adapters.RecipesAdapter;
import com.example.finalprojectandroidapp.listeners.AppOriginRecipesClickListener;
import com.example.finalprojectandroidapp.listeners.AppOriginRecipesResponseListener;
import com.example.finalprojectandroidapp.models.RecipesApiResponse;
import java.util.ArrayList;
import java.util.List;

//This code represents a fragment that displays a list of recipes, fetched from a remote API.
// It has a number of functionalities:
//        1. Show a loading dialog at the start until the recipes are loaded
//        2. Show a list of recipes with a click listener that navigates the user to a new fragment for each recipe (AppOriginRecipesDetailsFragment)
//        3. Fetch the list of recipes from a remote API and display them in a recycler view
//        4. Provide a spinner that the user can use to select a tag to filter the recipes
//        5. Provide a search view that the user can use to search for a recipe

public class AppOriginRecipesFragment extends Fragment {

    //Initial
    ProgressDialog progressDialog;
    RequestManagerApi requestManagerApi;
    RecipesAdapter recipesAdapter;
    RecyclerView recycleView;
    LinearLayoutManager layoutManager;
    Spinner spinner;
    List<String> tags = new ArrayList<>();
    SearchView searchView;


    public AppOriginRecipesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //I create progress dialog that will pop at the start, until all the recipes will upload
        View view = inflater.inflate(R.layout.fragment_app_origin_recipes, container, false);
        progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setTitle("Loading Recipes");

        //Create "appOriginRecipesClickListener" object so I can put it the adapter
        //Each recipe is identify according to it's id (in the api) - so when the user will click one of the
        //recipes - it will take him to new fragment
        final AppOriginRecipesClickListener appOriginRecipesClickListener = new AppOriginRecipesClickListener() {
            @Override
            public void onAppOriginRecipeClicked(String id) {
                Bundle bundle = new Bundle();
                bundle.putString("recipeID", id);
                Navigation.findNavController(view).navigate(R.id.action_userFragment_to_appOriginRecipesDetailsFragment, bundle);
            }
        };

        //Create an instance of "requestManagerApi"
        requestManagerApi = new RequestManagerApi(view.getContext());

        AppOriginRecipesResponseListener recipeResponseListener = new AppOriginRecipesResponseListener() {
            @Override
            public void didFetch(RecipesApiResponse recipesApiResponse, String msg) {
                recycleView = (RecyclerView)view.findViewById(R.id.recyclerViewUserFavoritesRecipesFragment);
                recycleView.setHasFixedSize(true);
                layoutManager = new LinearLayoutManager(getContext()); // new GridLayoutManager
                recycleView.setLayoutManager(layoutManager);
                recipesAdapter = new RecipesAdapter(view.getContext(), recipesApiResponse.recipes, appOriginRecipesClickListener);
                recycleView.setAdapter(recipesAdapter);
                progressDialog.dismiss();

            }
            @Override
            public void didError(String msg) {
                Toast.makeText(getView().getContext(), msg, Toast.LENGTH_SHORT).show();
            }
        };

        //This is for when user choose item in the spinner
        AdapterView.OnItemSelectedListener spinnerSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tags.clear();
                tags.add(adapterView.getSelectedItem().toString());
                requestManagerApi.getRecipes(recipeResponseListener, tags);
                progressDialog.show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };

        //This lines take care of the spinner
        //I created two layout files (spinner_text, spinner_inner_text) that styles how to spinner looks.
        spinner = view.findViewById(R.id.spinnerTagsUserRecipes);
        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(view.getContext(),R.array.tags, R.layout.spinner_text);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_inner_text);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(spinnerSelectedListener);

        //When the user search for specific recipe
        searchView = view.findViewById(R.id.searchViewtheCookBookUserRecipes);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                tags.clear();
                tags.add(s);
                requestManagerApi.getRecipes(recipeResponseListener, tags);
                progressDialog.show();
                return true;
            }
            //I didn't change anything here
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        progressDialog.dismiss();
        return view;
    }
}