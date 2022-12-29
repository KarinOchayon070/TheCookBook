package com.example.finalprojectandroidapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.finalprojectandroidapp.R;

public class SelectWhichScreenFragment extends Fragment {



    public SelectWhichScreenFragment() {
        // Required empty public constructor
    }

    Button AppOriginRecipesBtn, UsersSharedRecipesBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_select_which_screen, container, false);

        //Identify the relevant elements by id
        AppOriginRecipesBtn = view.findViewById(R.id.AppOriginRecipesBtn);
        UsersSharedRecipesBtn = view.findViewById(R.id.UsersSharedRecipesBtn);

        AppOriginRecipesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_selectWhichScreen_to_userFragment);
            }
        });

        UsersSharedRecipesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_selectWhichScreen_to_userUploadRecipesFragment);
            }
        });

        return view;
    }

}