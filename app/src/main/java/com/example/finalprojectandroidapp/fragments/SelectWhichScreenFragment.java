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

import org.checkerframework.checker.units.qual.C;

public class SelectWhichScreenFragment extends Fragment {

    private String mParam;
    private static final String ARG_PARAM = "param1";


    public static SelectWhichScreenFragment newInstance(String userId) {
        SelectWhichScreenFragment fragment = new SelectWhichScreenFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM,userId);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam = getArguments().getString(ARG_PARAM);
        }
    }


    public SelectWhichScreenFragment() {
        // Required empty public constructor
    }

    Button AppOriginRecipesBtn, UsersSharedRecipesBtn, YourFavoriteRecipesBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_select_which_screen, container, false);

        //Identify the relevant elements by id
        AppOriginRecipesBtn = view.findViewById(R.id.AppOriginRecipesBtn);
        UsersSharedRecipesBtn = view.findViewById(R.id.UsersSharedRecipesBtn);
        YourFavoriteRecipesBtn = view.findViewById(R.id.YourFavoriteRecipesBtn);

        String IDUser = getArguments().getString("IDUser");
        Log.d("tagLetsTest", IDUser);

        Bundle bundle = new Bundle();
        bundle.putString("IDUser", IDUser);


        AppOriginRecipesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_selectWhichScreen_to_userFragment, bundle);
            }
        });

        UsersSharedRecipesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_selectWhichScreen_to_userUploadRecipesFragment, bundle);
            }
        });

        YourFavoriteRecipesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_selectWhichScreen_to_userFavoriteRecipesFragment, bundle);
            }
        });

        return view;
    }

}