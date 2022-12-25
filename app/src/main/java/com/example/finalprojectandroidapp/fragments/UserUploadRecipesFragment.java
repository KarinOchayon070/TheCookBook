package com.example.finalprojectandroidapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.finalprojectandroidapp.R;
import com.example.finalprojectandroidapp.UploadRecipeImage;
import com.example.finalprojectandroidapp.adapters.UserRecipesAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class UserUploadRecipesFragment extends Fragment {

    RecyclerView mRecyclerView;
    UserRecipesAdapter mAdapter;
    ProgressBar mProgressCircle;
    DatabaseReference mDatabaseRef;
    List<UploadRecipeImage> mUploads;
    LinearLayoutManager layoutManager;

    public UserUploadRecipesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_upload_recipes, container, false);

        mRecyclerView = view.findViewById(R.id.recycler_view_user_images);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        mProgressCircle = view.findViewById(R.id.progress_circle);

        mUploads = new ArrayList<>();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Recipes Images");
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    UploadRecipeImage upload = postSnapshot.getValue(UploadRecipeImage.class);
                    mUploads.add(upload);
                }
//                mAdapter = new UserRecipesAdapter(view.getContext(), mUploads);
//                mRecyclerView.setAdapter(mAdapter);
//                mProgressCircle.setVisibility(View.INVISIBLE);

                layoutManager = new LinearLayoutManager(getContext()); // new GridLayoutManager
                mRecyclerView.setLayoutManager(layoutManager);
                mAdapter = new UserRecipesAdapter(view.getContext(), mUploads);
                mRecyclerView.setAdapter(mAdapter);
                mProgressCircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(view.getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });
        return view;
    }
}