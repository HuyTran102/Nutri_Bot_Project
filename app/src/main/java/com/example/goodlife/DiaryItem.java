package com.example.goodlife;

public class DiaryItem {
    public String name, unit_type, unit_name;
    public int image;
    public int kcal;
    public int adding_year, adding_month, adding_day, adding_hour, adding_minute, adding_second;
    public double amount, protein, lipid, glucid;

    public DiaryItem(String name, double amount, int kcal,  double protein
            , double lipid, double glucid, String unit_type, String unit_name
            , int image, int adding_year, int adding_month, int adding_day
            , int adding_hour, int adding_minute, int adding_second) {
        this.name = name;
        this.amount = amount;
        this.kcal = kcal;
        this.protein = protein;
        this.lipid = lipid;
        this.glucid = glucid;
        this.unit_type = unit_type;
        this.unit_name = unit_name;
        this.image = image;
        this.adding_year = adding_year;
        this.adding_month = adding_month;
        this.adding_day = adding_day;
        this.adding_hour = adding_hour;
        this.adding_minute = adding_minute;
        this.adding_second = adding_second;
    }

    public int getAdding_second() {
        return adding_second;
    }

    public void setAdding_second(int adding_second) {
        this.adding_second = adding_second;
    }

    public int getAdding_minute() {
        return adding_minute;
    }

    public void setAdding_minute(int adding_minute) {
        this.adding_minute = adding_minute;
    }

    public int getAdding_hour() {
        return adding_hour;
    }

    public void setAdding_hour(int adding_hour) {
        this.adding_hour = adding_hour;
    }

    public int getAdding_month() {
        return adding_month;
    }

    public void setAdding_month(int adding_month) {
        this.adding_month = adding_month;
    }

    public int getAdding_year() {
        return adding_year;
    }

    public void setAdding_year(int adding_year) {
        this.adding_year = adding_year;
    }

    public int getAdding_day() {
        return adding_day;
    }

    public void setAdding_day(int adding_day) {
        this.adding_day = adding_day;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getUnit_name() {
        return unit_name;
    }

    public void setUnit_name(String unit_name) {
        this.unit_name = unit_name;
    }

    public double getGlucid() {
        return glucid;
    }

    public void setGlucid(double glucid) {
        this.glucid = glucid;
    }

    public double getLipid() {
        return lipid;
    }

    public void setLipid(double lipid) {
        this.lipid = lipid;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getKcal() {
        return kcal;
    }

    public void setKcal(int kcal) {
        this.kcal = kcal;
    }


    public String getUnit_type() {
        return unit_type;
    }

    public void setUnit_type(String unit_type) {
        this.unit_type = unit_type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
