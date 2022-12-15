package com.example.finalprojectandroidapp;

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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Document;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;


public class RegisterFragment extends Fragment {


    Button registerBtn;
    TextView fullNameRegisterFragment, emailRegisterFragment, passwordRegisterFragment, editTextTextConfirmPasswordRegisterFragment;
    boolean valid = true;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;




    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_register, container, false);
        TextView loginTextViewRegisterFragment = view.findViewById(R.id.loginTextViewRegisterFragment);


        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        registerBtn = view.findViewById(R.id.registerButtonRegisterFragment);
        fullNameRegisterFragment = view.findViewById(R.id.editTextFullNameRegisterFragment);
        emailRegisterFragment = view.findViewById(R.id.editTextTextEmailAddressRegisterFragment);
        passwordRegisterFragment = view.findViewById(R.id.editTextTextPasswordRegisterFragment);
        editTextTextConfirmPasswordRegisterFragment = view.findViewById(R.id.editTextTextConfirmPasswordRegisterFragment);


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                checkField((EditText) fullNameRegisterFragment);
                checkField((EditText) emailRegisterFragment);
                checkField((EditText) passwordRegisterFragment);
                checkField((EditText) editTextTextConfirmPasswordRegisterFragment);

                if((passwordRegisterFragment.getText() != editTextTextConfirmPasswordRegisterFragment.getText())
                        &&
                        ((passwordRegisterFragment.getText().toString() != null) && editTextTextConfirmPasswordRegisterFragment.getText().toString() != null)){
                    Toast.makeText(view.getContext(), "Different passwords have been entered", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (valid) {
                    fAuth.createUserWithEmailAndPassword(emailRegisterFragment.getText().toString(), passwordRegisterFragment.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            FirebaseUser user = fAuth.getCurrentUser();
                            Toast.makeText(view.getContext(), "Account Created Successfully", Toast.LENGTH_SHORT).show();
                            DocumentReference df = fStore.collection("Users").document(user.getUid());
                            Map<String, Object> userInfo = new HashMap<>();
                            userInfo.put("FullName", fullNameRegisterFragment.getText().toString());
                            userInfo.put("UserEmail", emailRegisterFragment.getText().toString());
                            userInfo.put("Password", passwordRegisterFragment.getText().toString());
                            userInfo.put("isUser", "1");
                            df.set(userInfo);
                            //                    finish();
                            Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_userFragment);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(view.getContext(), "Failed To Create An Account", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        //Go back to login fragment.
        loginTextViewRegisterFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment);
            }
        });
        return view;
    }


    // Check if one of the field is empty.
    public boolean checkField(EditText textField){
        if(textField.getText().toString().isEmpty()){
            textField.setError("Please fill in all the details");
            valid = false;
        }
        else{
            valid = true;
        }
        return valid;
    }
}