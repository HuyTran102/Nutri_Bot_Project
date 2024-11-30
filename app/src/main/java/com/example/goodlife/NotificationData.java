package com.example.goodlife;

public class NotificationData {
    public String name, information, time;

    public NotificationData() {
    }

    public NotificationData(String name, String information, String time) {
        this.name = name;
        this.information = information;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
