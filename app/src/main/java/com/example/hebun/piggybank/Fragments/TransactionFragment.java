package com.example.hebun.piggybank.Fragments;

import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hebun.piggybank.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A placeholder fragment containing a simple view.
 */
public class TransactionFragment extends Fragment {

    CircleImageView mediumP;
    ImageView loginRecord, statistics, savingGoal, contact;
    TextView fullName, lastEntry;
    DatabaseReference myRef;
    FirebaseDatabase database;
    StorageReference fbStorageRef;
    FirebaseAuth mAuth;
    FirebaseUser current;

    public TransactionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaction, container, false);

        loginRecord = view.findViewById(R.id.loginRecord);
        statistics = view.findViewById(R.id.statistics);
        savingGoal = view.findViewById(R.id.savingGoal);
        contact = view.findViewById(R.id.contactInfo);
        mediumP = view.findViewById(R.id.mediumPP);
        fullName = view.findViewById(R.id.fullnameT);
        lastEntry = view.findViewById(R.id.lastSuccessEntry);


        mAuth = FirebaseAuth.getInstance();
        current = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        fbStorageRef = FirebaseStorage.getInstance().getReference();

        getDatabase();





        return view;
    }

    public FirebaseDatabase getDatabase() {
        myRef = database.getReference("Customers").child("user_" + current.getUid());
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String Fullname = dataSnapshot.child("Fullname").getValue(String.class);
                fullName.setText(Fullname.toUpperCase());

                String ppurl = dataSnapshot.child("PP").getValue(String.class);
                Picasso.get().load(ppurl).into(mediumP);

                String date = dataSnapshot.child("Entry Transactions").child("Last Entry").child("Date").getValue(String.class);
                lastEntry.setText(date);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return database;
    }
}
