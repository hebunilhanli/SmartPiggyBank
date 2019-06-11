package com.example.hebun.piggybank.Fragments;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.util.Log;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A placeholder fragment containing a simple view.
 */
public class TransactionFragment extends Fragment {

    CircleImageView mediumP;
    TextView fullName, successEntry,failedEntry;
    DatabaseReference myRef,myRef2;
    FirebaseDatabase database;
    StorageReference fbStorageRef;
    FirebaseAuth mAuth;
    FirebaseUser current;
    CardView loginRecords, contactInfo,statisticsCV,banktransss,savinggoal;
    ArrayList<String> failed;



    public TransactionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaction, container, false);


        mediumP = view.findViewById(R.id.mediumPP);
        fullName = view.findViewById(R.id.fullnameT);
        successEntry = view.findViewById(R.id.lastSuccessEntry);
        loginRecords = view.findViewById(R.id.loginRecord);
        failedEntry = view.findViewById(R.id.lastFailedEntry);
        contactInfo = view.findViewById(R.id.contactInfor);
        statisticsCV = view.findViewById(R.id.statisticsCV);
        banktransss = view.findViewById(R.id.banktransss);
        savinggoal = view.findViewById(R.id.savingGoalTR);

        failed = new ArrayList<>();


        mAuth = FirebaseAuth.getInstance();
        current = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        myRef2 = database.getReference("Customers").child("user_" + current.getUid()).child("Entry Transactions").child("Login Record");
        fbStorageRef = FirebaseStorage.getInstance().getReference();

        getDatabase();

        loginRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment loginRecordlik = new LoginRecordFragment();

                FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_left_to_right,R.anim.exit_to_right);
                fragmentTransaction.replace(R.id.transactionFrame,loginRecordlik).addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        contactInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment contactlik = new ContactInformationFragment();

                FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_left_to_right,R.anim.exit_to_right);
                fragmentTransaction.replace(R.id.transactionFrame,contactlik).addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        statisticsCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment statisticslik = new StatisticsFragment();

                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_left_to_right,R.anim.exit_to_right);
                transaction.replace(R.id.transactionFrame,statisticslik).addToBackStack(null);
                transaction.commit();
            }
        });
        banktransss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef = database.getReference().child("Customers").child("user_"+current.getUid());
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        myRef.child("kasadurumu").setValue("0");
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

                Fragment piggylik = new PiggyTransactionFragment();

                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_left_to_right,R.anim.exit_to_right);
                transaction.replace(R.id.transactionFrame,piggylik).addToBackStack(null);
                transaction.commit();
            }
        });
        savinggoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment savinglik = new SavingMoneyFragment();

                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_left_to_right,R.anim.exit_to_right);
                transaction.replace(R.id.transactionFrame,savinglik).addToBackStack(null);
                transaction.commit();
            }
        });








        return view;
    }

    public FirebaseDatabase getDatabase() {
        myRef = database.getReference("Customers").child("user_" + current.getUid());
        final Query failedQuery = myRef2.orderByChild("statusRecord").equalTo("Failed");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {




                String Fullname = dataSnapshot.child("Fullname").getValue(String.class);
                fullName.setText(Fullname.toUpperCase());

                String ppurl = dataSnapshot.child("PP").getValue(String.class);
                Picasso.get().load(ppurl).into(mediumP);

                long count = dataSnapshot.child("Entry Transactions").child("Login Record").getChildrenCount();
                final String status = dataSnapshot.child("Entry Transactions").child("Login Record").child(String.valueOf(count)).child("dateRecord").getValue(String.class);
                successEntry.setText(status);


                failedQuery.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                          String userUID = String.valueOf(dsp.getKey());
                          Log.e("LİST LİST ==", userUID );
                            failed.add(userUID);
                        }if (dataSnapshot.exists()){
                            String date = dataSnapshot.child(failed.get(failed.size()-1)).child("dateRecord").getValue(String.class);
                            failedEntry.setText(date);
                        }else {
                            failedEntry.setText("-/-/-");
                        }


                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return database;
    }
}
