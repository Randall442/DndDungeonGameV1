package com.example.dnddungeongamev1.controller;

import android.content.Context;

import com.example.dnddungeongamev1.model.database.DatabaseHelper;
import com.example.dnddungeongamev1.model.database.PlayerLocation;

public class GameEngine {
    private final Context context;
    private final DatabaseHelper dbHelper;
    private PlayerLocation playerLocation;
    private GameControllerListener listener;

    public interface GameControllerListener {
        void onPlayerMoved(String direction, int x, int y);
        void onInteraction(String action);
    }


    public GameEngine(Context context) {
        this.context = context;
        this.dbHelper = new DatabaseHelper(context);
        this.dbHelper.createDatabase();

        this.playerLocation = dbHelper.getPlayerLocation(1);
    }

    public void setListener(GameControllerListener listener) {
        this.listener = listener;
    }

    public void movePlayer(String direction) {
        if (playerLocation == null) return;

        switch (direction) {
            case "North":
                playerLocation.setY(playerLocation.getY() + 1);
                break;
            case "South":
                playerLocation.setY(playerLocation.getY() - 1);
                break;
            case "East":
                playerLocation.setX(playerLocation.getX() + 1);
                break;
            case "West":
                playerLocation.setX(playerLocation.getX() - 1);
                break;
        }


        dbHelper.updatePlayerLocation(1, playerLocation.getX(), playerLocation.getY());


        if (listener != null) {
            listener.onPlayerMoved(direction, playerLocation.getX(), playerLocation.getY());
        }
    }

    public void interact(String action) {
        // Perform interaction logic
        if (listener != null) {
            listener.onInteraction(action);
        }
    }
}
