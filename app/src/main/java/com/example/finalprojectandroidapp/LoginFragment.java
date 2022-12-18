package com.example.finalprojectandroidapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginFragment extends Fragment {

    //Initial
    Button loginBtn;
    TextView idLoginFragment, passwordLoginFragment, registerTextViewLoginFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //The current view is the login fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        //Create object of DatabaseReference class to access firebase's realtime database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://thecookbook-fcc12-default-rtdb.firebaseio.com/");

        //Identify the relevant elements by id
        idLoginFragment = view.findViewById(R.id.editTextTextIDLoginFragment);
        passwordLoginFragment = view.findViewById(R.id.editTextTextPasswordLoginFragment);
        loginBtn = view.findViewById(R.id.loginButtonLoginFragment);
        registerTextViewLoginFragment = view.findViewById(R.id.registerTextViewLoginFragment);


        //When login button is pressed...
        loginBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            //Get data from editText into string vars
                                            final String id = idLoginFragment.getText().toString();
                                            final String password = passwordLoginFragment.getText().toString();

                                            //Check if the user fill all the fields before sending data to firebase
                                            if (id.isEmpty() || password.isEmpty()) {
                                                Toast.makeText(view.getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                                            } else {
                                                databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        //Check if email is exists in firebase database
                                                        if (snapshot.hasChild(id)) {
                                                            //Email is exist in firebase database
                                                            //Now get the password of the user from firebase data and match it with user entered password
                                                            final String getPassword = snapshot.child(id).child("password").getValue(String.class);
                                                            if (getPassword.equals(password)) {
                                                                Toast.makeText(view.getContext(), "Successfully logged in", Toast.LENGTH_SHORT).show();
                                                                //If the user is admin - go to admin fragment
                                                                if(snapshot.child(id).child("isAdmin").getValue() != null){
                                                                    Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_adminFragment);
                                                                }
                                                                //If the user is regular user - go to user fragment
                                                                else {
                                                                    Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_userFragment);
                                                                }
                                                            }
                                                            else{
                                                                Toast.makeText(view.getContext(), "Wrong password", Toast.LENGTH_SHORT).show();
                                                            }
                                                        } else {
                                                            Toast.makeText(view.getContext(), "Login failed", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });
                                            }
                                        }
                                    });
                //Go to register fragment.
                registerTextViewLoginFragment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment);
                        }
                });
                return view;
                }
        }