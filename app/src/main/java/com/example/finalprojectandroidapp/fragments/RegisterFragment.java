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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //The current view is the register fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        //Create object of DatabaseReference class to access firebase's realtime database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://thecookbook-fcc12-default-rtdb.firebaseio.com/");


        //Identify the relevant elements by id
        fullNameRegisterFragment = view.findViewById(R.id.editTextFullNameRegisterFragment);
        emailRegisterFragment = view.findViewById(R.id.editTextTextEmailAddressRegisterFragment);
        editTextTexIDRegisterFragment = view.findViewById(R.id.editTextTexIDRegisterFragment);
        passwordRegisterFragment = view.findViewById(R.id.editTextTextPasswordRegisterFragment);
        editTextTextConfirmPasswordRegisterFragment = view.findViewById(R.id.editTextTextConfirmPasswordRegisterFragment);
        registerBtn = view.findViewById(R.id.registerButtonRegisterFragment);
        loginTextViewRegisterFragment = view.findViewById(R.id.loginTextViewRegisterFragment);


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
                    Toast.makeText(view.getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                }

                //Check if the passwords are matching with each other
                else if (!passwordRegisterFragment.getText().toString().equals(editTextTextConfirmPasswordRegisterFragment.getText().toString())) {
                    Toast.makeText(view.getContext(), "Different passwords have been entered", Toast.LENGTH_SHORT).show();
                } else {
                    databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            //Check if ID is not registered before
                            if (snapshot.hasChild(id)) {
                                Toast.makeText(view.getContext(), "ID is already registered", Toast.LENGTH_SHORT).show();

                            } else {
                                //Sending data to firebase realtime database
                                //I am using the id of each user as unique identity of every user
                                //All the other details (full name, email, password, etc..) comes under the user's id
                                databaseReference.child("Users").child(id).child("fullName").setValue(fullName);
                                databaseReference.child("Users").child(id).child("email").setValue(email);
                                databaseReference.child("Users").child(id).child("password").setValue(password);
                                //In this way, I will know how to distinguish between two types of users - user/administrator
                                //Since there are not many administrators in the system (only me) - I manually added the option to
                                // "isAdmin" in my database
                                databaseReference.child("Users").child(id).child("isUser").setValue("1");
                                //Show the user a success message
                                Toast.makeText(view.getContext(), "User registered successfully", Toast.LENGTH_SHORT).show();
                                Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_userFragment);
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
                Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment);
            }
        });
        return view;
    }
}






    // Check if one of the field is empty.
//    public boolean checkField(EditText textField){
//        if(textField.getText().toString().isEmpty()){
//            textField.setError("Please fill in all the details");
//            //Toast.makeText(getView().getContext(), )
//            valid = false;
//        }
//        else{
//            valid = true;
//        }
//        return valid;
//    }
