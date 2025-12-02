package com.example.shopinventory;

import static com.example.shopinventory.R.id.imgInventoryTab;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Report extends AppCompatActivity {

    LinearLayout btnSalesTab, btnInventoryTab;
    LinearLayout salesSection, inventorySection;
    ImageButton btnGenerateSave, btnBackHome;
    Spinner inventorySortSpinner;

    ListView listSales, listInventory;

    InventoryDatabase db;

    ArrayList<String> salesList = new ArrayList<>();
    ArrayList<Product> inventoryList = new ArrayList<>();
    ArrayAdapter<String> salesAdapter;
    ArrayAdapter<String> inventoryAdapter;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        db = new InventoryDatabase(this);

        // -------------------------------
        // FIND VIEWS
        // -------------------------------
        btnSalesTab = findViewById(R.id.btnSalesTab);
        btnInventoryTab = findViewById(R.id.btnInventoryTab);

        salesSection = findViewById(R.id.salesSection);
        inventorySection = findViewById(R.id.inventorySection);

        btnGenerateSave = findViewById(R.id.btnGenerateSave);
        btnBackHome = findViewById(R.id.btnBackHome);
        inventorySortSpinner = findViewById(R.id.inventorySortSpinner);

        listSales = findViewById(R.id.imgSalesTab);
        listInventory = findViewById(imgInventoryTab);

        // -------------------------------
        // BUTTON LISTENERS
        // -------------------------------
        btnGenerateSave.setOnClickListener(v ->
                Toast.makeText(this, "Report Generated / Saved!", Toast.LENGTH_SHORT).show());

        btnBackHome.setOnClickListener(v -> finish());

        btnSalesTab.setOnClickListener(v -> showSales());
        btnInventoryTab.setOnClickListener(v -> showInventory());

        // -------------------------------
        // SETUP LISTS
        // -------------------------------
        salesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, salesList);
        listSales.setAdapter(salesAdapter);

        inventoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listInventory.setAdapter(inventoryAdapter);

        // -------------------------------
        // SETUP INVENTORY SPINNER
        // -------------------------------
        setupInventorySpinner();

        // Show Sales tab by default
        showSales();
    }

    // -------------------------------
    // SHOW SALES SECTION
    // -------------------------------
    private void showSales() {
        salesSection.setVisibility(View.VISIBLE);
        inventorySection.setVisibility(View.GONE);
        highlightTab(btnSalesTab, btnInventoryTab);

        // Load sales from DB
        salesList.clear();
        ArrayList<String> salesFromDb = db.getAllSales(); // We will add this method
        salesList.addAll(salesFromDb);
        salesAdapter.notifyDataSetChanged();
    }

    // -------------------------------
    // SHOW INVENTORY SECTION
    // -------------------------------
    private void showInventory() {
        salesSection.setVisibility(View.GONE);
        inventorySection.setVisibility(View.VISIBLE);
        highlightTab(btnInventoryTab, btnSalesTab);

        // Load inventory from DB
        inventoryList.clear();
        inventoryList.addAll(db.getAllProductsFull()); // method returns list of Product objects
        updateInventoryList();
    }

    // -------------------------------
    // TAB HIGHLIGHTING
    // -------------------------------
    private void highlightTab(LinearLayout active, LinearLayout inactive) {
        active.setBackgroundColor(0xFFDDDDDD);
        inactive.setBackgroundColor(0x00000000);
    }

    // -------------------------------
    // SETUP INVENTORY SPINNER
    // -------------------------------
    private void setupInventorySpinner() {
        ArrayList<String> options = new ArrayList<>();
        options.add("Sort by Name");
        options.add("Sort by Quantity");
        options.add("Sort by Price");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, options);
        inventorySortSpinner.setAdapter(spinnerAdapter);

        inventorySortSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                sortInventory(position);
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) { }
        });
    }

    private void sortInventory(int option) {
        if (option == 0) { // Name
            Collections.sort(inventoryList, Comparator.comparing(p -> p.name));
        } else if (option == 1) { // Quantity
            Collections.sort(inventoryList, Comparator.comparingInt(p -> p.quantity));
        } else if (option == 2) { // Price
            Collections.sort(inventoryList, Comparator.comparingDouble(p -> p.price));
        }
        updateInventoryList();
    }

    private void updateInventoryList() {
        inventoryAdapter.clear();
        for (Product p : inventoryList) {
            inventoryAdapter.add(p.name + " | Qty: " + p.quantity + " | $" + p.price);
        }
        inventoryAdapter.notifyDataSetChanged();
    }

    // -------------------------------
    // PRODUCT MODEL
    // -------------------------------
    public static class Product {
        public int id;
        public String name;
        public int quantity;
        public double price;

        public Product(int id, String name, int quantity, double price) {
            this.id = id;
            this.name = name;
            this.quantity = quantity;
            this.price = price;
        }
    }
}
