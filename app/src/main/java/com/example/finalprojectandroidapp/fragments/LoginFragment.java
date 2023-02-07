package com.example.finalprojectandroidapp.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.finalprojectandroidapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginFragment extends Fragment {

    //Initial
    Button loginBtn;
    TextView editTextTextIDLoginFragment, passwordLoginFragment, registerTextViewLoginFragment, editTextTextEmailLoginFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //The current view is the login fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        //Create object of DatabaseReference class to access firebase's realtime database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://thecookbook-fcc12-default-rtdb.firebaseio.com/");

        //Identify the relevant elements by id
        editTextTextIDLoginFragment = view.findViewById(R.id.editTextTextIDLoginFragment);
        passwordLoginFragment = view.findViewById(R.id.editTextTextPasswordLoginFragment);
        loginBtn = view.findViewById(R.id.loginButtonLoginFragment);
        registerTextViewLoginFragment = view.findViewById(R.id.registerTextViewLoginFragment);
        editTextTextEmailLoginFragment = view.findViewById(R.id.editTextTextEmailLoginFragment);

        //When login button is pressed...
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Get data from editText into string vars
                final String id = editTextTextIDLoginFragment.getText().toString();
                final String password = passwordLoginFragment.getText().toString();
                final String email = editTextTextEmailLoginFragment.getText().toString();

                //Check if the user fill all the fields before sending data to firebase
                if (email.isEmpty() || password.isEmpty() || id.isEmpty()) {
                    Toast.makeText(view.getContext(), "Please Fill All Fields", Toast.LENGTH_SHORT).show();
                } else {
                    // Listens for a single value change under the "Users" child in Firebase Database
                    databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            //Tries to sign in with the provided email and password using
                            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                        @Override
                                        public void onSuccess(AuthResult authResult) {
                                            Log.d("LoginSuccessful", "Login successful!");
                                            //Create bundle - this is being used to pass data between Android fragments, with the "IDUser" key-value pair
                                            // serving as a way to identify the current user in the app
                                            Bundle bundle = new Bundle();
                                            bundle.putString("IDUser", id);
                                            //If the sign-in is successful, it checks the value of "isAdmin" child of the user with the provided id
                                            // under the "Users" child in the Firebase Database.
                                            // If the value is not null, it means the user is an admin and the code navigates the user to an Admin
                                            // fragment with a Toast message
                                            if (snapshot.child(id).child("isAdmin").getValue() != null) {
                                                Toast.makeText(view.getContext(), "Hi There You Cool Admin!", Toast.LENGTH_SHORT).show();
                                                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_selectWhichScreen, bundle);
                                            }
                                            //If the value is null, it means the user is a regular user and the code navigates the user to a User fragment
                                            // with a Toast message
                                            else {
                                                Toast.makeText(view.getContext(), "Successfully Logged In", Toast.LENGTH_SHORT).show();
                                                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_selectWhichScreen, bundle);
                                            }
                                        }
                                    })
                                    //If the sign-in fails, a failure message is displayed to the user with a Toast message
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("LoginFailed", "Login failed:", e);
                                            Toast.makeText(view.getContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }
            }
        });
        //Go to register fragment
        registerTextViewLoginFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment);
            }
        });
        return view;
    }
}