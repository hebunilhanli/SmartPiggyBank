package com.example.hebun.piggybank;

public class RecordData {

    public String dateRecord,timeRecord,statusRecord,ipRecord;

    public RecordData(String dateCount, String month, String money) {
        this.dateRecord = dateRecord;
        this.timeRecord = timeRecord;
        this.statusRecord = statusRecord;
        this.ipRecord = ipRecord;

    }

    public RecordData(){

    }

    public String getDateRecord() {
        return dateRecord;
    }

    public void setDateRecord(String dateRecord) {
        this.dateRecord = dateRecord;
    }

    public String getTimeRecord() {
        return timeRecord;
    }

    public void setTimeRecord(String timeRecord) {
        this.timeRecord = timeRecord;
    }

    public String getStatusRecord() {
        return statusRecord;
    }

    public void setStatusRecord(String statusRecord) {
        this.statusRecord = statusRecord;
    }

    public String getIpRecord() {
        return ipRecord;
    }

    public void setIpRecord(String ipRecord) {
        this.ipRecord = ipRecord;
    }
}
