package com.example.hebun.piggybank;


import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

class RecyclerViewHolder extends RecyclerView.ViewHolder{
    public ImageView addPiggy;
    public TextView dateCount;
    public TextView monthCount;
    public TextView processMoney;

    public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        addPiggy = itemView.findViewById(R.id.addPiggy);
        dateCount = itemView.findViewById(R.id.dateCount);
        monthCount = itemView.findViewById(R.id.monthDate);
        processMoney = itemView.findViewById(R.id.processing);

    }
}

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
    private List<Data> list = new ArrayList<Data>();

    public RecyclerViewAdapter(List<Data> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = layoutInflater.inflate(R.layout.item,viewGroup,false);
        return new RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int i) {
        holder.addPiggy.setImageResource(list.get(i).getImageID());
        holder.dateCount.setText(list.get(i).getString());
        holder.monthCount.setText(list.get(i).getString());
        holder.processMoney.setText(list.get(i).getString());



    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
