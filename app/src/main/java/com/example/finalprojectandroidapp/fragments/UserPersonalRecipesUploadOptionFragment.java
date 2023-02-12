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
import com.example.finalprojectandroidapp.UploadRecipe;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import java.util.HashMap;
import java.util.Map;

// This link helped me a lot to upload image to fireBase - https://www.youtube.com/watch?v=MfCiiTEwt3g&t=259s&ab_channel=CodinginFlow

// This code allows the user to upload a recipe to Firebase Database (and Storage for the image recipe).
// The user can select an image using the "chooseImage" button and then upload the image along with the recipe name, summary, and instructions using the "uploadImage" button.
// The uploaded image will be stored in Firebase Storage under the folder "Recipes Images" and the recipe information will be stored in the Firebase Realtime Database under the same folder.
// The progress of the upload task is displayed using a ProgressBar. Error handling is implemented to display Toast messages in case of failure during the upload process.

public class UserPersonalRecipesUploadOptionFragment extends Fragment {

    //I will use this later to identify the image request. Any positive number will be fine in here
    private static final int PICK_IMAGE_REQUEST = 1;

    //Initial
    EditText editTextRecipeName, editTextRecipeSummary,  editTextRecipeInstructions;
    Button chooseImage, uploadImage;
    TextView textViewShowUpload;
    ImageView imageRecipe;
    ProgressBar progressBar;
    //This is the uri which will point to the image so I can show it in the imageView and upload it to firebase storage
    Uri imageUri;
    //Initial the reference to database
    private StorageReference mStorageRef;     //This one is for the storage database  (for the images)
    private DatabaseReference mDatabaseRef;  //This one is for the real time database
    private StorageTask mUploadTask;
    //This will use me to the url of the image
    String recipeImage = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //The current view is the fragment where the user upload recipes images
        View view = inflater.inflate(R.layout.fragment_user_personal_recipes_upload_option, container, false);

        //Bundle - this is being used to pass data between Android fragments
        String IDUser = getArguments().getString("IDUser");
        Bundle bundle = new Bundle();
        bundle.putString("IDUser",IDUser);

        //Identify the relevant elements by id
        chooseImage = view.findViewById(R.id.chooseImage);
        uploadImage = view.findViewById(R.id.uploadImage);
        textViewShowUpload = view.findViewById(R.id.textViewShowUpload);
        imageRecipe = view.findViewById(R.id.imageRecipe);
        progressBar = view.findViewById(R.id.progressBar);
        editTextRecipeName =  view.findViewById(R.id.editTextRecipeName);
        editTextRecipeSummary =  view.findViewById(R.id.editTextRecipeSummary);
        editTextRecipeInstructions =  view.findViewById(R.id.editTextRecipeInstructions);

        //Create object of DatabaseReference class to access firebase's database
        //The name of the folders will be "Recipes Images"
        mStorageRef = FirebaseStorage.getInstance().getReference("Recipes Images");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Recipes Images");

        //When the user will press the "choose image" the "openImageChooser" method is called
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

                //Get the relevant field from the user and make sure they are not empty
                String recipeName = editTextRecipeName.getText().toString();
                String recipeSummary = editTextRecipeSummary.getText().toString();
                String recipeInstructions = editTextRecipeInstructions.getText().toString();

                if (recipeName.isEmpty() || recipeSummary.isEmpty() || recipeInstructions.isEmpty()) {
                    Toast.makeText(view.getContext(), "Please Fill All Fields", Toast.LENGTH_SHORT).show();
                }

                else{
                    //If the user did pick an image - I want to upload it
                    if (imageUri != null) {
                        //I had to create a unique name for each of the images (otherwise it will override)
                        //The unique name of the image is the time in milliseconds (because it changing so fast so it's unique
                        //It will look like this - Recipes Images/5989555959.png
                        StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
                        Log.d("TagUrlImage", String.valueOf(fileReference.getDownloadUrl()));
                        //Here I passed the image I want to add to the database
                        mUploadTask = fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    //When the image is upload successfully
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        //When the image is uploaded I want to set the progressbar to zero
                                        //Here I made a short delay that the user could actually see the progressbar
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                progressBar.setProgress(0);
                                                imageRecipe.setImageResource(R.drawable.orange_border);
                                            }
                                        }, 5000);
                                        //When the image is upload successfully I want to let the user know
                                        //The id of the recipe
                                        String uploadId = mDatabaseRef.push().getKey();
                                        //This favorite map is for the "favoriteBy" map in the firebase
                                        Map<String, Boolean> favorite = new HashMap<>();
                                        //Create new object of the "UploadRecipe" and upload it to realtime database
                                        UploadRecipe uploadRecipeImage = new UploadRecipe(IDUser, editTextRecipeName.getText().toString(), editTextRecipeSummary.getText().toString(),  editTextRecipeInstructions.getText().toString(), recipeImage, favorite);
                                        //Now the key of each recipe is the recipe key I declare above, the children are the userId, recipe name, atc...
                                        mDatabaseRef.child(uploadId).setValue(uploadRecipeImage);
                                        //Let the user know the recipe uploaded
                                        Toast.makeText(view.getContext(), "Upload Successful", Toast.LENGTH_LONG).show();
                                        // Reset the recipeName, recipeSummary, and recipeInstructions to empty strings
                                        editTextRecipeName.setText("");
                                        editTextRecipeSummary.setText("");
                                        editTextRecipeInstructions.setText("");
                                    }
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
        //When the user will press the "show upload" navigate back to all users recipes
        textViewShowUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_userPersonalRecipes_to_userUploadRecipesFragment, bundle);
            }
        });
        return view;
    }

    //This function (from the internet) return the extension of the image the user picked
    //For example - for a jepg image, the function will return jpg
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContext().getContentResolver();
        //A MIME type is a string sent along with a file indicating the type of the file
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    //This function will be called when the user press on the button "choose image"
    private void openImageChooser(){
        Intent intent = new Intent();
        //Only see images
        intent.setType("image/*");
        //The intent's action is set to Intent.ACTION_GET_CONTENT which specifies that the activity should return an image
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //In this case, the startActivityForResult method is used to start an activity to select an image, and the request code
        //PICK_IMAGE_REQUEST is used to identify the result of that activity when it returns. When the user selects an image,
        //the result is returned to the calling activity, and the request code is used to match the result to the appropriate
        //activity that started it.
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    //This method will be called when the user picked an image
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            //The imageUri var contain the image uri the user picked (uri will point the image position)
            imageUri = data.getData();
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
            Toast.makeText(getContext(), "Please Wait For The Image To Load", Toast.LENGTH_SHORT).show();
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