package com.example.oppawtunity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class admin_main_page extends AppCompatActivity {
    private Button signOut, viewUsers, scanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_admin_main_page);
        FirebaseAuth mAuth;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef;
        mAuth = FirebaseAuth.getInstance();

        signOut = findViewById(R.id.signOutAdmin);
        viewUsers = findViewById(R.id.view_clients);
        scanner = findViewById(R.id.scanner);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            startActivity(new Intent(admin_main_page.this, MainActivity.class));
            finish();
        } else {
            docRef = db.collection("users").document(currentUser.getUid());
            docRef.get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            boolean admin = documentSnapshot.getBoolean("isAdmin");
                            if (!admin) {
                                Intent i = new Intent(admin_main_page.this, adminViewUserPets.class);
                                i.putExtra("uName",documentSnapshot.getString("fullName"));
                                i.putExtra("uPhone",documentSnapshot.getString("phone"));
                                i.putExtra("uUser",documentSnapshot.getId());
                                startActivity(i);
                                finish();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
        }

        scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(admin_main_page.this , qr_scanner.class));
                finish();
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(admin_main_page.this, "User signed out", Toast.LENGTH_SHORT).show();
                mAuth.signOut();
                startActivity(new Intent(admin_main_page.this , MainActivity.class));
                finish();
            }
        });

        viewUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(admin_main_page.this , admin_viewUsers.class));
                finish();
            }
        });
    }
}