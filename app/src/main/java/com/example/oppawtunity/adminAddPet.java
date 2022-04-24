package com.example.oppawtunity;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class adminAddPet extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

    private Button goBack, addPet, uploadIMG;
    private ProgressBar progress;
    private Uri mImageUri;
    private ImageView mImageView;
    private ProgressBar mProgressBar;
    private StorageReference mStorageRef;
    private StorageTask mUploadTask;
    private EditText petName, petAge, petBreed, petWeight;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_pet);
        goBack = findViewById(R.id.addPetBack);
        addPet = findViewById(R.id.addPet);
        uploadIMG = findViewById(R.id.addPetUploadIMG);
        mImageView = findViewById(R.id.addPetIMGView);
        petName = findViewById(R.id.addPetName);
        petAge = findViewById(R.id.addPetAge);
        petBreed = findViewById(R.id.addPetBreed);
        petWeight = findViewById(R.id.addPetWeight);
        mProgressBar = findViewById(R.id.progress_bar);

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        Bundle extras = getIntent().getExtras();
        String uUser = extras.getString("uUser");
        String uPhone = extras.getString("uPhone");
        String fName = extras.getString("uName");

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(adminAddPet.this, adminViewUserPets.class);
                i.putExtra("uName",fName);
                i.putExtra("uPhone",uPhone);
                i.putExtra("uUser", uUser);
                startActivity(i);
                finish();
            }
        });

        uploadIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();

            }
        });

        addPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(adminAddPet.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(petBreed.getText().toString()) || TextUtils.isEmpty(petAge.getText().toString()) ||
                            TextUtils.isEmpty(petWeight.getText().toString()) || TextUtils.isEmpty(petName.getText().toString())) {
                    Toast.makeText(adminAddPet.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    Pets pet = new Pets();
                    pet.setBreed(petBreed.getText().toString());
                    pet.setAge(petAge.getText().toString());
                    pet.setWeight(petWeight.getText().toString() + " kgs");
                    pet.setName(petName.getText().toString());
                    pet.setuUser(uUser);
                    pet.setuName(fName);
                    pet.setuPhone(uPhone);
                    pet.setPetID(petName.getText().toString());
                    uploadFile(uUser, pet, fName, uPhone, uUser);
                }
            }
        });


    }


    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.with(this).load(mImageUri).into(mImageView);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile(String userID, Pets pet, String fName, String uPhone, String uUser) {
        if (mImageUri != null) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setProgress(0);
                                }
                            }, 500);

                            Toast.makeText(adminAddPet.this, "Upload successful", Toast.LENGTH_LONG).show();
                            Task<Uri> uri = fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String fileLink = uri.toString();
                                    pet.setImgURL(fileLink);
                                    db.collection("users/" + userID + "/pets")
                                            .document(pet.getName())
                                            .set(pet)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(adminAddPet.this, "New Pet Added", Toast.LENGTH_SHORT).show();
                                                    Bundle extras = getIntent().getExtras();
                                                    Intent i = new Intent(adminAddPet.this, adminViewUserPets.class);
                                                    i.putExtra("uName",fName);
                                                    i.putExtra("uPhone",uPhone);
                                                    i.putExtra("uUser",uUser);
                                                    startActivity(i);
                                                    finish();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w(TAG, "Error adding document", e);
                                                }
                                            });

                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(adminAddPet.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }
}

