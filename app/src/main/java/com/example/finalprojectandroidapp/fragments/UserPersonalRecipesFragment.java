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
    EditText editTextFileName;
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

//    ActivityResultLauncher<Intent> activityResultLauncher;

    String url = "";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //The current view is the fragment where the user upload recipes images
        View view = inflater.inflate(R.layout.fragment_user_personal_recipes, container, false);

        //Identify the relevant elements by id
        textViewUserID = view.findViewById(R.id.textViewUserID);
        editTextFileName = view.findViewById(R.id.editTextFileName);
        chooseImage = view.findViewById(R.id.chooseImage);
        uploadImage = view.findViewById(R.id.uploadImage);
        textViewShowUpload = view.findViewById(R.id.textViewShowUpload);
        imageRecipe = view.findViewById(R.id.imageRecipe);
        progressBar = view.findViewById(R.id.progressBar);

        //Create object of DatabaseReference class to access firebase's database
        //The name of the collections will be "Recipes Images"
        mStorageRef = FirebaseStorage.getInstance().getReference("Recipes Images");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Recipes Images");



//        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
//            @Override
//            public void onActivityResult(ActivityResult result) {
//                if(result.getResultCode() == RESULT_OK && result.getData() != null){
//                    Bundle bundle = result.getData().getExtras();
//                    Bitmap bitmap = (Bitmap) bundle.get("data");
//                    imageRecipe.setImageBitmap(bitmap);
//                }
//            }
//        });



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
                                        //When the image is upload successfuly I want to let the user know
                                        textViewUserID.setText(getArguments().getString("IDUser"));
                                        Toast.makeText(view.getContext(), "Upload Successful", Toast.LENGTH_LONG).show();
                                        //Create instance of the "UploadRecipeImage" class and pass it the name (that the user entered and the image url)
//                                        UploadRecipeImage uploadRecipeImage = new UploadRecipeImage(textViewUserID.getText().toString(), editTextFileName.getText().toString().trim(), taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());
//                                        UploadRecipeImage uploadRecipeImage = new UploadRecipeImage(editTextFileName.getText().toString().trim(), mStorageRef.getDownloadUrl().toString());
                                        //Create a new entry to the database with an unique id

                                        fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                // Update the url variable with the actual URL of the uploaded image
                                                url = uri.toString();
                                                // Display the URL in the text view
                                                UploadRecipeImage uploadRecipeImage = new UploadRecipeImage(editTextFileName.getText().toString().trim(), url);
                                                String uploadId = mDatabaseRef.push().getKey();
                                                mDatabaseRef.child(uploadId).setValue(uploadRecipeImage);
                                            }
                                        });



                                        //This two lines add to the "Users" collection in real time database the image each user uploaded acorrding to his id
//                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://thecookbook-fcc12-default-rtdb.firebaseio.com/");
//                                        databaseReference.child("Users").child(textViewUserID.getText().toString()).child(uploadId).setValue(uploadRecipeImage);
                                    }
                                //When the image is not upload successfuly
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
//        activityResultLauncher.launch(intent);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }



    //This method will be called when the user picked an image
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();
            imageRecipe.setImageURI(imageUri);
            Log.d("TAG!!!!!!!!",data.getData().toString());

        }
    }


}