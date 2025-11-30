package com.example.shopinventory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class InventoryDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "inventory.db";
    private static final int DATABASE_VERSION = 1;

    public InventoryDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // PRODUCT TABLE
        db.execSQL(
                "CREATE TABLE Products (" +
                        "product_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "product_name TEXT NOT NULL, " +
                        "quantity_remaining INTEGER NOT NULL, " +
                        "price REAL NOT NULL)"
        );

        // SALES TABLE
        db.execSQL(
                "CREATE TABLE Sales (" +
                        "sale_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "product_id INTEGER NOT NULL, " +
                        "sale_quantity INTEGER NOT NULL, " +
                        "sale_total REAL NOT NULL, " +
                        "sale_datetime TEXT NOT NULL, " +
                        "FOREIGN KEY(product_id) REFERENCES Products(product_id))"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Sales");
        db.execSQL("DROP TABLE IF EXISTS Products");
        onCreate(db);
    }

    // ----------------------------------------------------------
    // ADD PRODUCT (returns boolean)
    // ----------------------------------------------------------
    public boolean addProducts(String name, int quantity, double price) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("product_name", name);
        cv.put("quantity_remaining", quantity);
        cv.put("price", price);

        long insert = db.insert("Products", null, cv);

        return insert != -1;  // true = success, false = failed
    }

    // ----------------------------------------------------------
    // GET ALL PRODUCT NAMES
    // ----------------------------------------------------------
    public ArrayList<String> getAllProducts() {
        ArrayList<String> productList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT product_name FROM Products", null);

        if (cursor.moveToFirst()) {
            do {
                productList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return productList;
    }

    // ----------------------------------------------------------
    // RESTOCK PRODUCT
    // ----------------------------------------------------------
    public boolean restockProduct(int productId, int quantityToAdd) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL(
                "UPDATE Products SET quantity_remaining = quantity_remaining + ? WHERE product_id = ?",
                new Object[]{quantityToAdd, productId}
        );

        return true;
    }

    public ArrayList<String> getAllProductNames() {
        ArrayList<String> productList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT product_id, product_name FROM Products", null);

        if (cursor.moveToFirst()) {
            do {
                // store names with ID so spinner can identify exact product
                int id = cursor.getInt(0);
                String name = cursor.getString(1);

                // Format: "1 - Sugar"
                productList.add(id + " - " + name);

            } while (cursor.moveToNext());
        }

        cursor.close();
        return productList;
    }
}