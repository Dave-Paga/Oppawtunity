package com.example.oppawtunity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class admin_editPet extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    Button goBack, editPet, uploadBTN;
    EditText ePetName, ePetWeight, ePetBreed, ePetAge;
    ImageView ePetIMG;
    private ProgressBar mProgressBar;
    private Uri mImageUri;
    private StorageReference mStorageRef, photoRef;
    private StorageTask mUploadTask;
    FirebaseStorage mFirebaseStorage;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_pet);
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mFirebaseStorage = FirebaseStorage.getInstance();

        ePetAge = findViewById(R.id.editPetAge);
        ePetBreed = findViewById(R.id.editPetBreed);
        ePetWeight = findViewById(R.id.editPetWeight);
        ePetName = findViewById(R.id.editPetName);

        uploadBTN = findViewById(R.id.editPetUploadIMG);
        editPet = findViewById(R.id.editPetBTN);
        goBack = findViewById(R.id.goBackFromEditPet);

        ePetIMG = findViewById(R.id.editPetIMGView);
        mProgressBar = findViewById(R.id.progress_bar1);

        Bundle extras = getIntent().getExtras();
        String image = extras.getString("petIMG");
        String uUser = extras.getString("uUser");
        String fName = extras.getString("uName");
        String uPhone = extras.getString("uPhone");
        String petName = extras.getString("petName");
        String petWeight = extras.getString("petWeight");
        String petAge = extras.getString("petAge");
        String petBreed = extras.getString("petBreed");
        String petID = extras.getString("petID");
        photoRef = mFirebaseStorage.getReferenceFromUrl(image);

        ePetAge.setText(petAge);
        int number = Integer.valueOf(petWeight.replaceAll("\\D+",""));
        ePetWeight.setText(String.valueOf(number));
        ePetName.setText(petName);
        ePetBreed.setText(petBreed);


        uploadBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        editPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(admin_editPet.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(ePetBreed.getText().toString()) || TextUtils.isEmpty(ePetAge.getText().toString()) ||
                        TextUtils.isEmpty(ePetWeight.getText().toString()) || TextUtils.isEmpty(ePetName.getText().toString())) {
                    Toast.makeText(admin_editPet.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    Pets pet = new Pets();
                    pet.setBreed(ePetBreed.getText().toString());
                    pet.setAge(ePetAge.getText().toString());
                    pet.setWeight(ePetWeight.getText().toString() + " kgs");
                    pet.setName(ePetName.getText().toString());
                    pet.setuUser(uUser);
                    pet.setuName(fName);
                    pet.setuPhone(uPhone);
                    pet.setPetID(petID);
                    pet.setImgURL(image);
                    uploadFile(uUser, pet, fName, uPhone, uUser);
                }
            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(admin_editPet.this, admin_petDetails.class);
                i.putExtra("uName",fName);
                i.putExtra("uPhone",uPhone);
                i.putExtra("uUser",uUser);
                i.putExtra("petName",petName);
                i.putExtra("petAge", petAge);
                i.putExtra("petWeight", petWeight);
                i.putExtra("petBreed", petBreed);
                i.putExtra("petIMG", image);
                i.putExtra("uUser", uUser);
                i.putExtra("petID", petID);
                i.putExtra("uPhone", uPhone);
                i.putExtra("uName", fName);
                startActivity(i);
                finish();
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

            Picasso.with(this).load(mImageUri).into(ePetIMG);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile(String userID, Pets pet, String fName, String uPhone, String uUser) {
        if (mImageUri != null) {
            photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "onSuccess: deleted file");
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

                                    Toast.makeText(admin_editPet.this, "Upload successful", Toast.LENGTH_LONG).show();
                                    Task<Uri> uri = fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String fileLink = uri.toString();
                                            pet.setImgURL(fileLink);
                                            db.collection("users/" + userID + "/pets")
                                                    .document(pet.getPetID())
                                                    .set(pet)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            Toast.makeText(admin_editPet.this, "New Pet Added", Toast.LENGTH_SHORT).show();
                                                            Bundle extras = getIntent().getExtras();
                                                            Intent i = new Intent(admin_editPet.this, adminViewUserPets.class);
                                                            i.putExtra("uName",fName);
                                                            i.putExtra("uPhone",uPhone);
                                                            i.putExtra("uUser", uUser);
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
                                    Toast.makeText(admin_editPet.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                    mProgressBar.setProgress((int) progress);
                                }
                            });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Log.d(TAG, "onFailure: did not delete file");
                }
            });

        } else {
            db.collection("users/" + userID + "/pets")
                    .document(pet.getPetID())
                    .set(pet)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(admin_editPet.this, "New Pet Added", Toast.LENGTH_SHORT).show();
                            Bundle extras = getIntent().getExtras();
                            Intent i = new Intent(admin_editPet.this, adminViewUserPets.class);
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
    }
}