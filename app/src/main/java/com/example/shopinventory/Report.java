package com.example.shopinventory;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Report extends AppCompatActivity {

    LinearLayout btnSalesTab, btnInventoryTab;
    LinearLayout salesSection = findViewById(R.id.salesSection), inventorySection = findViewById(R.id.inventorySection);

    // CORRECT
    ListView salesListView, // CORRECT
            inventoryListView;  // FIXED
    Spinner inventorySortSpinner;

    InventoryDatabase db;

    @SuppressLint("WrongViewCast")
    public Report() {
        inventoryListView = findViewById(R.id.inventorySection);
        salesListView = findViewById(R.id.salesSection);
    }

    // Product class for inventory
    public static class Product {
        int id;
        String name;
        int quantity;
        double price;

        public Product(int id, String name, int quantity, double price) {
            this.id = id;
            this.name = name;
            this.quantity = quantity;
            this.price = price;
        }

        @Override
        public String toString() {
            return name + " | Qty: " + quantity + " | $" + price;
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        // -----------------------------
        // FIND VIEWS — MATCH XML EXACTLY
        // -----------------------------
        btnSalesTab = findViewById(R.id.btnSalesTab);
        btnInventoryTab = findViewById(R.id.btnInventoryTab);

        inventorySortSpinner = findViewById(R.id.inventorySortSpinner);

        db = new InventoryDatabase(this);

        // TAB CLICK HANDLERS
        btnSalesTab.setOnClickListener(v -> showSales());
        btnInventoryTab.setOnClickListener(v -> showInventory());

        // SHOW SALES FIRST
        showSales();
    }

    // -----------------------------
    // SHOW SALES TAB
    // -----------------------------
    private void showSales() {
        salesSection.setVisibility(View.VISIBLE);
        inventorySection.setVisibility(View.GONE);

        highlightTab(btnSalesTab, btnInventoryTab);

        ArrayList<String> sales = db.getAllSales();

        ArrayAdapter<String> salesAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, sales);

        salesListView.setAdapter(salesAdapter);
    }

    // -----------------------------
    // SHOW INVENTORY TAB
    // -----------------------------
    private void showInventory() {
        salesSection.setVisibility(View.GONE);
        inventorySection.setVisibility(View.VISIBLE);

        highlightTab(btnInventoryTab, btnSalesTab);

        ArrayList<Product> products = db.getAllProductsFull();

        ArrayAdapter<Product> inventoryAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, products);

        inventoryListView.setAdapter(inventoryAdapter);
    }

    // -----------------------------
    // TAB HIGHLIGHT FUNCTION
    // -----------------------------
    private void highlightTab(LinearLayout active, LinearLayout inactive) {
        active.setBackgroundColor(0xFFDDDDDD);     // light gray
        inactive.setBackgroundColor(0x00000000);   // transparent
    }
}
