package com.example.finalprojectandroidapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalprojectandroidapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Document;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class RegisterFragment extends Fragment {

    //Initial
    Button registerBtn;
    TextView fullNameRegisterFragment, emailRegisterFragment, editTextTexIDRegisterFragment,
            passwordRegisterFragment, editTextTextConfirmPasswordRegisterFragment, loginTextViewRegisterFragment;


    private String mParam;
    private static final String ARG_PARAM = "param1";


    public static RegisterFragment newInstance(String userId) {
        RegisterFragment fragment = new RegisterFragment();
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

        //The current view is the register fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        //Create object of DatabaseReference class to access firebase's realtime database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://thecookbook-fcc12-default-rtdb.firebaseio.com/");

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


        //Identify the relevant elements by id
        fullNameRegisterFragment = view.findViewById(R.id.editTextFullNameRegisterFragment);
        emailRegisterFragment = view.findViewById(R.id.editTextTextEmailAddressRegisterFragment);
        editTextTexIDRegisterFragment = view.findViewById(R.id.editTextTexIDRegisterFragment);
        passwordRegisterFragment = view.findViewById(R.id.editTextTextPasswordRegisterFragment);
        editTextTextConfirmPasswordRegisterFragment = view.findViewById(R.id.editTextTextConfirmPasswordRegisterFragment);
        registerBtn = view.findViewById(R.id.registerButtonRegisterFragment);
        loginTextViewRegisterFragment = view.findViewById(R.id.loginTextViewRegisterFragment);


        Bundle bundle = new Bundle();

        //When register button is pressed...
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Get data from editText into string vars
                final String fullName = fullNameRegisterFragment.getText().toString();
                final String email = emailRegisterFragment.getText().toString();
                final String id = editTextTexIDRegisterFragment.getText().toString();
                final String password = passwordRegisterFragment.getText().toString();
                final String confirmPassword = editTextTextConfirmPasswordRegisterFragment.getText().toString();

                //Check if the user fill all the fields before sending data to firebase
                if (fullName.isEmpty() || email.isEmpty() || id.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(view.getContext(), "Please Fill All Fields", Toast.LENGTH_SHORT).show();
                }

                //For the information about the user to be saved in authentication - the password must have 6 or more characters
                else if(password.length() < 6 || confirmPassword.length() < 6){
                    Toast.makeText(view.getContext(), "Password Must Have 6 Characters Or More", Toast.LENGTH_SHORT).show();
                }

                //Check if the passwords are matching with each other
                else if (!passwordRegisterFragment.getText().toString().equals(editTextTextConfirmPasswordRegisterFragment.getText().toString())) {
                    Toast.makeText(view.getContext(), "Different Passwords Have Been Entered", Toast.LENGTH_SHORT).show();
                } else {
                    databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            //Check if ID is not registered before
                            if (snapshot.hasChild(id)) {
                                Toast.makeText(view.getContext(), "ID Is Already Registered", Toast.LENGTH_SHORT).show();

                            } else {
                                //Sending data to firebase realtime database
                                //I am using the id of each user as unique identity of every user
                                //All the other details (full name, email, password, etc..) comes under the user's id
                                bundle.putString("IDUser", id);
                                databaseReference.child("Users").child(id).child("fullName").setValue(fullName);
                                databaseReference.child("Users").child(id).child("email").setValue(email);
//                                databaseReference.child("Users").child(id).child("password").setValue(password);
                                //In this way, I will know how to distinguish between two types of users - user/administrator
                                //Since there are not many administrators in the system (only me) - I manually added the option to
                                // "isAdmin" in my database
                                databaseReference.child("Users").child(id).child("isUser").setValue("1");
                                //Show the user a success message
                                Toast.makeText(view.getContext(), "User Registered Successfully", Toast.LENGTH_SHORT).show();



                                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                            @Override
                                            public void onSuccess(AuthResult authResult) {
                                                // User has been successfully created
                                                // You can now save the user's details in the Realtime Database
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // There was an error creating the user
                                                // You can show an error message to the user
                                            }
                                        });
                                Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_selectWhichScreen, bundle);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(view.getContext(), "Failed To Create An Account", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        //When login option is pressed... at the bottom of the screen there is a line that says if you have an existing account, click here
        //Clicking "login" will take you back to login fragment
        loginTextViewRegisterFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment, bundle);
            }
        });
        return view;
    }
}
