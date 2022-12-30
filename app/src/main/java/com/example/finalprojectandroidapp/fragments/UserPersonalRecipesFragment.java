package com.example.finalprojectandroidapp.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.finalprojectandroidapp.R;
import com.example.finalprojectandroidapp.UploadRecipeImage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;


public class UserPersonalRecipesFragment extends Fragment {

    //I will use this later to identify the image request
    private static final int PICK_IMAGE_REQUEST = 1;

    //Initial the var from the layout
//    EditText editTextFileName;
    EditText  editTextRecipeName, editTextRecipeSummary, editTextRecipeIngredients, editTextRecipeInstructions;
    Button chooseImage, uploadImage;
    TextView textViewShowUpload, textViewUserID;
    ImageView imageRecipe;
    ProgressBar progressBar;

    //This is the uri which will point to the image so I can show it in the imageView and upload it to firebase
    Uri imageUri;

    //Initial the reference to database
    private StorageReference mStorageRef;    //This one is for the storage database
    private DatabaseReference mDatabaseRef;  //This one is for the real time database

    private StorageTask mUploadTask;

    //This will use me to the url of the image
    String recipeImage = "";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //The current view is the fragment where the user upload recipes images
        View view = inflater.inflate(R.layout.fragment_user_personal_recipes, container, false);


        chooseImage = view.findViewById(R.id.chooseImage);
        uploadImage = view.findViewById(R.id.uploadImage);
        textViewShowUpload = view.findViewById(R.id.textViewShowUpload);
        imageRecipe = view.findViewById(R.id.imageRecipe);
        progressBar = view.findViewById(R.id.progressBar);


        editTextRecipeName =  view.findViewById(R.id.editTextRecipeName);
        editTextRecipeSummary =  view.findViewById(R.id.editTextRecipeSummary);
        editTextRecipeIngredients =  view.findViewById(R.id.editTextRecipeIngredients);
        editTextRecipeInstructions =  view.findViewById(R.id.editTextRecipeInstructions);



        //Create object of DatabaseReference class to access firebase's database
        //The name of the collections will be "Recipes Images"
        mStorageRef = FirebaseStorage.getInstance().getReference("Recipes Images");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Recipes Images");


        //When the user will press the "choose image"...
        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImageChooser();
            }
        });

        //When the user will press the "upload image"
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //When the user picked an images (there for the mUploadTask is not null) and the image upload progress is still on
                //I will let the user know that the upload is still in progress
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(view.getContext(), "Upload In Progress", Toast.LENGTH_SHORT).show();
                }

                final String recipeName = editTextRecipeName.getText().toString();
                final String recipeSummary = editTextRecipeSummary.getText().toString();
                final String recipeIngredients = editTextRecipeIngredients.getText().toString();
                final String recipeInstructions = editTextRecipeInstructions.getText().toString();



                if (recipeName.isEmpty() || recipeSummary.isEmpty() || recipeIngredients.isEmpty() || recipeInstructions.isEmpty()) {
                    Toast.makeText(view.getContext(), "Please Fill All Fields", Toast.LENGTH_SHORT).show();
                }

                else{
                    //If the user did pick an image - I want to upload it
                    if (imageUri != null) {
                        //I had to create a unique name for each of the images (otherwise it will override)
                        //The unique name of the image is the time in milliseconds (because it changing so fast so it's unique
                        //It will look like this - Recipes Images/5989555959.png
                        StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
                        Log.d("TagTest", String.valueOf(fileReference.getDownloadUrl()));
//                        url = fileReference.getStorageReferenceUri().getHttpUri().toString();
                        //Here I passed the image I want to add to the database

//


                        mUploadTask = fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    //When the image is upload successfuly
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                                        //When the image is uploaded I want to set the progressbar
                                        //Here I made a short delay that the user could actually see the progressbar
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                progressBar.setProgress(0);
                                                imageRecipe.setImageResource(R.drawable.whitebackground);
                                            }
                                        }, 500);
                                        //When the image is upload successfully I want to let the user know
//                                        textViewUserID.setText(getArguments().getString("IDUser"));

                                        String uploadId = mDatabaseRef.push().getKey();

                                        String IDUser = getArguments().getString("IDUser");
                                        Log.d("tagiduser", IDUser);


                                        UploadRecipeImage uploadRecipeImage = new UploadRecipeImage(IDUser, editTextRecipeName.getText().toString(), editTextRecipeSummary.getText().toString(), editTextRecipeIngredients.getText().toString(), editTextRecipeInstructions.getText().toString(), recipeImage);
                                        mDatabaseRef.child(uploadId).setValue(uploadRecipeImage);
                                        Toast.makeText(view.getContext(), "Upload Successful", Toast.LENGTH_LONG).show();


                                        //This two lines add to the "Users" collection in real time database the image each user uploaded acorrding to his id
                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://thecookbook-fcc12-default-rtdb.firebaseio.com/");
                                        databaseReference.child("Users").child(IDUser).child(uploadId).setValue(uploadRecipeImage);
                                    }
                                //When the image is not upload successfully
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        //When the image didn't uploaded to the database from some reason - I want the user will know
                                        Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                //When the image is still on progress
                                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                        //This is gives me the progress - because we get the transfer bytes of the image divide by the total bytes
                                        double progress = (100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                                        //Than I passed it to the progressbar element so that the user can actually see the progress
                                        progressBar.setProgress((int) progress);
                                    }
                                });
                    } else {
                        //This is the case where the user didn't pick an image
                        Toast.makeText(view.getContext(), "No Image Selected", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //When the user will press the "show upload"...
        textViewShowUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_userPersonalRecipes_to_userUploadRecipesFragment);
            }
        });
        return view;
    }

    //This function (from the internet) return the extension of the image the user picked
    //For example - for a jepg image, the function will return jpg
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    //This function will be called when the user press on the button "choose image"
    private void openImageChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }



    //This method will be called when the user picked an image
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));

            fileReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get the URL of the uploaded image
                            taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // Update the recipeImage variable with the actual URL of the uploaded image
                                    recipeImage = uri.toString();
                                    imageRecipe.setImageURI(imageUri);
                                    Log.d("TestOnActivityResult",recipeImage);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle failure
                        }
                    });
        }
    }
}