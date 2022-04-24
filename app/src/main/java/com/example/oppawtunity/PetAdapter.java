package com.example.oppawtunity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class PetAdapter extends ArrayAdapter<Pets>{
    public PetAdapter(@NonNull Context context, ArrayList<Pets> usersArrayList) {
        super(context, 0, usersArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listView = convertView;
        if (listView == null) {
            listView = LayoutInflater.from(getContext()).inflate(R.layout.view_pets, parent, false);
        }
        Pets pet = getItem(position);
        TextView pName = listView.findViewById(R.id.viewPetName);
        TextView pAge = listView.findViewById(R.id.viewPetAge);
        TextView pWeight = listView.findViewById(R.id.viewPetWeight);
        TextView pBreed = listView.findViewById(R.id.viewPetBreed);
        ImageView pImage = listView.findViewById(R.id.viewPetImage);

        Picasso.with(getContext()).load(pet.getImgURL()).into(pImage);

        pName.setText(pet.getName());
        pAge.setText(pet.getAge());
        pWeight.setText(pet.getWeight());
        pBreed.setText(pet.getBreed());

        listView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), admin_petDetails.class);
                i.putExtra("petName",pet.getName());
                i.putExtra("petAge", pet.getAge());
                i.putExtra("petWeight", pet.getWeight());
                i.putExtra("petBreed", pet.getBreed());
                i.putExtra("petIMG", pet.getImgURL());
                i.putExtra("uUser", pet.getuUser());
                i.putExtra("petID", pet.getPetID());
                i.putExtra("uPhone", pet.getuPhone());
                i.putExtra("uName", pet.getuName());
                v.getContext().startActivity(i);
                ((Activity)v.getContext()).finish();
            }
        });
        return listView;
    }
}
