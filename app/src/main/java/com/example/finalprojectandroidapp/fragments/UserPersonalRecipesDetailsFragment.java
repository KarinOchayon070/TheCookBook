package com.example.finalprojectandroidapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.finalprojectandroidapp.R;
import com.example.finalprojectandroidapp.UploadRecipeImage;
import com.squareup.picasso.Picasso;

public class UserPersonalRecipesDetailsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserPersonalRecipesDetailsFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static UserPersonalRecipesDetailsFragment newInstance(String param1, String param2) {
        UserPersonalRecipesDetailsFragment fragment = new UserPersonalRecipesDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_user_personal_recipes_details, container, false);


        TextView recipeName = view.findViewById(R.id.TextViewRecipeNameUserDetailsFragment);
        recipeName.setText(getArguments().getString("name"));


        ImageView recipeImage = view.findViewById(R.id.recipeImageUserDetailsFragment);
        String imageUrl = getArguments().getString("image");
        Picasso.get().load(imageUrl).placeholder(R.mipmap.ic_launcher).fit().centerCrop().into(recipeImage);


        TextView recipeSummary = view.findViewById(R.id.recipeSummaryUserDetailsFragment);
        recipeSummary.setText(getArguments().getString("summary"));


        TextView recipeInstructions = view.findViewById(R.id.recipeTestUserDetailsFragment);
        recipeInstructions.setText(getArguments().getString("instructions"));



        return view;
    }
}




