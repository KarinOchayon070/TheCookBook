package com.example.finalprojectandroidapp.fragments;

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

import com.example.finalprojectandroidapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginFragment extends Fragment {

    //Initial
    Button loginBtn;
    TextView idLoginFragment, passwordLoginFragment, registerTextViewLoginFragment;

    private String mParam;
    private static final String ARG_PARAM = "param1";


    public static LoginFragment newInstance(String userId) {
        LoginFragment fragment = new LoginFragment();
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
                                                Toast.makeText(view.getContext(), "Please Fill All Fields", Toast.LENGTH_SHORT).show();
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
                                                                Bundle bundle = new Bundle();
                                                                bundle.putString("IDUser", id);
                                                                //If the user is admin - go to admin fragment
                                                                if(snapshot.child(id).child("isAdmin").getValue() != null){
                                                                    Toast.makeText(view.getContext(), "Hi There yOU Cool Admin!", Toast.LENGTH_SHORT).show();
                                                                    Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_selectWhichScreen, bundle);
                                                                }
                                                                //If the user is regular user - go to user fragment
                                                                else {
                                                                    Toast.makeText(view.getContext(), "Successfully Logged In", Toast.LENGTH_SHORT).show();
                                                                    Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_selectWhichScreen, bundle);
                                                                }
                                                            }
                                                            else{
                                                                Toast.makeText(view.getContext(), "Wrong Password", Toast.LENGTH_SHORT).show();
                                                            }
                                                        } else {
                                                            Toast.makeText(view.getContext(), "Login Failed", Toast.LENGTH_SHORT).show();
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