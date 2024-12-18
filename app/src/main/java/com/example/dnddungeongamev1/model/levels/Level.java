package com.example.dnddungeongamev1.model.levels;

import com.example.dnddungeongamev1.model.database.Items;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Level {
    private String name;
    private Boolean isLocked;
    private Boolean lightsOn;
    private Optional<Items> keyRequired;
    private List<LevelObject> levelObjects;

    //constructor setting the basics for a typical room
    public Level(String name) {
        //setting the name parameter
        this.name = name;
        //made basic room accessible at all times.
        this.isLocked = false;
        this.lightsOn = true;
        this.keyRequired = Optional.empty();
        this.levelObjects = new ArrayList<>();

    }

    //getter for room name.
    public String getName()
    {
        return name;
    }

    //method to lock room
    public void lock(Items key) {
        this.isLocked = true;
        this.keyRequired = Optional.of(key);
    }

    //method to unlock room
    public void unlock(Items key) {
        this.isLocked = false;

        //check if key is required and if we have a key
        if (this.keyRequired.isPresent() && keyRequired.get().equals(key))
        {
            this.isLocked = false;
        }

    }

    //method to turn lights on
    public void toggleLights(Boolean lightsOn)
    {
        this.lightsOn = lightsOn;
    }

    //method to add objects to our List object
    public void addObject(LevelObject object)
    {
        levelObjects.add(object);

    }

    //method to enter a room with validation, based on if the lights are on or not and if the room is locked
    public String enterRoom(Items playerKey)
    {
        if(!lightsOn)
        {
            return "Too dark to enter the room " + name;
        }

        if(isLocked)
        {
            if(keyRequired.isPresent() && keyRequired.get().equals(playerKey))
            {
                //statement that calls the name (in the constructor with room name)
                return "You unlocked and entered the room of " + name;
            }

            return "The " + name + " is locked";
        }

        return "You have entered the " + name;

    }


    //method to search room, based off of what is in the roomObjects list for each specific room.
    public String searchRoom()
    {
        if(levelObjects.isEmpty())
        {
            return "The " + name + " is empty.";
        }

        StringBuilder objectsList = new StringBuilder("You look around the " + name + " and see the following things: ");

        //loop through the list of objects that were added for that specific room.
        for(LevelObject object: levelObjects)
        {
            objectsList.append(object.getName()).append(", ");
        }

        //return that list of objects in a substring.
        return objectsList.substring(0, objectsList.length() - 2) + ".";
    }


    //get the first letter of that name so that it may appear on the map.
    public char getSymbol() {
        return name.charAt(0);
    }
}
