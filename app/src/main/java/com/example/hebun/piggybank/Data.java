package com.example.hebun.piggybank;

import android.widget.ImageView;
import android.widget.TextView;

public class Data {

    public String dateCount, month, money;

    public Data(String dateCount, String month, String money) {
        this.dateCount = dateCount;
        this.month = month;
        this.money = money;
    }

    public Data() {
    }

    public String getDateCount() {
        return dateCount;
    }

    public void setDateCount(String dateCount) {
        this.dateCount = dateCount;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}
