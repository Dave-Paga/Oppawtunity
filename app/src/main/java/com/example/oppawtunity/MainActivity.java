package com.example.oppawtunity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {

    private Button register, login;
    private EditText inputPass, inputUser;
    FirebaseAuth userAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference docRef;

    public void checkLogin() {
        DocumentReference docRef;
        FirebaseAuth mAuth;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null){
            docRef = db.collection("users").document(currentUser.getUid());
            docRef.get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot d) {
                            Users user = new Users();
                            boolean admin = d.getBoolean("isAdmin");
                            if (!admin) {
                                Intent i = new Intent(MainActivity.this, adminViewUserPets.class);
                                i.putExtra("uName",d.getString("fullName"));
                                i.putExtra("uPhone",d.getString("phone"));
                                i.putExtra("uUser",d.getId());
                                startActivity(i);
                            } else {
                                startActivity(new Intent(MainActivity.this, admin_main_page.class));
                            }
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_main);

        register = findViewById(R.id.registerBtn);
        login = findViewById(R.id.loginBtn);
        inputPass = findViewById(R.id.inputPass);
        inputUser = findViewById(R.id.inputUser);

        FirebaseAuth mAuth;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        checkLogin();


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, registrationpage.class));
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getPass = inputPass.getText().toString();
                String getUser = inputUser.getText().toString();

                if (TextUtils.isEmpty(getPass) || TextUtils.isEmpty(getUser)) {
                    Toast.makeText(MainActivity.this, "Please fill both username and password", Toast.LENGTH_SHORT).show();
                } else {
                    String email = getUser + "@email.com";
                    mAuth.signInWithEmailAndPassword(email, getPass)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                    checkLogin();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MainActivity.this, "Invalid User or Password", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

    }
}