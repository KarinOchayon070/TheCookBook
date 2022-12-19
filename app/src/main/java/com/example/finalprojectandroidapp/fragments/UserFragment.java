package com.example.finalprojectandroidapp.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.finalprojectandroidapp.R;
import com.example.finalprojectandroidapp.RequestManagerApi;
import com.example.finalprojectandroidapp.adapters.RecipesAdapter;
import com.example.finalprojectandroidapp.listeners.RecipeResponseListener;
import com.example.finalprojectandroidapp.models.Recipe;
import com.example.finalprojectandroidapp.models.RecipesApiResponse;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import org.checkerframework.checker.units.qual.C;
import java.util.ArrayList;
import java.util.List;


public class UserFragment extends Fragment {

    ProgressDialog progressDialog;
    RequestManagerApi requestManagerApi;
    RecipesAdapter recipesAdapter;
    RecyclerView recycleView;
    LinearLayoutManager layoutManager;


    public UserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user, container, false);
        progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setTitle("Loading Recipes");
        requestManagerApi = new RequestManagerApi(view.getContext());

        RecipeResponseListener recipeResponseListener = new RecipeResponseListener() {
            @Override
            public void didFetch(RecipesApiResponse recipesApiResponse, String msg) {
                recycleView = (RecyclerView)view.findViewById(R.id.recyclerViewUserFragment);
                recycleView.setHasFixedSize(true);
                layoutManager = new LinearLayoutManager(getContext()); // new GridLayoutManager
                recycleView.setLayoutManager(layoutManager);
                recipesAdapter = new RecipesAdapter(view.getContext(), recipesApiResponse.recipes);
                recycleView.setAdapter(recipesAdapter);
            }
            @Override
            public void didError(String msg) {
                Toast.makeText(getView().getContext(), msg, Toast.LENGTH_SHORT).show();
            }
        };
        requestManagerApi.getRecipes(recipeResponseListener);
        progressDialog.show();
        return view;
    }
}