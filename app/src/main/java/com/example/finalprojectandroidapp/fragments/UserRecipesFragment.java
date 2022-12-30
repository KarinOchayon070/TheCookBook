package com.example.finalprojectandroidapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.finalprojectandroidapp.R;
import com.example.finalprojectandroidapp.UploadRecipe;
import com.example.finalprojectandroidapp.adapters.UserRecipesAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


public class UserRecipesFragment extends Fragment implements UserRecipesAdapter.OnItemClickListener {

    RecyclerView mRecyclerView;
    UserRecipesAdapter mAdapter;
    ProgressBar mProgressCircle;
    DatabaseReference mDatabaseRef;
    List<UploadRecipe> mUploads;
    LinearLayoutManager layoutManager;
    FirebaseStorage mStorage;
    ValueEventListener mDBListener;
    String IDUser="";




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_upload_recipes, container, false);


        IDUser += getArguments().getString("IDUser");
        Log.d("tagLetsTest", IDUser);
        Bundle bundle = new Bundle();
        bundle.putString("IDUser", IDUser);



        mRecyclerView = view.findViewById(R.id.recycler_view_user_images);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        mProgressCircle = view.findViewById(R.id.progress_circle);

        mUploads = new ArrayList<>();

        Button buttonUserUploadRecipe =  view.findViewById(R.id.buttonUserUploadRecipe);





        buttonUserUploadRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_userUploadRecipesFragment_to_userPersonalRecipes, bundle);
            }
        });



//        layoutManager = new LinearLayoutManager(getContext()); // new GridLayoutManager
//        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new UserRecipesAdapter(view.getContext(), mUploads);
        mRecyclerView.setAdapter(mAdapter);
//        mProgressCircle.setVisibility(View.INVISIBLE);
        mAdapter.setOnItemClickListener(this);

        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Recipes Images");

        mDBListener =  mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mUploads.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    UploadRecipe upload = postSnapshot.getValue(UploadRecipe.class);
                    upload.setKey(postSnapshot.getKey());
                    mUploads.add(upload);
                }
                mAdapter.notifyDataSetChanged();
                mProgressCircle.setVisibility(View.INVISIBLE);
//                layoutManager = new LinearLayoutManager(getContext()); // new GridLayoutManager
//                mRecyclerView.setLayoutManager(layoutManager);
//                mAdapter = new UserRecipesAdapter(view.getContext(), mUploads);
//                mRecyclerView.setAdapter(mAdapter);
//                mProgressCircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(view.getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });
        return view;
    }


    @Override
    public void onItemClick(int position) {
        Toast.makeText(getView().getContext(), "Normal click at position: " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onWhatEverClick(int position) {
        Toast.makeText(getView().getContext(), "Whatever click at position: " + position, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onDeleteClick(int position) {
        if(IDUser == " "){
            UploadRecipe selectedItem = mUploads.get(position);
            final String selectedKey = selectedItem.getKey();

            StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getRecipeImageUrl());

            imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    mDatabaseRef.child(selectedKey).removeValue();
                    Log.d("tagchecksucces", mDatabaseRef.child(selectedKey).toString());
                    Toast.makeText(getView().getContext(), "Recipe Deleted", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            Toast.makeText(getView().getContext(), "This User Is Not Allowed To Delete This Recipe", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }
}