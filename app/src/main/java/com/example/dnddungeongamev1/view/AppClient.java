package com.example.dnddungeongamev1.view;

import com.example.dnddungeongamev1.controller.GameEngine;
import com.example.dnddungeongamev1.model.Levels;
import com.example.dnddungeongamev1.model.database.Database;

public enum AppClient {
    INSTANCE;

    private final Database db;
    private final Levels level;
    private final GameEngine engine;

    AppClient() {
        this.db = new Database();
        this.level = new Levels();
        this.engine = new GameEngine(level, db);
    }

    public Database getDatabase() {
        return db;
    }

    public Levels getLevels() {
        return level;
    }

    public GameEngine getGameEngine() {
        return engine;
    }


}
