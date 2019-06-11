package com.example.hebun.piggybank.Fragments;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import io.ghyeok.stickyswitch.widget.StickySwitch;

/**
 * A simple {@link Fragment} subclass.
 */
public class PiggyTransactionFragment extends Fragment{

    StickySwitch stickySwitch;
    DatabaseReference myRef,myRef2;
    FirebaseDatabase database;
    StorageReference fbStorageRef;
    FirebaseAuth mAuth;
    FirebaseUser current;
    TextView kasakasa;


    public PiggyTransactionFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_piggy_transaction, container, false);

        stickySwitch = view.findViewById(R.id.coverSwitch);
        kasakasa = view.findViewById(R.id.kasadurumu);

        mAuth = FirebaseAuth.getInstance();
        current = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        fbStorageRef = FirebaseStorage.getInstance().getReference();


        stickySwitch.setAnimationDuration(1000);
        stickySwitch.setOnSelectedChangeListener(new StickySwitch.OnSelectedChangeListener() {
            @Override
            public void onSelectedChange(StickySwitch.Direction direction, String s) {
                myRef = database.getReference().child("Customers").child("user_"+current.getUid());
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (String.valueOf(stickySwitch.getDirection()) == "RIGHT"){
                            myRef.child("kasadurumu").setValue("1");
                            kasakasa.setText("Please insert the coins. After the coins are stacked, slide the button to the left.");

                        }else if (String.valueOf(stickySwitch.getDirection()) == "LEFT"){
                            myRef.child("kasadurumu").setValue("2");
                            kasakasa.setText("The transaction is complete. We're transferring you to the main screen. You can check the process.");


                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    Fragment mainlik = new MainScreenFragment();

                                    FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                                    transaction.replace(R.id.piggyFrame, mainlik);
                                    transaction.commit();

                                }
                            },7000);



                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });




        return view;
    }

}
