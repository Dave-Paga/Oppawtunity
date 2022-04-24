package com.example.oppawtunity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class adminViewUserPets extends AppCompatActivity {
    Button goBack, addPet, signOut;
    TextView name, phone;
    ListView viewPets;
    ArrayList<Pets> petList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference docRef, getAdmin;
    FirebaseAuth mAuth;
    Boolean admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_user_pets);
        String fName, uPhone, userName;

        goBack = findViewById(R.id.goBackFromViewUserPet);
        viewPets = findViewById(R.id.viewUserPetList);
        name = findViewById(R.id.viewPetUser);
        phone = findViewById(R.id.viewUserPetPhone);
        addPet = findViewById(R.id.adminAddPet);
        signOut = findViewById(R.id.signOutUser);

        Bundle extras = getIntent().getExtras();

        fName = extras.getString("uName");
        uPhone = extras.getString("uPhone");
        userName = extras.getString("uUser");
        name.setText(fName);
        phone.setText(uPhone);
        petList = new ArrayList<Pets>();

        db = FirebaseFirestore.getInstance();
        loadDataInListview(userName);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        getAdmin = db.collection("users").document(currentUser.getUid());
        getAdmin.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        admin = documentSnapshot.getBoolean("isAdmin");
                        if(!admin){
                            addPet.setVisibility(View.GONE);
                            goBack.setVisibility(View.GONE);
                        } else {
                            signOut.setVisibility(View.GONE);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });

        addPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(adminViewUserPets.this, adminAddPet.class);
                i.putExtra("uName",fName);
                i.putExtra("uPhone",uPhone);
                i.putExtra("uUser",userName);
                startActivity(i);
                finish();
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(adminViewUserPets.this, "User signed out", Toast.LENGTH_SHORT).show();
                mAuth.signOut();
                startActivity(new Intent(adminViewUserPets.this , MainActivity.class));
                finish();
            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(adminViewUserPets.this, admin_viewUsers.class));
                finish();
            }
        });
    }

    private void loadDataInListview(String userName) {
        db.collection("users").document(userName).collection("pets").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                Pets pet = new Pets();
                                pet.setName(d.getString("name"));
                                pet.setWeight(d.getString("weight"));
                                pet.setAge(d.getString("age"));
                                pet.setImgURL(d.getString("imgURL"));
                                pet.setBreed(d.getString("breed"));
                                pet.setuPhone(d.getString("uPhone"));
                                pet.setuName(d.getString("uName"));
                                pet.setuUser(userName);
                                pet.setPetID(d.getId().toString());
                                petList.add(pet);
                            }
                            PetAdapter adapter = new PetAdapter(adminViewUserPets.this, petList);

                            viewPets.setAdapter(adapter);
                        } else {
                            Toast.makeText(adminViewUserPets.this, "No data found in Database", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(adminViewUserPets.this, "User has no pet record", Toast.LENGTH_SHORT).show();
            }
        });
    }
}