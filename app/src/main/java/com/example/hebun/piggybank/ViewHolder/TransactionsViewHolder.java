package com.example.hebun.piggybank.ViewHolder;

import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hebun.piggybank.R;

public class TransactionsViewHolder extends RecyclerView.ViewHolder {

    public TextView dateCount;
    public TextView month;
    public ImageView piggyAdd;
    public TextView plus;
    public TextView money;
    public TextView moneyType;

    public TransactionsViewHolder(@NonNull View itemView) {
        super(itemView);

        dateCount = itemView.findViewById(R.id.dateCount);
        month = itemView.findViewById(R.id.monthDate);
        piggyAdd = itemView.findViewById(R.id.addPiggy);
        plus = itemView.findViewById(R.id.plus);
        money = itemView.findViewById(R.id.processing);
        moneyType =itemView.findViewById(R.id.tl);
    }
}
