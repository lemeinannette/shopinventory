package com.example.shopinventory;

import static com.example.shopinventory.R.id.main;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class Report extends AppCompatActivity {

    LinearLayout btnSalesTab, btnInventoryTab;
    LinearLayout salesSection, inventorySection;
    Spinner inventorySortSpinner;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_report);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // --------------------------------------
        // FIND VIEWS
        // --------------------------------------
        btnSalesTab = findViewById(R.id.btnSalesTab);
        btnInventoryTab = findViewById(R.id.btnInventoryTab);

        salesSection = findViewById(R.id.salesSection);
        inventorySection = findViewById(R.id.inventorySection);

        inventorySortSpinner = findViewById(R.id.inventorySortSpinner);

        // --------------------------------------
        // SETUP SPINNER
        // --------------------------------------
        setupInventorySpinner();

        // --------------------------------------
        // TAB CLICK HANDLERS
        // --------------------------------------
        btnSalesTab.setOnClickListener(v -> showSales());
        btnInventoryTab.setOnClickListener(v -> showInventory());

        // SHOW SALES BY DEFAULT
        showSales();
    }

    // ---------------------------------------------------
    // SHOW SALES SECTION
    // ---------------------------------------------------
    private void showSales() {
        salesSection.setVisibility(View.VISIBLE);
        inventorySection.setVisibility(View.GONE);

        highlightTab(btnSalesTab, btnInventoryTab);
    }

    // ---------------------------------------------------
    // SHOW INVENTORY SECTION
    // ---------------------------------------------------
    private void showInventory() {
        salesSection.setVisibility(View.GONE);
        inventorySection.setVisibility(View.VISIBLE);

        highlightTab(btnInventoryTab, btnSalesTab);
    }

    // ---------------------------------------------------
    // OPTIONAL TAB HIGHLIGHTING
    // ---------------------------------------------------
    private void highlightTab(LinearLayout active, LinearLayout inactive) {
        active.setBackgroundColor(Color.parseColor("#DDDDDD"));
        inactive.setBackgroundColor(Color.TRANSPARENT);
    }

    // ---------------------------------------------------
    // SETUP SORT OPTIONS FOR INVENTORY
    // ---------------------------------------------------
    private void setupInventorySpinner() {
        ArrayList<String> options = new ArrayList<>();
        options.add("Sort by Name");
        options.add("Sort by Quantity");
        options.add("Sort by Price");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                options
        );

        inventorySortSpinner.setAdapter(adapter);
    }
}
