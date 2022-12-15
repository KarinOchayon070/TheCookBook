package com.example.finalprojectandroidapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.zip.Inflater;


public class LoginFragment extends Fragment {

    Button loginBtn;
    TextView emailLoginFragment, passwordLoginFragment;
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
    public LoginFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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

        View view = inflater.inflate(R.layout.fragment_login, container, false);
        TextView registerTextViewLoginFragment = view.findViewById(R.id.registerTextViewLoginFragment);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        loginBtn = view.findViewById(R.id.loginButtonLoginFragment);
        emailLoginFragment = view.findViewById(R.id.editTextTextEmailAddressLoginFragment);
        passwordLoginFragment = view.findViewById(R.id.editTextTextPasswordLoginFragment);



        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkField((EditText) emailLoginFragment);
                checkField((EditText) passwordLoginFragment);

                if(valid){
                    fAuth.signInWithEmailAndPassword(emailLoginFragment.getText().toString(), passwordLoginFragment.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
//                            checkUserAccessLevel(authResult.getUser().getUid());
                            String uid = authResult.getUser().getUid();
                            DocumentReference df = fStore.collection("Users").document(uid);
                            df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    Log.d("TAG", "onSuccess: " + documentSnapshot.getData());

                                    if(documentSnapshot.getString("isAdmin") != null){
                                        //User is admin
                                        Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_adminFragment);
                                    }
                                    if(documentSnapshot.getString("isUser") != null){
                                        Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_userFragment);
                                    }
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(view.getContext(), "Loggin Failed", Toast.LENGTH_SHORT).show();
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

//    @Override
//    protected void onStart(){
//        super.onStart();
//        if(FirebaseAuth.getInstance().getCurrentUser() != null){
//
//        }
//    }
}