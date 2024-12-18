package com.example.dnddungeongamev1.model.levels;

public interface LevelObject {

    String search();
    String getName();
    void addItem();
    String removeItem(String itemName);

    String imageLink();


}
