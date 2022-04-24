package com.example.oppawtunity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

public class UserAdapter extends ArrayAdapter<Users> {
    public UserAdapter(@NonNull Context context, ArrayList<Users> usersArrayList) {
        super(context, 0, usersArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listView = convertView;
        if (listView == null) {
            listView = LayoutInflater.from(getContext()).inflate(R.layout.view_user_card, parent, false);
        }
        Users users = getItem(position);
        TextView fName = listView.findViewById(R.id.viewFullName);
        TextView uName = listView.findViewById(R.id.viewUsername);
        TextView pNumber = listView.findViewById(R.id.viewPhoneNo);

        fName.setText(users.getFullName());
        uName.setText(users.getUsername());
        pNumber.setText(users.getPhoneNo());

        listView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), adminViewUserPets.class);
                i.putExtra("uName",users.getFullName());
                i.putExtra("uPhone",users.getPhoneNo());
                i.putExtra("uUser", users.getUsername());
                v.getContext().startActivity(i);
                ((Activity)v.getContext()).finish();
            }
        });
        return listView;
    }
}
