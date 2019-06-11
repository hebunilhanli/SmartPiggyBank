package com.example.hebun.piggybank.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hebun.piggybank.R;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class StatisticsFragment extends Fragment {
    DatabaseReference myRef;
    FirebaseDatabase database;
    StorageReference fbStorageRef;
    FirebaseAuth mAuth;
    FirebaseUser current;
    HorizontalBarChart statisticsChart;
    int birliras, ellikrs, yirmibeskrs, onkrs, beskrs, birkrs ;


    public StatisticsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        mAuth = FirebaseAuth.getInstance();
        current = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        fbStorageRef = FirebaseStorage.getInstance().getReference();

        statisticsChart = (HorizontalBarChart) view.findViewById(R.id.statisticsChart);

        getDatabase();




        return view;
    }

    public FirebaseDatabase getDatabase() {
        myRef = database.getReference("Customers").child("user_" + current.getUid()).child("Money Transactions");
        Query query = myRef.orderByChild("TypesMoney");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                birliras = 0;
                ellikrs = 0;
                yirmibeskrs = 0;
                onkrs = 0;
                beskrs = 0;
                birkrs = 0;

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    String userUID = String.valueOf(ds.getKey());
                    Log.e("CACACACA",userUID);

                    Map<String, Object> map = (Map<String, Object>) ds.child("TypesMoney").getValue();
                    Object birlira = map.get("1Lira");
                    Object ellikr = map.get("50Kr");
                    Object yirmibes = map.get("25Kr");
                    Object onkr = map.get("10Kr");
                    Object beskr = map.get("5Kr");
                    Object birkr = map.get("1Kr");


                    birliras += Float.parseFloat(String.valueOf(Integer.parseInt(String.valueOf(birlira))));
                    ellikrs += Float.parseFloat(String.valueOf(Integer.parseInt(String.valueOf(ellikr))));
                    beskrs += Float.parseFloat(String.valueOf(Integer.parseInt(String.valueOf(beskr))));
                    yirmibeskrs += Float.parseFloat(String.valueOf(Integer.parseInt(String.valueOf(yirmibes))));
                    beskrs += Float.parseFloat(String.valueOf(Integer.parseInt(String.valueOf(beskr))));
                    birkrs += Float.parseFloat(String.valueOf(Integer.parseInt(String.valueOf(birkr))));
                    onkrs += Float.parseFloat(String.valueOf(Integer.parseInt(String.valueOf(onkr))));

                    float moneys[] = {birliras, ellikrs,yirmibeskrs,onkrs,beskrs,birkrs};
                    String moneyName[] ={"1 Lira", "50 Kuruş", "25 Kuruş", "10 Kuruş", "5 Kuruş", "1 Kuruş"};
                    List<BarEntry> barEntries = new ArrayList<>();
                    for (int i = 0; i < moneys.length; i++ ){
                        barEntries.add(new BarEntry(10f*i,moneys[i]));

                    }



                    XAxis xAxis = statisticsChart.getXAxis();
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setDrawGridLines(false);
                    xAxis.setValueFormatter(new IndexAxisValueFormatter(moneyName));


                    BarDataSet set1;

                    set1 = new BarDataSet(barEntries,"Data1");
                    set1.setColors(ColorTemplate.JOYFUL_COLORS);
                    set1.setValueTextSize(20f);
                    BarData data = new BarData(set1);
                    data.setBarWidth(9f);

                    statisticsChart.setData(data);
                    statisticsChart.animateY(3000);
                    statisticsChart.invalidate();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        return database;
    }
    public ArrayList<String> getAreaCount() {
        String moneyName[] ={"1 Lira", "50 Kuruş", "25 Kuruş", "10 Kuruş", "5 Kuruş", "1 Kuruş"};

        ArrayList<String> label = new ArrayList<>();
        for (int i = 0; i < moneyName.length; i++)
            label.add(String.valueOf(moneyName[0]));
            label.add(String.valueOf(moneyName[1]));
            label.add(String.valueOf(moneyName[2]));
        return label;
    }

}
