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
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class registrationpage extends AppCompatActivity {

    EditText getUser, password, phone, fullName;
    private Button register, cancel;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_registrationpage);
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();

        getUser = findViewById(R.id.getUsername);
        password = findViewById(R.id.getPassword);
        phone = findViewById(R.id.getPhone);
        fullName = findViewById(R.id.getFullName);
        register = findViewById(R.id.registerUser);
        cancel = findViewById(R.id.goBack);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(registrationpage.this , MainActivity.class));
                finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String strUser = getUser.getText().toString();
                String strPass = password.getText().toString();
                String phoneVal= phone.getText().toString();
                String name = fullName.getText().toString();

                if (TextUtils.isEmpty(strUser) || TextUtils.isEmpty(strPass) || TextUtils.isEmpty(phoneVal) || TextUtils.isEmpty(name)) {
                    Toast.makeText(registrationpage.this, "Please complete all fields", Toast.LENGTH_SHORT).show();
                } else {
                    int intPhone = Integer.parseInt(phoneVal);
                    String email = strUser + "@email.com";
                    Map<String, Object> user = new HashMap<>();
                    user.put("username", strUser);
                    user.put("fullName", name);
                    user.put("phone", phoneVal);
                    user.put("password", strPass);
                    user.put("isAdmin", false);

                    // CHECK FOR DUPLICATES IN "USER" COLLECTION
                    CollectionReference reference=db.collection("users");
                    Query q1=reference.whereEqualTo("username",strUser);
                    q1.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            boolean isExisting = false;
                            for (DocumentSnapshot ds : queryDocumentSnapshots) {
                                String rname;
                                rname = ds.getString("username");
                                if (rname.equals(strUser)) {
                                    isExisting = true;
                                }
                            }

                            if (!isExisting) {
                                mAuth.createUserWithEmailAndPassword(email, strPass).addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        String userID = mAuth.getCurrentUser().getUid();
                                        db.collection("users")
                                                .document(userID)
                                                .set(user)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Toast.makeText(registrationpage.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                                        mAuth.signOut();
                                                        startActivity(new Intent(registrationpage.this , MainActivity.class));
                                                        finish();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w(TAG, "Error adding document", e);
                                                    }
                                                });
                                    } else {
                                        Toast.makeText(registrationpage.this, "Error on authentication", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Toast.makeText(registrationpage.this, "Username already exist", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }

        });


    }
}