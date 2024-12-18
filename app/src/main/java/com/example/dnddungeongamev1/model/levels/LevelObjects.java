package com.example.dnddungeongamev1.model.levels;

import com.example.dnddungeongamev1.model.database.Items;

import java.util.ArrayList;
import java.util.List;

public class LevelObjects implements LevelObject{

    private String name;
    private List<Items> items;

    //constructor
    public LevelObjects(String name)
    {
        this.items = new ArrayList<>();
        this.name = name;
    }

    //search for the items in the room or inside a room object.
    @Override
    public String search() {
        if(items.isEmpty())
        {
            return "The " + name + " is empty.";
        }

        StringBuilder result = new StringBuilder("Inside the " + name + ", you find: ");

        for(Items item : items)
        {
            result.append(item.getName() + "\n");
        }
        return result.toString();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void addItem(Items item) {
        items.add(item);
    }

    //allows us to remove an item from the ArrayList when player takes that item.
    @Override
    public String removeItem(String itemName) {
        for(Items item : items)
        {
            if(item.getName().equalsIgnoreCase(itemName))
            {
                items.remove(item);
                return "You took the " + itemName + " from the " + name + ".";
            }

        }
        return "There is no " + itemName + " in the " + name + ".";
    }

    @Override
    public List<Items> getItems() {
        return items;
    }




    @Override
    public Boolean obtainCheck() {
        for (Items item : items) {
            if (item.getName().equalsIgnoreCase("key") ||
                    item.getName().equalsIgnoreCase("flashlight") ||
                    item.getName().equalsIgnoreCase("map")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String imageLink() {
        return "";
    }
}
