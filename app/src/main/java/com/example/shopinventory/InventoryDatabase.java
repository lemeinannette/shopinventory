package com.example.shopinventory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class InventoryDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "inventory.db";
    private static final int DATABASE_VERSION = 2;

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

    // ---------------------------
    // CLEAR DATABASE
    // ---------------------------
    public void clearDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM Sales");
        db.execSQL("DELETE FROM Products");
    }

    // ---------------------------
    // ADD PRODUCT
    // ---------------------------
    public boolean addProducts(String name, int quantity, double price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("product_name", name);
        cv.put("quantity_remaining", quantity);
        cv.put("price", price);
        long insert = db.insert("Products", null, cv);
        return insert != -1;
    }

    // ---------------------------
    // RESTOCK PRODUCT
    // ---------------------------
    public boolean restockProduct(int productId, int quantityToAdd) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(
                "UPDATE Products SET quantity_remaining = quantity_remaining + ? WHERE product_id = ?",
                new Object[]{quantityToAdd, productId}
        );
        return true;
    }

    // ---------------------------
    // GET ALL PRODUCTS (names only)
    // ---------------------------
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

    // ---------------------------
    // GET ALL PRODUCT NAMES WITH IDS
    // ---------------------------
    public ArrayList<String> getAllProductNames() {
        ArrayList<String> productList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT product_id, product_name FROM Products", null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                productList.add(id + " - " + name);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return productList;
    }

    // ---------------------------
    // GET FULL PRODUCTS (for Inventory)
    // ---------------------------
    public ArrayList<Report.Product> getAllProductsFull() {
        ArrayList<Report.Product> products = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT product_id, product_name, quantity_remaining, price FROM Products", null
        );
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                int qty = cursor.getInt(2);
                double price = cursor.getDouble(3);
                products.add(new Report.Product(id, name, qty, price));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return products;
    }

    // ---------------------------
    // GET ALL SALES
    // ---------------------------
    public ArrayList<String> getAllSales() {
        ArrayList<String> sales = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT S.sale_id, P.product_name, S.sale_quantity, S.sale_total, S.sale_datetime " +
                        "FROM Sales S JOIN Products P ON S.product_id = P.product_id", null
        );
        if (cursor.moveToFirst()) {
            do {
                int saleId = cursor.getInt(0);
                String name = cursor.getString(1);
                int qty = cursor.getInt(2);
                double total = cursor.getDouble(3);
                String date = cursor.getString(4);
                sales.add("Sale #" + saleId + ": " + name + " | Qty: " + qty + " | $" + total + " | " + date);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return sales;
    }
}
