package com.example.oppawtunity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class admin_viewUsers extends AppCompatActivity {
    FirebaseAuth userAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference docRef;
    ListView viewUsers;
    Button back;
    ArrayList<Users> usersList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_users);

        back = findViewById(R.id.viewUsersBack);
        viewUsers = findViewById(R.id.viewUsers);
        usersList = new ArrayList<Users>();

        db = FirebaseFirestore.getInstance();
        loadDataInListview();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(admin_viewUsers.this, admin_main_page.class));
                finish();
            }
        });
    }


    private void loadDataInListview() {
        db.collection("users").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                if(d.getBoolean("isAdmin") == false) {
                                    Users user = new Users();
                                    user.setFullName(d.getString("fullName"));
                                    user.setPhoneNo(d.getString("phone"));
                                    user.setUsername(d.getId());
                                    usersList.add(user);
                                }
                            }
                            UserAdapter adapter = new UserAdapter(admin_viewUsers.this, usersList);

                            viewUsers.setAdapter(adapter);
                            setListViewHeightBasedOnChildren(viewUsers);

                        } else {
                            Toast.makeText(admin_viewUsers.this, "No data found in Database", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(admin_viewUsers.this, "Fail to load data..", Toast.LENGTH_SHORT).show();
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
}