package com.example.dnddungeongamev1.controller;

import com.example.dnddungeongamev1.model.Levels;
import com.example.dnddungeongamev1.model.database.Database;

public class GameEngine {

    Levels level;
    Database db;
    public GameEngine(Levels level, Database db) {
        this.level = level;
        this.db = db;

    }
}
