package com.example.hebun.piggybank.Fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import java.util.Map;


public class SavingMoneyFragment extends Fragment {

    DatabaseReference myRef;
    FirebaseDatabase database;
    StorageReference fbStorageRef;
    FirebaseAuth mAuth;
    FirebaseUser current;
    double sumss;
    TextView sumsMoney;
    EditText goal,time,title;
    Button calculate;
    RadioGroup radioGroup;
    RadioButton one,two,three;


    public SavingMoneyFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saving_money, container, false);

        mAuth = FirebaseAuth.getInstance();
        current = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        fbStorageRef = FirebaseStorage.getInstance().getReference();

        goal = view.findViewById(R.id.goal);
        calculate = view.findViewById(R.id.calculatorrr);
        time = view.findViewById(R.id.timessss);
        radioGroup = view.findViewById(R.id.radioGroup2);
        one = view.findViewById(R.id.week);
        two = view.findViewById(R.id.month);
        three = view.findViewById(R.id.year);
        title = view.findViewById(R.id.titless);

        sumsMoney = view.findViewById(R.id.sumMoneySaving);

        getDatabase();

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Fragment calculik = new CalculateSMFragment();

                Bundle bundle = new Bundle();


                if (one.isChecked()){
                    bundle = new Bundle();
                    bundle.putString("Date", "per week for " + time.getText().toString() + " weekly");
                }
                if (two.isChecked()){
                    bundle = new Bundle();
                    bundle.putString("Date", "per month for " + time.getText().toString() + " monthly");
                }
                if (three.isChecked()){
                    bundle = new Bundle();
                    bundle.putString("Date", "per year for " + time.getText().toString() + " yearly");
                }
                bundle.putString("Total",goal.getText().toString());
                bundle.putString("Time",time.getText().toString());
                bundle.putString("BankMoney",sumsMoney.getText().toString());


                calculik.setArguments(bundle);

                FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.savingGoalFR,calculik).addToBackStack(null);
                fragmentTransaction.commit();


            }
        });





        return view;
    }

    public FirebaseDatabase getDatabase() {


        myRef = database.getReference("Customers").child("user_" + current.getUid()).child("Money Transactions");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sumss = 0;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    Map<String, String> map = (Map<String, String>) ds.getValue();
                    String moneys = map.get("money");
                    if (dataSnapshot.exists()) {
                        double mValue = Double.parseDouble(moneys);
                        sumss += mValue;
                        sumsMoney.setText((String.valueOf(sumss)));
                    } else {
                        sumsMoney.setText("0.0");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


        return database;
    }
}
