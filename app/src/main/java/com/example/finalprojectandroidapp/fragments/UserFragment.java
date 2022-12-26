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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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
    Spinner spinner;
    List<String> tags = new ArrayList<>();


    public UserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //I create progress dialog that will pop at the start, until all the recipes will upload.
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setTitle("Loading Recipes");

        //Create an instance of "requestManagerApi"
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

        //This lines take care of the spinner.
        //I created two layout files (spinner_text, spinner_inner_text) that styles how to spinner looks.
        spinner = view.findViewById(R.id.spinner_tags);
        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(view.getContext(),R.array.tags, R.layout.spinner_text);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_inner_text);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(spinnerSelectedListener);

//        requestManagerApi.getRecipes(recipeResponseListener);
//        progressDialog.show();
        return view;
    }
}