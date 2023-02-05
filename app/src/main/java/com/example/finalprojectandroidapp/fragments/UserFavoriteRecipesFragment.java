package com.example.finalprojectandroidapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.finalprojectandroidapp.R;
import com.example.finalprojectandroidapp.UploadRecipe;
import com.example.finalprojectandroidapp.adapters.UserFavoriteRecipesAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class UserFavoriteRecipesFragment extends Fragment {

    List<UploadRecipe> listOfUploadRecipes = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private UserFavoriteRecipesAdapter mAdapter;
    private List<UploadRecipe> mUploads;
    private Bundle mBundle;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public UserFavoriteRecipesFragment() {
        // Required empty public constructor
    }

    public static UserFavoriteRecipesFragment newInstance(String param1, String param2) {
        UserFavoriteRecipesFragment fragment = new UserFavoriteRecipesFragment();
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


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_favorite_recipes, container, false);

        mRecyclerView = view.findViewById(R.id.recyclerViewUserFavoritesRecipesFragment);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mBundle = getArguments();
        String IDUser = mBundle.getString("IDUser");

        mUploads = new ArrayList<>();

        final DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference("Favorite Recipes").child(IDUser);

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    UploadRecipe upload = postSnapshot.getValue(UploadRecipe.class);
                    Log.d("Snapshot", postSnapshot.toString());
                    mUploads.add(upload);
                }

                mAdapter = new UserFavoriteRecipesAdapter(getActivity(), mUploads);

                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }
}