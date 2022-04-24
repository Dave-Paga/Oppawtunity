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
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class HistoryAdapter extends ArrayAdapter<History> {

    public HistoryAdapter(@NonNull Context context, ArrayList<History> historyArrayList) {
        super(context, 0, historyArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        FirebaseAuth mAuth = null;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef;

        View listView = convertView;
        if (listView == null) {
            listView = LayoutInflater.from(getContext()).inflate(R.layout.view_history, parent, false);
        }
        History history = getItem(position);
        TextView hName = listView.findViewById(R.id.historyName);
        TextView date1 = listView.findViewById(R.id.date1);
        TextView date2 = listView.findViewById(R.id.date2);
        TextView extra = listView.findViewById(R.id.extra);

        switch(history.getType()) {
            case "1":
                hName.setText("Vaccine: " + history.getName());
                date1.setText("Vaccine Date: " + history.getDate1());
                break;
            case "2":
                hName.setText("Surgery: " + history.getName());
                date1.setText("Surgery Date: " + history.getDate1());
                break;
            case "3":
                hName.setText("Medication: " + history.getName());
                date1.setText("Start Date: " + history.getDate1());
                date2.setText("End Date: " + history.getDate2());
                break;
            case "4":
                hName.setText("Clinic Location: " + history.getName());
                date1.setText("Date: " + history.getDate1());
                extra.setText("Nature of Visit: " + history.getExtra());
                break;
        }

        listView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(history.getAdmin() == false) {

                } else if (history.getType() != null) {
                    Intent i = new Intent(v.getContext(), admin_editHistory.class);
                    i.putExtra("name", history.getName());
                    i.putExtra("date1", history.getDate1());
                    i.putExtra("date2", history.getDate2());
                    i.putExtra("extra", history.getExtra());
                    i.putExtra("type", history.getType());
                    i.putExtra("historyID", history.gethID());

                    i.putExtra("petName",history.getpName());
                    i.putExtra("petAge", history.getpAge());
                    i.putExtra("petWeight", history.getpWeight());
                    i.putExtra("petBreed", history.getpBreed());
                    i.putExtra("petIMG",history.getpIMG());
                    i.putExtra("uUser", history.getuUser());
                    i.putExtra("petID", history.getpID());
                    i.putExtra("uPhone", history.getuPhone());
                    i.putExtra("uName", history.getuName());

                    i.putExtra("editFlag", "1");
                    v.getContext().startActivity(i);
                    ((Activity)v.getContext()).finish();
                }
            }
        });
        return listView;
    }

}

/*



public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder>{
    ArrayList<History> historyArrayList;
    private TextView hName, date1, date2, extra;
    private Context context;
    private History history;

    private int recID;


    public HistoryAdapter(Context context,ArrayList<History> historyArrayList, int recID) {
        this.context = context;
        this.historyArrayList = historyArrayList;
        this.recID = recID;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.view_history,parent,false);
        ViewHolder viewHolder= new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        History history1 = this.historyArrayList.get(position);
        holder.bindHistory(history1);
        History history = ;

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(history.getType())) {
                    Intent i = new Intent(view.getContext(), admin_editHistory.class);
                    i.putExtra("name", history.getName());
                    i.putExtra("date1", history.getDate1());
                    i.putExtra("date2", history.getDate2());
                    i.putExtra("extra", history.getExtra());
                    i.putExtra("type", history.getType());
                    i.putExtra("historyID", history.gethID());


                    i.putExtra("petName",history.getpName());
                    i.putExtra("petAge", history.getpAge());
                    i.putExtra("petWeight", history.getpWeight());
                    i.putExtra("petBreed", history.getpBreed());
                    i.putExtra("petIMG",history.getpIMG());
                    i.putExtra("uUser", history.getuUser());
                    i.putExtra("petID", history.getpID());
                    i.putExtra("uPhone", history.getuPhone());
                    i.putExtra("uName", history.getuName());

                    i.putExtra("editFlag", "1");
                    view.getContext().startActivity(i);
                    ((Activity)view.getContext()).finish();
                }
            }
        });


    }


    @Override
    public int getItemCount() {
        return historyArrayList.size();
    }

    @Override
    public void onClick(View view) {
        int itemPosition = mRecyclerView.getChildLayoutPosition(view);

        History history = historyArrayList.get(itemPosition);
        if (TextUtils.isEmpty(history.getType())) {
            Intent i = new Intent(view.getContext(), admin_editHistory.class);
            i.putExtra("name", history.getName());
            i.putExtra("date1", history.getDate1());
            i.putExtra("date2", history.getDate2());
            i.putExtra("extra", history.getExtra());
            i.putExtra("type", history.getType());
            i.putExtra("historyID", history.gethID());


            i.putExtra("petName",history.getpName());
            i.putExtra("petAge", history.getpAge());
            i.putExtra("petWeight", history.getpWeight());
            i.putExtra("petBreed", history.getpBreed());
            i.putExtra("petIMG",history.getpIMG());
            i.putExtra("uUser", history.getuUser());
            i.putExtra("petID", history.getpID());
            i.putExtra("uPhone", history.getuPhone());
            i.putExtra("uName", history.getuName());

            i.putExtra("editFlag", "1");
            view.getContext().startActivity(i);
            ((Activity)view.getContext()).finish();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public RecyclerView recyclerView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            hName = itemView.findViewById(R.id.historyName);
            date1 = itemView.findViewById(R.id.date1);
            date2 = itemView.findViewById(R.id.date2);
            extra = itemView.findViewById(R.id.extra);
            recyclerView = itemView.findViewById(recID);
        }

        public void bindHistory(History history) {
            switch(history.getType()) {
                case "1":
                    hName.setText("Vaccine: " + history.getName());
                    date1.setText("Vaccine Date: " + history.getDate1());
                    break;
                case "2":
                    hName.setText("Surgery: " + history.getName());
                    date1.setText("Surgery Date: " + history.getDate1());
                    break;
                case "3":
                    hName.setText("Medication: " + history.getName());
                    date1.setText("Start Date: " + history.getDate1());
                    date2.setText("End Date: " + history.getDate2());
                    break;
                case "4":
                    hName.setText("Clinic Location: " + history.getName());
                    date1.setText("Date: " + history.getDate1());
                    extra.setText("Nature of Visit: " + history.getDate2());
                    break;
            }
        }
    }


}



 */
