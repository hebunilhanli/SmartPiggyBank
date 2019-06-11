package com.example.hebun.piggybank.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.hebun.piggybank.R;

public class LoginRecordViewHolder  extends RecyclerView.ViewHolder {
    public TextView dateRecord;
    public TextView timeRecord;
    public TextView statusRecord;
    public TextView ipRecord;


    public LoginRecordViewHolder(@NonNull View itemView) {
        super(itemView);

        dateRecord = itemView.findViewById(R.id.recordDate);
        timeRecord = itemView.findViewById(R.id.recordTime);
        ipRecord = itemView.findViewById(R.id.recordIP);
        statusRecord = itemView.findViewById(R.id.recordStatus);
    }
}
