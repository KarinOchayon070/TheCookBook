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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.List;


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

        //Get the instance of the FirebaseAuth class to be able to perform authentication operations using the Firebase Authentication service
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        //Identify the relevant elements by id
        fullNameRegisterFragment = view.findViewById(R.id.editTextFullNameRegisterFragment);
        emailRegisterFragment = view.findViewById(R.id.editTextTextEmailAddressRegisterFragment);
        editTextTexIDRegisterFragment = view.findViewById(R.id.editTextTexIDRegisterFragment);
        passwordRegisterFragment = view.findViewById(R.id.editTextTextPasswordRegisterFragment);
        editTextTextConfirmPasswordRegisterFragment = view.findViewById(R.id.editTextTextConfirmPasswordRegisterFragment);
        registerBtn = view.findViewById(R.id.registerButtonRegisterFragment);
        loginTextViewRegisterFragment = view.findViewById(R.id.loginTextViewRegisterFragment);

        //Create bundle - this is being used to pass data between Android fragments
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

                //The id must have 9 characters
                else if(id.length() != 9 ){
                    Toast.makeText(view.getContext(), "ID Must Have 9 Characters", Toast.LENGTH_SHORT).show();
                }

                //Check if the passwords are matching with each other
                else if (!passwordRegisterFragment.getText().toString().equals(editTextTextConfirmPasswordRegisterFragment.getText().toString())) {
                    Toast.makeText(view.getContext(), "Different Passwords Have Been Entered", Toast.LENGTH_SHORT).show();
                }

                else {
                    databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            //Check if ID is not registered before
                            if (snapshot.hasChild(id)) {
                                Toast.makeText(view.getContext(), "ID Is Already Registered", Toast.LENGTH_SHORT).show();
                            }

                            else if(emailExist(email)){
                                Toast.makeText(view.getContext(), "Email is already registered", Toast.LENGTH_SHORT).show();
                            }

                            else {
                                //creates a new user in the Firebase Authentication service using the createUserWithEmailAndPassword method from the FirebaseAuth instance
                                //Only if the authentication succeed, had to "Users" (databaseReference) the child in here
                                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                            @Override
                                            public void onSuccess(AuthResult authResult) {
                                                // User has been successfully created
                                                //Sending data to firebase realtime database
                                                //I am using the id of each user as unique identity of every user
                                                //All the other details (full name, email, password, etc..) comes under the user's id
                                                //Here is the bundle I pass the id of the user
                                                bundle.putString("IDUser", id);
                                                databaseReference.child("Users").child(id).child("fullName").setValue(fullName);
                                                databaseReference.child("Users").child(id).child("email").setValue(email);
                                                databaseReference.child("Users").child(id).child("id").setValue(id);
                                                //In this way, I will know how to distinguish between two types of users - user/administrator
                                                //Since there are not many administrators in the system (only me) - I manually added the option to
                                                // "isAdmin" in my database
                                                databaseReference.child("Users").child(id).child("isUser").setValue("1");
                                                //Show the user a success message
                                                Toast.makeText(view.getContext(), "User Registered Successfully", Toast.LENGTH_SHORT).show();
                                                //After the user register successfully - navigates the user to a different screen
                                                Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_selectWhichScreen, bundle);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // There was an error creating the user
                                                Toast.makeText(view.getContext(), "There Aas An Error Creating The User", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(view.getContext(), "Failed To Create An Account", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            //Util function to check id the email is exist in firebaseAuth
            public boolean emailExist(String email){
                final boolean[] flag = {false};
                firebaseAuth.fetchSignInMethodsForEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                            @Override
                            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                if (task.isSuccessful()) {
                                    SignInMethodQueryResult result = task.getResult();
                                    List<String> signInMethods = result.getSignInMethods();
                                    if (!signInMethods.isEmpty()) {
                                        // Email already exists
                                        flag[0] = true;
                                        Toast.makeText(view.getContext(), "Email is already registered", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // Email does not exist, continue with registering a new user
                                    }
                                }
                            }
                        });
                return flag[0];
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
