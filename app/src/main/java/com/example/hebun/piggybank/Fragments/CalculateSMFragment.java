package com.example.hebun.piggybank.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hebun.piggybank.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CalculateSMFragment extends Fragment {

    RecyclerView recyclerView;
    TextView form,iniac,totalentity,totalaccum;
    String time,goal,totalbank,check;



    public CalculateSMFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calculate_sm, container, false);

        form = view.findViewById(R.id.form);
        iniac = view.findViewById(R.id.iniac);
        totalentity = view.findViewById(R.id.totalentity);
        totalaccum = view.findViewById(R.id.totalaccum);

        Bundle IDs = getArguments();
        time = IDs.getString("Time");
        goal = IDs.getString("Total");
        totalbank = IDs.getString("BankMoney");
        check = IDs.getString("Date");

        Log.e("OROROROR", (time+goal+totalbank+check));

        float accum = (Float.valueOf(goal)-Float.valueOf(totalbank))/Float.valueOf(time);
        String acc = String.valueOf(accum);

        form.setText(acc+ " TL" + check);
        iniac.setText(totalbank);
        totalentity.setText(String.valueOf(Float.valueOf(goal)-Float.valueOf(totalbank)));
        totalaccum.setText(goal);










    return view;
    }

}
