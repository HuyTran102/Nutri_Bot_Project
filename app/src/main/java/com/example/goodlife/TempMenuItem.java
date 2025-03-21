package com.example.goodlife;

public class TempMenuItem {
    public String name;
    public int icon_image, data_image;

    public TempMenuItem(String name, int icon_image, int data_image) {
        this.name = name;
        this.icon_image = icon_image;
        this.data_image = data_image;
    }

    public int getData_image() {
        return data_image;
    }

    public void setData_image(int data_image) {
        this.data_image = data_image;
    }

    public int getIcon_image() {
        return icon_image;
    }

    public void setIcon_image(int image) {
        this.icon_image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
