package com.example.shopinventory;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class RestockProductsActivity extends AppCompatActivity {

    Spinner spinnerProducts;
    EditText productQuantity;
    ImageButton btnSaveRestock, btnBackHome;

    InventoryDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restock_products);

        db = new InventoryDatabase(this);

        spinnerProducts = findViewById(R.id.spinnerProducts);
        productQuantity = findViewById(R.id.productQuantity);
        btnSaveRestock = findViewById(R.id.btnSaveRestock);
        btnBackHome = findViewById(R.id.btnBackHome);

        loadProductsIntoSpinner();

        btnSaveRestock.setOnClickListener(view -> restockProduct());
        btnBackHome.setOnClickListener(view -> finish());
    }

    // ⭐ LOAD PRODUCTS INTO DROPDOWN
    private void loadProductsIntoSpinner() {

        // 🔥 FIXED: This should call a GET method, not addProducts()
        ArrayList<String> productNames = db.getAllProductNames();

        if (productNames.isEmpty()) {
            productNames.add("No products available");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                productNames
        );

        spinnerProducts.setAdapter(adapter);
    }

    // ⭐ RESTOCK FUNCTION
    private void restockProduct() {
        String product = spinnerProducts.getSelectedItem().toString();
        String qtyString = productQuantity.getText().toString().trim();

        if (product.equals("No products available")) {
            Toast.makeText(this, "No products to restock", Toast.LENGTH_SHORT).show();
            return;
        }

        if (qtyString.isEmpty()) {
            productQuantity.setError("Enter quantity");
            return;
        }

        int qty = Integer.parseInt(qtyString);

        // 🔥 Update quantity in the database
        boolean success = db.restockProduct(Integer.parseInt(product), qty);

        if (success) {
            Toast.makeText(this, "Restocked " + product + " by " + qty, Toast.LENGTH_SHORT).show();
            productQuantity.setText("");
        } else {
            Toast.makeText(this, "Failed to restock", Toast.LENGTH_SHORT).show();
        }
    }
}
