package com.example.hyponic.model;

public class DateFormatter {
    private String date;
    private String month;
    private String year;
    private String dd_mm;

    public DateFormatter(String data){
        int indexSpasi = data.indexOf(" ");
        String dd_mm_yy = data.substring(0,indexSpasi);
        setYear(dd_mm_yy.substring(0,4));
        setMonth(dd_mm_yy.substring(6,7));
        setDate(dd_mm_yy);
        setDd_mm(getDate()+"-"+getMonth());
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
//        if(month.equals("01")){
//            this.month = "Jan";
//        }else if(month.equals("02")){
//            this.month = "Feb";
//        }else if(month.equals("03")){
//            this.month = "Mar";
//        }else if(month.equals("04")){
//            this.month = "Apr";
//        }else if(month.equals("05")){
//            this.month = "Mei";
//        }else if(month.equals("06")){
//            this.month = "Jun";
//        }else if(month.equals("07")){
//            this.month = "Jul";
//        }else if(month.equals("08")){
//            this.month = "Agust";
//        }else if(month.equals("09")){
//            this.month = "Sept";
//        }else if(month.equals("10")){
//            this.month = "Okt";
//        }else if(month.equals("11")){
//            this.month = "Nov";
//        }else{
//            this.month="Des";
//        }

    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDd_mm() {
        return dd_mm;
    }

    public void setDd_mm(String dd_mm) {
        this.dd_mm = dd_mm;
    }
}
