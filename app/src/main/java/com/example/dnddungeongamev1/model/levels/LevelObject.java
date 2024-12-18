package com.example.dnddungeongamev1.model.levels;

import com.example.dnddungeongamev1.model.database.Items;

import java.util.List;

public interface LevelObject {

    String search();
    String getName();
    void addItem(Items item);
    String removeItem(String itemName);
    List<Items> getItems();
    Boolean obtainCheck();

    String imageLink();


}
