package com.example.finalprojectandroidapp.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

// This fragment displays a list of favorite recipes saved by a user in a RecyclerView.
// The RecyclerView is populated with data from a Firebase Realtime Database, which is
// queried using the user's unique ID to retrieve the corresponding list of favorite recipes saved by the user.
// The fragment sets an event listener on the database reference and retrieves the data in real-time when it changes.
// The retrieved data is then passed to the RecyclerView adapter for display.

public class UserFavoriteRecipesFragment extends Fragment {

    //Initial
    List<UploadRecipe> listOfUploadRecipes = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private UserFavoriteRecipesAdapter mAdapter;
    private List<UploadRecipe> mUploads;
    private Bundle mBundle;

    public UserFavoriteRecipesFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //The current view is the favorite recipes fragment
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