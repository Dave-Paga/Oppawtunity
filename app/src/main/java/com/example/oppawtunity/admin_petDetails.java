package com.example.oppawtunity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class admin_petDetails extends AppCompatActivity {
    TextView petAge, petWeight, petBreed, petName;
    Button addVac, addVisit, addSurg, addMed, goBack, deletePet, editPet, generateQR;
    ImageView dPetIMG;
    StorageReference photoRef;
    FirebaseStorage mFirebaseStorage;
    FirebaseFirestore db;
    ListView vaccines, surgeries, meds, visit;
    FirebaseAuth mAuth;
    DocumentReference docRef;
    Boolean admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_pet_details);
        mFirebaseStorage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();

        petAge = findViewById(R.id.dPetAge);
        petName = findViewById(R.id.dPetName);
        petWeight = findViewById(R.id.dPetWeight);
        petBreed = findViewById(R.id.dPetBreed);
        dPetIMG = findViewById(R.id.dPetIMG);

        addVac = findViewById(R.id.addVaccination);
        addSurg = findViewById(R.id.addSurgeries);
        addVisit = findViewById(R.id.addVisitations);
        addMed = findViewById(R.id.addMedications);
        goBack = findViewById(R.id.goBackFromPetDetails);
        deletePet = findViewById(R.id.deletePet);
        editPet = findViewById(R.id.editPet);
        generateQR = findViewById(R.id.generateQR);

        vaccines = findViewById(R.id.dVaccination);
        surgeries = findViewById(R.id.dSurgeries);
        meds = findViewById(R.id.dMedications);
        visit = findViewById(R.id.dVisitations);

        Bundle extras = getIntent().getExtras();

        String pAge = extras.getString("petAge");
        String pName = extras.getString("petName");
        String pWeight = extras.getString("petWeight");
        String pBreed = extras.getString("petBreed");
        String pIMG = extras.getString("petIMG");
        String fName = extras.getString("uName");
        String uPhone = extras.getString("uPhone");
        String uUser = extras.getString("uUser");
        String pID = extras.getString("petID");


        photoRef = mFirebaseStorage.getReferenceFromUrl(pIMG);
        petAge.setText(pAge);
        petName.setText(pName);
        petWeight.setText(pWeight);
        petBreed.setText(pBreed);
        Picasso.with(this).load(pIMG).into(dPetIMG);

        // 1 = vaccines, 2 = surgeries, 3 = meds, 4 = visitations

        loadDataInListview(uUser,pID,vaccines, "1");
        loadDataInListview(uUser,pID,surgeries, "2");
        loadDataInListview(uUser,pID,meds,"3");
        loadDataInListview(uUser,pID,visit,"4");

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        docRef = db.collection("users").document(currentUser.getUid());
        Boolean admin;
        docRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Boolean admin = documentSnapshot.getBoolean("isAdmin");
                        String getID = documentSnapshot.getString("fullName");
                        if (admin == false) {
                            addVac.setVisibility(View.GONE);
                            addMed.setVisibility(View.GONE);
                            addSurg.setVisibility(View.GONE);
                            addVisit.setVisibility(View.GONE);
                            deletePet.setVisibility(View.GONE);
                        } else {
                            generateQR.setVisibility(View.GONE);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });


        addVac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(admin_petDetails.this, admin_editHistory.class);
                i.putExtra("petName",pName);
                i.putExtra("petAge", pAge);
                i.putExtra("petWeight", pWeight);
                i.putExtra("petBreed", pBreed);
                i.putExtra("petIMG", pIMG);
                i.putExtra("uUser", uUser);
                i.putExtra("petID", pID);
                i.putExtra("uPhone", uPhone);
                i.putExtra("uName", fName);
                i.putExtra("type", "1");
                i.putExtra("editFlag", "0");
                startActivity(i);
            }
        });

        addSurg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(admin_petDetails.this, admin_editHistory.class);
                i.putExtra("petName",pName);
                i.putExtra("petAge", pAge);
                i.putExtra("petWeight", pWeight);
                i.putExtra("petBreed", pBreed);
                i.putExtra("petIMG", pIMG);
                i.putExtra("uUser", uUser);
                i.putExtra("petID", pID);
                i.putExtra("uPhone", uPhone);
                i.putExtra("uName", fName);
                i.putExtra("type", "2");
                i.putExtra("editFlag", "0");
                startActivity(i);
            }
        });

        addMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(admin_petDetails.this, admin_editHistory.class);
                i.putExtra("petName",pName);
                i.putExtra("petAge", pAge);
                i.putExtra("petWeight", pWeight);
                i.putExtra("petBreed", pBreed);
                i.putExtra("petIMG", pIMG);
                i.putExtra("uUser", uUser);
                i.putExtra("petID", pID);
                i.putExtra("uPhone", uPhone);
                i.putExtra("uName", fName);
                i.putExtra("type", "3");
                i.putExtra("editFlag", "0");
                startActivity(i);
            }
        });

        addVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(admin_petDetails.this, admin_editHistory.class);
                i.putExtra("petName",pName);
                i.putExtra("petAge", pAge);
                i.putExtra("petWeight", pWeight);
                i.putExtra("petBreed", pBreed);
                i.putExtra("petIMG", pIMG);
                i.putExtra("uUser", uUser);
                i.putExtra("petID", pID);
                i.putExtra("uPhone", uPhone);
                i.putExtra("uName", fName);
                i.putExtra("type", "4");
                i.putExtra("editFlag", "0");
                startActivity(i);
            }
        });



        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(admin_petDetails.this, adminViewUserPets.class);
                i.putExtra("uName",fName);
                i.putExtra("uPhone",uPhone);
                i.putExtra("uUser",uUser);
                startActivity(i);
                finish();
            }
        });

        editPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(admin_petDetails.this, admin_editPet.class);
                i.putExtra("petName",pName);
                i.putExtra("petAge", pAge);
                i.putExtra("petWeight", pWeight);
                i.putExtra("petBreed", pBreed);
                i.putExtra("petIMG", pIMG);
                i.putExtra("uUser", uUser);
                i.putExtra("petID", pID);
                i.putExtra("uPhone", uPhone);
                i.putExtra("uName", fName);
                startActivity(i);
            }
        });

        generateQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(admin_petDetails.this, qr_generator.class);
                i.putExtra("petName",pName);
                i.putExtra("petAge", pAge);
                i.putExtra("petWeight", pWeight);
                i.putExtra("petBreed", pBreed);
                i.putExtra("petIMG", pIMG);
                i.putExtra("uUser", uUser);
                i.putExtra("petID", pID);
                i.putExtra("uPhone", uPhone);
                i.putExtra("uName", fName);
                startActivity(i);
            }
        });

        deletePet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("users/" + uUser + "/pets")
                        .document(pID)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "Document successfully deleted");
                                photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "onSuccess: deleted file");
                                        Intent i = new Intent(admin_petDetails.this, adminViewUserPets.class);
                                        i.putExtra("uName",fName);
                                        i.putExtra("uPhone",uPhone);
                                        i.putExtra("uUser",uUser);
                                        startActivity(i);
                                        finish();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        Log.d(TAG, "onFailure: did not delete file");
                                    }
                                });

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error deleting document", e);
                            }
                        });
            }
        });
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    private void loadDataInListview(String userName, String petID, ListView list1, String histType) {
        Bundle extras = getIntent().getExtras();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        docRef = db.collection("users").document(currentUser.getUid());
        docRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        admin = documentSnapshot.getBoolean("isAdmin");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });


        String pAge = extras.getString("petAge");
        String pName = extras.getString("petName");
        String pWeight = extras.getString("petWeight");
        String pBreed = extras.getString("petBreed");
        String pIMG = extras.getString("petIMG");
        String fName = extras.getString("uName");
        String uPhone = extras.getString("uPhone");
        String uUser = extras.getString("uUser");
        String pID = extras.getString("petID");

        ArrayList<History> historyList;
        historyList = new ArrayList<History>();


//        CollectionReference reference=db.collection("users/" + userName + "/pets/" + petID + "/history");
//        Query q1=reference.whereEqualTo("type",histType);
//        q1.get()
        db.collection("users/" + userName + "/pets/" + petID + "/history")
                .whereEqualTo("type", histType)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                History his = new History();
                                his.setName(d.getString("name"));
                                his.setDate1(d.getString("date1"));
                                his.setDate2(d.getString("date2"));
                                his.setExtra(d.getString("extra"));
                                his.setType(d.getString("type"));
                                his.sethID(d.getString("hID"));
                                his.setuUser(uUser);
                                his.setuName(fName);
                                his.setuPhone(uPhone);
                                his.setpName(pName);
                                his.setpAge(pAge);
                                his.setpBreed(pBreed);
                                his.setpID(pID);
                                his.setpIMG(pIMG);
                                his.setpWeight(pWeight);
                                his.setAdmin(admin);
                                historyList.add(his);
                            }
                            HistoryAdapter adapter = new HistoryAdapter(admin_petDetails.this,historyList);
                            list1.setAdapter(adapter);
                            setListViewHeightBasedOnChildren(list1);
                        } else {
                            Log.d(TAG, "No HistType= " + histType);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error getting document", e);
            }
        });
    }
}