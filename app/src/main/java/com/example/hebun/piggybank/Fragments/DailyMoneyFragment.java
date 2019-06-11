package com.example.hebun.piggybank.Fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.hebun.piggybank.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
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

import java.util.ArrayList;
import java.util.List;


public class DailyMoneyFragment extends Fragment {

    DatabaseReference myRef,myRef2;
    FirebaseDatabase database;
    StorageReference fbStorageRef;
    FirebaseAuth mAuth;
    FirebaseUser current;
    String date,money;
    Integer birlira,ellikr,yirmibeskr,onkr,beskr,birkr;
    String moneyName[];
    PieChart chart;
    ImageView imageView;


    public DailyMoneyFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daily_money, container, false);

        mAuth = FirebaseAuth.getInstance();
        current = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        fbStorageRef = FirebaseStorage.getInstance().getReference();
        chart = (PieChart) view.findViewById(R.id.chart);

        imageView = view.findViewById(R.id.imageProcess);



        Bundle bundle = getArguments();
        date = bundle.getString("DATE");
        money = bundle.getString("MONEY");


        getDatabase();

        return view;
    }

    public FirebaseDatabase getDatabase() {
        myRef = database.getReference().child("Customers").child("user_"+current.getUid()).child("Money Transactions").child(date);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                birlira = dataSnapshot.child("TypesMoney").child("1Lira").getValue(Integer.class);
                ellikr = dataSnapshot.child("TypesMoney").child("50Kr").getValue(Integer.class);
                yirmibeskr = dataSnapshot.child("TypesMoney").child("25Kr").getValue(Integer.class);
                onkr = dataSnapshot.child("TypesMoney").child("10Kr").getValue(Integer.class);
                beskr = dataSnapshot.child("TypesMoney").child("5Kr").getValue(Integer.class);
                birkr = dataSnapshot.child("TypesMoney").child("1Kr").getValue(Integer.class);
                String img = dataSnapshot.child("downloadURL").getValue(String.class);

                Picasso.get().load(img).into(imageView);


                int moneys[] = {birlira, ellikr,yirmibeskr,onkr,beskr,birkr};

                String moneyName[] ={"1 Lira", "50 Kuruş", "25 Kuruş", "10 Kuruş", "5 Kuruş", "1 Kuruş"};

                List<PieEntry> pieEntries = new ArrayList<>();
                for (int i = 0; i < moneys.length; i++ ){
                    pieEntries.add(new PieEntry(moneys[i],moneyName[i]));
                }
                PieDataSet dataSet = new PieDataSet(pieEntries,"Moneys");

                dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
                dataSet.setSliceSpace(5f);
                dataSet.setSelectionShift(5f);

                PieData data = new PieData(dataSet);

                chart.setCenterText(money+" ₺");
                chart.setCenterTextColor(R.color.colorPrimaryDark);
                chart.setCenterTextSize(30f);
                chart.setExtraOffsets(5,10,5,5);
                chart.setDragDecelerationFrictionCoef(0.95f);
                chart.setDrawHoleEnabled(true);
                chart.setTransparentCircleRadius(61f);

                data.setValueTextSize(15f);
                data.setValueTextColor(R.color.colorPrimaryDark);

                chart.setData(data);
                chart.animateY(3000);
                chart.invalidate();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return database;
    }

}
