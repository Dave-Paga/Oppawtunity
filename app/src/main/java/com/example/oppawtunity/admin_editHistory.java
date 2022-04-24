package com.example.oppawtunity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class admin_editHistory extends AppCompatActivity {
    final Calendar myCalendar = Calendar.getInstance();
    EditText hName, hDate1, hDate2, hExtra, hType, dynamicDate;
    TextView hTextName, hTextDate1, hTextDate2, hTextExtra;
    String image, uUser, fName, uPhone, petName, petWeight, petAge, petBreed, petID, ehName, ehDate1, ehDate2, ehExtra, ehType, ehID, flag;
    Button goBack, submit, delete;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_history);

        hTextName = findViewById(R.id.PHName);
        hTextDate1 = findViewById(R.id.PHDate1);
        hTextDate2 = findViewById(R.id.PHDate2);
        hTextExtra = findViewById(R.id.PHExtra);

        hName = findViewById(R.id.editPHName);
        hDate1 = findViewById(R.id.editPHDate1);
        hDate2 = findViewById(R.id.editPHDate2);
        hExtra = findViewById(R.id.editPHExtra);
        hType = findViewById(R.id.editPHType);

        goBack = findViewById(R.id.goBackFromHistory);
        submit = findViewById(R.id.submitPH);
        delete = findViewById(R.id.deleteHistory);

        Bundle extras = getIntent().getExtras();
        image = extras.getString("petIMG");
        uUser = extras.getString("uUser");
        fName = extras.getString("uName");
        uPhone = extras.getString("uPhone");
        petName = extras.getString("petName");
        petWeight = extras.getString("petWeight");
        petAge = extras.getString("petAge");
        petBreed = extras.getString("petBreed");
        petID = extras.getString("petID");

        ehType = extras.getString("type");
        flag = extras.getString("editFlag");

        ehName = extras.getString("name");
        ehDate1 = extras.getString("date1");
        ehDate2 = extras.getString("date2");
        ehExtra = extras.getString("extra");
        ehID = extras.getString("historyID");

        hName.setText(ehName);
        hDate1.setText(ehDate1);
        hDate2.setText(ehDate2);
        hExtra.setText(ehExtra);
        hType.setText(ehType);

        String nText = " ", d1Text = " ", d2Text = " ", eText = " ";
        int v1 = View.VISIBLE;
        int v2 = View.VISIBLE;
        switch(ehType){
            case "1":
                nText = "Vaccination";
                d1Text= "Date";
                hDate2.setText(" ");
                hExtra.setText(" ");
                v1 = View.INVISIBLE;
                v2 = View.INVISIBLE;
                break;
            case "2":
                nText = "Surgeries";
                d1Text = "Date";
                hDate2.setText(" ");
                hExtra.setText(" ");
                v1 = View.INVISIBLE;
                v2 = View.INVISIBLE;
                break;
            case "3":
                nText = "Medication";
                d1Text = "Start Date";
                d2Text = "End Date";
                hExtra.setText(" ");
                v2 = View.INVISIBLE;
                break;
            case "4":
                nText = "Clinic Location";
                d1Text = "Date";
                eText = "Nature of Visitation";
                hDate2.setText(" ");
                v1 = View.INVISIBLE;
                break;
        }

        hTextName.setText(nText);
        hTextDate1.setText(d1Text);
        hTextDate2.setText(d2Text);
        hTextExtra.setText(eText);
        hDate2.setVisibility(v1);
        hExtra.setVisibility(v2);



        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };

        hDate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dynamicDate = hDate1;
                new DatePickerDialog(admin_editHistory.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        hDate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dynamicDate = hDate2;
                new DatePickerDialog(admin_editHistory.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getName, getDate1, getDate2, getExtra, getType;
                switch(flag) {
                    case "1":

                        getName = hName.getText().toString();
                        getDate1 = hDate1.getText().toString();
                        getDate2 = hDate2.getText().toString();
                        getExtra = hExtra.getText().toString();
                        getType = hType.getText().toString();

                        if (TextUtils.isEmpty(getName) || TextUtils.isEmpty(getDate1) || TextUtils.isEmpty(getDate2) || TextUtils.isEmpty(getExtra)) {
                            Toast.makeText(admin_editHistory.this, "Please complete all fields", Toast.LENGTH_SHORT).show();
                        } else {
                            History his = new History();
                            his.setName(getName);
                            his.setDate1(getDate1);
                            his.setDate2(getDate2);
                            his.setExtra(getExtra);
                            his.setType(ehType);
                            his.sethID(ehID);
                            his.setpIMG(image);
                            his.setuUser(uUser);
                            his.setuName(fName);
                            his.setuPhone(uPhone);
                            his.setpName(petName);
                            his.setpID(petID);
                            his.setpWeight(petWeight);
                            his.setpAge(petAge);
                            his.setpBreed(petBreed);

                            db.collection("users/" + uUser + "/pets/" + petID + "/history")
                                    .document(ehID)
                                    .set(his)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(admin_editHistory.this, "New Entry Added", Toast.LENGTH_SHORT).show();
                                            Intent i = new Intent(admin_editHistory.this, admin_petDetails.class);
                                            i.putExtra("petName", petName);
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
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error adding document", e);
                                        }
                                    });
                        }
                        break;
                    case "0":
                        getName = hName.getText().toString();
                        getDate1 = hDate1.getText().toString();
                        getDate2 = hDate2.getText().toString();
                        getExtra = hExtra.getText().toString();

                        if (TextUtils.isEmpty(getName) || TextUtils.isEmpty(getDate1)) {
                            Toast.makeText(admin_editHistory.this, "Please complete all fields", Toast.LENGTH_SHORT).show();
                        } else {
                            ref = db.collection("users/" + uUser + "/pets/" + petID + "/history").document();

                            History his = new History();
                            his.setName(getName);
                            his.setDate1(getDate1);
                            his.setDate2(getDate2);
                            his.setExtra(getExtra);
                            his.setType(ehType);
                            his.sethID(String.valueOf(ref));

                            his.setpIMG(image);
                            his.setuUser(uUser);
                            his.setuName(fName);
                            his.setuPhone(uPhone);
                            his.setpName(petName);
                            his.setpID(petID);
                            his.setpWeight(petWeight);
                            his.setpAge(petAge);
                            his.setpBreed(petBreed);

                            db.collection("users/" + uUser + "/pets/" + petID + "/history")
                                    .document(his.gethID())
                                    .set(his)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(admin_editHistory.this, "New Entry Added", Toast.LENGTH_SHORT).show();
                                            Intent i = new Intent(admin_editHistory.this, admin_petDetails.class);
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
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error adding document", e);
                                        }
                                    });
                        }
                        break;
                }
            }
        });

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(admin_editHistory.this, admin_petDetails.class);
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

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ehID == null) {
                    Toast.makeText(admin_editHistory.this, "No such entry", Toast.LENGTH_SHORT).show();
                } else {
                    db.collection("users/" + uUser + "/pets/" + petID + "/history")
                            .document(ehID)
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: deleted file");
                                    Intent i = new Intent(admin_editHistory.this, admin_petDetails.class);
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
                                    Log.d(TAG, "Document successfully deleted");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error deleting document", e);

                                }
                            });
                }
            }
        });
    }

    private void updateLabel(){
        String myFormat="MM/dd/yy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        dynamicDate.setText(dateFormat.format(myCalendar.getTime()));
    }
}