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
    LinearLayout salesSection, inventorySection;

    ListView salesListView, inventoryListView;
    Spinner inventorySortSpinner;

    InventoryDatabase db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        // FIND VIEWS
        btnSalesTab = findViewById(R.id.btnSalesTab);
        btnInventoryTab = findViewById(R.id.btnInventoryTab);

        salesSection = findViewById(R.id.salesSection);
        inventorySection = findViewById(R.id.inventorySection);

        inventorySortSpinner = findViewById(R.id.inventorySortSpinner);

        // CREATE LISTVIEWS DYNAMICALLY
        salesListView = new ListView(this);
        inventoryListView = new ListView(this);

        // Add listviews to layout
        salesSection.addView(salesListView);
        inventorySection.addView(inventoryListView);

        db = new InventoryDatabase(this);

        // Click handlers
        btnSalesTab.setOnClickListener(v -> showSales());
        btnInventoryTab.setOnClickListener(v -> showInventory());

        // Default tab
        showSales();
    }

    private void showSales() {
        salesSection.setVisibility(View.VISIBLE);
        inventorySection.setVisibility(View.GONE);

        highlightTab(btnSalesTab, btnInventoryTab);

        ArrayList<String> sales = db.getAllSales();

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, sales);

        salesListView.setAdapter(adapter);
    }

    private void showInventory() {
        salesSection.setVisibility(View.GONE);
        inventorySection.setVisibility(View.VISIBLE);

        highlightTab(btnInventoryTab, btnSalesTab);

        ArrayList<Report.Product> products = db.getAllProductsFull();

        ArrayAdapter<Product> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, products);

        inventoryListView.setAdapter(adapter);
    }

    private void highlightTab(LinearLayout active, LinearLayout inactive) {
        active.setBackgroundColor(0xFFDDDDDD); // active
        inactive.setBackgroundColor(0x00000000); // transparent
    }

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
}
