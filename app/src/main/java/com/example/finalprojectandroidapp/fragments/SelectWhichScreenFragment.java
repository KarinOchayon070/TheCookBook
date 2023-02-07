package com.example.finalprojectandroidapp.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.example.finalprojectandroidapp.R;

public class SelectWhichScreenFragment extends Fragment {

    //Initial
    Button AppOriginRecipesBtn, UsersSharedRecipesBtn, YourFavoriteRecipesBtn;

    public SelectWhichScreenFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_select_which_screen, container, false);

        //Identify the relevant elements by id
        AppOriginRecipesBtn = view.findViewById(R.id.AppOriginRecipesBtn);
        UsersSharedRecipesBtn = view.findViewById(R.id.UsersSharedRecipesBtn);
        YourFavoriteRecipesBtn = view.findViewById(R.id.YourFavoriteRecipesBtn);

        //This is the id bundle - We get the user's ID from the previous fragment we were in
        //and transfer it to the next fragment we move to
        String IDUser = getArguments().getString("IDUser");
        Log.d("TagIdUserSelectWhichFragments", IDUser);
        Bundle bundle = new Bundle();
        bundle.putString("IDUser", IDUser);

        //Moving to the app origin recipes fragment (the recipes from the API)
        AppOriginRecipesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_selectWhichScreen_to_userFragment, bundle);
            }
        });

        //Moving to the users recipes fragment (from there the user can upload recipes as well)
        UsersSharedRecipesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_selectWhichScreen_to_userUploadRecipesFragment, bundle);
            }
        });

        //Moving to the users favorite recipes fragment
        YourFavoriteRecipesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_selectWhichScreen_to_userFavoriteRecipesFragment, bundle);
            }
        });
        return view;
    }
}