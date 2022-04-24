package com.example.oppawtunity;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.budiyev.android.codescanner.CodeScanner;
import com.google.zxing.WriterException;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class qr_generator extends AppCompatActivity {
    Button goBack;
    ImageView qrIMG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_generator);

        goBack = findViewById(R.id.goBackFromQR);
        qrIMG = findViewById(R.id.qrIMAGE);
        Bundle extras = getIntent().getExtras();
        String fName = extras.getString("uName");
        String uPhone = extras.getString("uPhone");
        String uUser = extras.getString("uUser");

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(qr_generator.this, adminViewUserPets.class);
                i.putExtra("uName",fName);
                i.putExtra("uPhone",uPhone);
                i.putExtra("uUser",uUser);
                startActivity(i);
                finish();
            }
        });

        generateQR();
    }

    private void generateQR() {

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
        // true;uUser;fName;uPhone;pID;pName;pAge;pBreed;pWeight;pIMG
        String text = "true" + ';' + uUser + ';' + fName + ';' + uPhone + ';' + pID + ';' + pName + ';' + pAge + ';' + pBreed + ';' + pWeight + ';' + pIMG;

        MultiFormatWriter writer = new MultiFormatWriter();
        try {
            BitMatrix matrix = writer.encode(text, BarcodeFormat.QR_CODE,600,600);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(matrix);
            //set data image to imageview
            qrIMG.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}