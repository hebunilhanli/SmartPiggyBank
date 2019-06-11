package com.example.hebun.piggybank.Fragments;

import android.icu.text.AlphabeticIndex;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hebun.piggybank.Data;
import com.example.hebun.piggybank.R;
import com.example.hebun.piggybank.RecordData;
import com.example.hebun.piggybank.ViewHolder.LoginRecordViewHolder;
import com.example.hebun.piggybank.ViewHolder.TransactionsViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * A placeholder fragment containing a simple view.
 */
public class LoginRecordFragment extends Fragment {

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    FirebaseRecyclerOptions<RecordData> recyclerOptions;
    FirebaseRecyclerAdapter<RecordData,LoginRecordViewHolder> recyclerAdapter;
    DatabaseReference myRef,myRef2;
    FirebaseDatabase database;
    StorageReference fbStorageRef;
    FirebaseAuth mAuth;
    FirebaseUser current;

    public LoginRecordFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_login_record, container, false);

        recyclerView = view.findViewById(R.id.recycleRecord);

        mAuth = FirebaseAuth.getInstance();
        current = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("Customers").child("user_"+current.getUid()).child("Entry Transactions").child("Login Record");
        fbStorageRef = FirebaseStorage.getInstance().getReference();

        recyclerOptions = new FirebaseRecyclerOptions.Builder<RecordData>().setQuery(myRef,RecordData.class).build();

        recyclerAdapter = new FirebaseRecyclerAdapter<RecordData, LoginRecordViewHolder>(recyclerOptions) {
            @Override
            protected void onBindViewHolder(LoginRecordViewHolder holder, int position, RecordData model) {

                holder.dateRecord.setText(model.getDateRecord());
                holder.ipRecord.setText(model.getIpRecord());
                if (model.getStatusRecord().equals("Failed")) {
                    holder.statusRecord.setTextColor(getResources().getColor(R.color.fui_bgEmail));
                    holder.statusRecord.setText(model.getStatusRecord());
                }else if (model.getStatusRecord().equals("Success")){
                    holder.statusRecord.setTextColor(getResources().getColor(R.color.colorPrimary));
                    holder.statusRecord.setText(model.getStatusRecord());
                }
                holder.timeRecord.setText(model.getTimeRecord());

            }


            @NonNull
            @Override
            public LoginRecordViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                View view1 = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recorditem,viewGroup,false);
                return new LoginRecordViewHolder(view1);
            }
        };

        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter.startListening();
        recyclerView.setAdapter(recyclerAdapter);

        return view;
    }
}
