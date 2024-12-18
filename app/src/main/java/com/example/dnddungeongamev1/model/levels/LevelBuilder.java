package com.example.dnddungeongamev1.model.levels;

import com.example.dnddungeongamev1.model.database.Items;

public class LevelBuilder {
    //initialized that
    private Level level;


    //constructor
    public LevelBuilder(String name)
    {
        level = new Level(name);
    }

    //below are setters for the locked rooms and lights on


    public LevelBuilder setLocked(Items key) {
        level.lock(key);
        return this;
    }


    public LevelBuilder setLightsOn(Boolean lightsOn) {
        level.toggleLights(lightsOn);
        return this;
    }


    public LevelBuilder addObject(LevelObject object) {
        level.addObject(object);
        return this;
    }



    //to build the room by returning the room structure.
    public Level build() {
        return level;
    }

}
