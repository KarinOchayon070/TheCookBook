package com.example.finalprojectandroidapp.fragments;

import static android.content.ContentValues.TAG;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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

// This code is an Android fragment that implements a RecyclerView to display a list of user uploaded recipes.
// The recipes are stored in Firebase's Realtime Database and Firebase Storage.
// The fragment has a "Upload Recipe" button that navigates the user to another fragment to upload a new recipe.
// The data is retrieved from Firebase and displayed in the RecyclerView through an adapter.
// The RecyclerView uses a LinearLayoutManager to display the list of items.
// The fragment also displays a progress dialog at the start until all the recipes are loaded.
// The user can click on a recipe to view more information about it.

public class UserRecipesFragment extends Fragment implements UserRecipesAdapter.OnItemClickListener {

    //Initial
    RecyclerView mRecyclerView;
    UserRecipesAdapter mAdapter;
    DatabaseReference mDatabaseRef;
    List<UploadRecipe> mUploads;
    LinearLayoutManager layoutManager;
    FirebaseStorage mStorage;
    ValueEventListener mDBListener;
    ProgressDialog progressDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //The current view is the users recipes fragment
        View view = inflater.inflate(R.layout.fragment_user_recipes, container, false);

        //I create progress dialog that will pop at the start, until all the recipes will upload
        progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setTitle("Loading Recipes");
        progressDialog.show();

        //Bundle - this is being used to pass data between Android fragments
        String IDUser = getArguments().getString("IDUser");
        Bundle bundle = new Bundle();
        bundle.putString("IDUser", IDUser);

        mRecyclerView = view.findViewById(R.id.recycler_view_user_images);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        mUploads = new ArrayList<>();

        //When the user press on the "Upload Recipe" button, it navigate him to the fragment where he can upload his own recipe
        Button buttonUserUploadRecipe =  view.findViewById(R.id.buttonUserUploadRecipe);
        buttonUserUploadRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_userUploadRecipesFragment_to_userPersonalRecipes, bundle);
            }
        });

        mAdapter = new UserRecipesAdapter(view.getContext(), mUploads, bundle);
        mRecyclerView.setAdapter(mAdapter);
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
                progressDialog.dismiss();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(view.getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
        return view;
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(getView().getContext(), "Normal click at position: " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteClick(int position) {

        //Get the user id using bundle
        String IDUser = getArguments().getString("IDUser");
        Log.d("TagIdUserUserRecipesFragment", IDUser);

        //Transfer to the next fragment the user id
        Bundle bundle = new Bundle();
        bundle.putString("IDUser", IDUser);

        //The selected key is the unique id of each recipe that save id the firebase
        //The unique id of each recipe saved as a child of "Images Recipes"
        UploadRecipe selectedItem = mUploads.get(position);
        final String selectedKey = selectedItem.getKey();

        StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getRecipeImageUrl());

        //Get the url to the current user
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(IDUser);

        DatabaseReference recipeRef = FirebaseDatabase.getInstance().getReference("Recipes Images").child(selectedKey);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //If the current user is an admin/the user who upload the recipe - let him delete
                if (dataSnapshot.hasChild("isAdmin") || dataSnapshot.hasChild(selectedKey)) {
                    imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //If the user who upload the recipe do the delete action
                            if(dataSnapshot.hasChild(selectedKey)){
                                mDatabaseRef.child(selectedKey).removeValue();
                                userRef.child(selectedKey).removeValue();
                            }
                            if(dataSnapshot.hasChild("isAdmin")){
                                recipeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.hasChild("userId")) {
                                            String userWhoUploadId = dataSnapshot.child("userId").getValue(String.class);
                                            mDatabaseRef.child(selectedKey).removeValue();
                                            FirebaseDatabase.getInstance().getReference("Users").child(userWhoUploadId).child(selectedKey).removeValue();
                                        } else {
                                            // The "userId" field does not exist
                                        }
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        // An error occurred, log the error
                                        Log.e(TAG, "Error retrieving userId field", databaseError.toException());
                                    }
                                });
                            }
                            Toast.makeText(getView().getContext(), "Recipe Deleted", Toast.LENGTH_SHORT).show();
                        }
                    });
                    //Else - the user can't delete the current recipe
                } else {
                    Toast.makeText(getView().getContext(), "This User Is Not Allowed To Delete This Recipe", Toast.LENGTH_SHORT).show();
                }
            }
            //If something is wrong
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // An error occurred, log the error
                Log.e(TAG, "Error", databaseError.toException());
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }
}