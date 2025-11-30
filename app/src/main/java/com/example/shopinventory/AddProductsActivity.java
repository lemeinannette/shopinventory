package com.example.shopinventory;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddProductsActivity extends AppCompatActivity {

    EditText productName, productQuantity, productPrice;
    ImageButton btnSave, btnBack;

    InventoryDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_products);

        // Initialize views
        productName = findViewById(R.id.productName);
        productQuantity = findViewById(R.id.productQuantity);
        productPrice = findViewById(R.id.productPrice);

        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btnBack);

        db = new InventoryDatabase(this);

        // Save product on click
        btnSave.setOnClickListener(v -> saveProduct());

        // Go back
        btnBack.setOnClickListener(v -> finish());
    }

    private void saveProduct() {
        String name = productName.getText().toString().trim();
        String qtyStr = productQuantity.getText().toString().trim();
        String priceStr = productPrice.getText().toString().trim();

        if (name.isEmpty()) {
            productName.setError("Enter product name");
            return;
        }
        if (qtyStr.isEmpty()) {
            productQuantity.setError("Enter quantity");
            return;
        }
        if (priceStr.isEmpty()) {
            productPrice.setError("Enter price");
            return;
        }

        int quantity = Integer.parseInt(qtyStr);
        double price = Double.parseDouble(priceStr);

        boolean success = db.addProducts(name, quantity, price);

        if (success) {
            Toast.makeText(this, "Product added successfully!", Toast.LENGTH_SHORT).show();
            productName.setText("");
            productQuantity.setText("");
            productPrice.setText("");
        } else {
            Toast.makeText(this, "Failed to add product.", Toast.LENGTH_SHORT).show();
        }
    }
}
