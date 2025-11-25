package com.example.shopinventory;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class InventoryDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "inventory.db";
    private static final int DATABASE_VERSION = 1;

    public InventoryDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // PRODUCT TABLE (Matches your structure)
        db.execSQL(
                "CREATE TABLE Products (" +
                        "product_id INTEGER PRIMARY KEY AUTOINCREMENT, " + // Product Code
                        "product_name TEXT NOT NULL, " +
                        "quantity_remaining INTEGER NOT NULL, " +
                        "price REAL NOT NULL)"
        );

        // SALES TABLE (Matches your structure)
        db.execSQL(
                "CREATE TABLE Sales (" +
                        "sale_id INTEGER PRIMARY KEY AUTOINCREMENT, " +  // Sales ID
                        "product_id INTEGER NOT NULL, " +                // Product id
                        "sale_quantity INTEGER NOT NULL, " +
                        "sales_total REAL NOT NULL, " +
                        "sale_datetime TEXT NOT NULL, " +
                        "FOREIGN KEY(product_id) REFERENCES Products(product_id))"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop in correct order (FK dependency)
        db.execSQL("DROP TABLE IF EXISTS Sales");
        db.execSQL("DROP TABLE IF EXISTS Products");

        // Recreate
        onCreate(db);
    }
}
