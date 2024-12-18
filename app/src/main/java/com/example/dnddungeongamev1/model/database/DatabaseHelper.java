package com.example.dnddungeongamev1.model.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "dndDB.db";
    private static final int DB_VERSION = 1;
    private static String DB_PATH = "";
    private final Context context;
    private SQLiteDatabase database;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
        DB_PATH = context.getDatabasePath(DB_NAME).getPath();
    }

    public void createDatabase() {
        if (!checkDatabase()) {
            this.getReadableDatabase();
            copyDatabase();
        }
    }

    private boolean checkDatabase() {
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            Log.d("DB", "Database does not exist yet.");
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null;
    }

    private void copyDatabase() {
        try (InputStream input = context.getAssets().open(DB_NAME);
             OutputStream output = new FileOutputStream(DB_PATH)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
        } catch (IOException e) {
            throw new Error("Error copying database", e);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Not needed if using preloaded .db
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public SQLiteDatabase getDatabase() {
        if (database == null || !database.isOpen()) {
            database = SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READWRITE);
        }
        return database;
    }

    @Override
    public synchronized void close() {
        if (database != null) {
            database.close();
        }
        super.close();
    }

    public PlayerLocation getPlayerLocation(int playerId) {
        SQLiteDatabase db = getDatabase();
        Cursor cursor = db.rawQuery("SELECT player_loc_x, player_loc_y FROM player WHERE player_id = ?",
                new String[]{String.valueOf(playerId)});
        PlayerLocation location = null;

        if (cursor.moveToFirst()) {
            int locX = cursor.getInt(cursor.getColumnIndexOrThrow("player_loc_x"));
            int locY = cursor.getInt(cursor.getColumnIndexOrThrow("player_loc_y"));
            location = new PlayerLocation(locX, locY);
        }
        cursor.close();
        return location;
    }

    // Update Player Location
    public void updatePlayerLocation(int playerId, int newX, int newY) {
        SQLiteDatabase db = getDatabase();
        ContentValues values = new ContentValues();
        values.put("player_loc_x", newX);
        values.put("player_loc_y", newY);
        db.update("player", values, "player_id = ?", new String[]{String.valueOf(playerId)});
    }

    // Query Grid Coordinates
    public TileLocation getGridLocation(int gridId) {
        SQLiteDatabase db = getDatabase();
        Cursor cursor = db.rawQuery("SELECT grid_x, grid_y FROM grid WHERE grid_id = ?",
                new String[]{String.valueOf(gridId)});
        TileLocation location = null;

        if (cursor.moveToFirst()) {
            int gridX = cursor.getInt(cursor.getColumnIndexOrThrow("grid_x"));
            int gridY = cursor.getInt(cursor.getColumnIndexOrThrow("grid_y"));
            location = new TileLocation(gridX, gridY);
        }
        cursor.close();
        return location;
    }

    // Update Grid Location
    public void updateGridLocation(int gridId, int newGridX, int newGridY) {
        SQLiteDatabase db = getDatabase();
        ContentValues values = new ContentValues();
        values.put("grid_x", newGridX);
        values.put("grid_y", newGridY);
        db.update("grid", values, "grid_id = ?", new String[]{String.valueOf(gridId)});
    }

    public long addItem(String itemName, int itemQuantity, Integer gridId) {
        SQLiteDatabase db = getDatabase();
        ContentValues values = new ContentValues();
        values.put("item_name", itemName);
        values.put("item_quantity", itemQuantity);
        values.put("grid_id", gridId); // Can be null if item isn't on the grid

        return db.insert("item", null, values); // Returns new item_id
    }

    public void removeItem(int itemId) {
        SQLiteDatabase db = getDatabase();
        db.delete("item", "item_id = ?", new String[]{String.valueOf(itemId)});
    }

    public void updateItemQuantity(int itemId, int newQuantity) {
        SQLiteDatabase db = getDatabase();
        ContentValues values = new ContentValues();
        values.put("item_quantity", newQuantity);

        db.update("item", values, "item_id = ?", new String[]{String.valueOf(itemId)});
    }

    public void assignItemToPlayer(int playerId, int itemId, int itemQuantity) {
        SQLiteDatabase db = getDatabase();
        ContentValues values = new ContentValues();
        values.put("player_id", playerId);
        values.put("item_id", itemId);
        values.put("item_quantity", itemQuantity);

        db.insertWithOnConflict("player_item", null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void removeItemFromPlayer(int playerId, int itemId) {
        SQLiteDatabase db = getDatabase();
        db.delete("player_item", "player_id = ? AND item_id = ?",
                new String[]{String.valueOf(playerId), String.valueOf(itemId)});
    }

    public List<Items> getPlayerItems(int playerId) {
        SQLiteDatabase db = getDatabase();
        List<Items> itemList = new ArrayList<>();
        Cursor cursor = db.rawQuery(
                "SELECT i.item_id, i.item_name, pi.item_quantity " +
                        "FROM item i " +
                        "JOIN player_item pi ON i.item_id = pi.item_id " +
                        "WHERE pi.player_id = ?",
                new String[]{String.valueOf(playerId)}
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int itemId = cursor.getInt(cursor.getColumnIndexOrThrow("item_id"));
                String itemName = cursor.getString(cursor.getColumnIndexOrThrow("item_name"));
                int itemQuantity = cursor.getInt(cursor.getColumnIndexOrThrow("item_quantity"));

                itemList.add(new Items(itemId, itemName, itemQuantity));
            }
            cursor.close();
        }
        return itemList;
    }
}

