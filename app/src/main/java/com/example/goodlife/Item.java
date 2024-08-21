package com.example.goodlife;

public class Item {

    String name;

    int image, button;

    public Item(String name) {
        this.name = name;
    }

//    public Item(String name, int image, int button) {
//        this.name = name;
//        this.image = image;
//        this.button = button;
//    }

    public int getButton() {
        return button;
    }

    public void setButton(int button) {
        this.button = button;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
