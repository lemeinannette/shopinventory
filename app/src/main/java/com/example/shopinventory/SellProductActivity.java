
package com.example.shopinventory;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class SellProductActivity extends AppCompatActivity {

    private Spinner productDropdown;
    private EditText quantityInput, priceInput;
    private ImageButton backHomeButton, saveChangesButton;

    private InventoryDatabase dbHelper;
    private ArrayList<String> productNames;
    private ArrayList<Integer> productIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_product);

        productDropdown = findViewById(R.id.productDropdown);
        quantityInput = findViewById(R.id.quantityInput);
        priceInput = findViewById(R.id.priceInput);
        backHomeButton = findViewById(R.id.backHomeButton);
        saveChangesButton = findViewById(R.id.saveChangesButton);

        dbHelper = new InventoryDatabase(this);

        loadProductsIntoSpinner();

        saveChangesButton.setOnClickListener(v -> processSale());
        backHomeButton.setOnClickListener(v -> {
            startActivity(new Intent(SellProductActivity.this, MainActivity.class));
            finish();
        });
    }

    // Load all product names into spinner
    private void loadProductsIntoSpinner() {
        productNames = new ArrayList<>();
        productIds = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT product_id, product_name FROM Products", null);

        while (cursor.moveToNext()) {
            productIds.add(cursor.getInt(0));      // product_id
            productNames.add(cursor.getString(1)); // product_name
        }

        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                productNames
        );
        productDropdown.setAdapter(adapter);
    }

    private void processSale() {

        if (productNames.isEmpty()) {
            Toast.makeText(this, "No products available!", Toast.LENGTH_SHORT).show();
            return;
        }

        String qtyStr = quantityInput.getText().toString().trim();
        String priceStr = priceInput.getText().toString().trim();

        if (qtyStr.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(this, "Enter all fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        int quantityToSell = Integer.parseInt(qtyStr);
        double price = Double.parseDouble(priceStr);

        if (quantityToSell <= 0) {
            Toast.makeText(this, "Invalid quantity!", Toast.LENGTH_SHORT).show();
            return;
        }

        int productIndex = productDropdown.getSelectedItemPosition();
        int productId = productIds.get(productIndex);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Get current stock
        Cursor cursor = db.rawQuery(
                "SELECT quantity_remaining FROM Products WHERE product_id = ?",
                new String[]{String.valueOf(productId)}
        );

        int currentStock = 0;
        if (cursor.moveToFirst()) {
            currentStock = cursor.getInt(0);
        }
        cursor.close();

        if (quantityToSell > currentStock) {
            Toast.makeText(this, "Not enough stock!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Calculate total
        double salesTotal = quantityToSell * price;

        // Insert into Sales Table
        ContentValues saleValues = new ContentValues();
        saleValues.put("product_id", productId);
        saleValues.put("sale_quantity", quantityToSell);
        saleValues.put("sales_total", salesTotal);
        saleValues.put("sale_datetime", getCurrentDateTime());

        db.insert("Sales", null, saleValues);

        // Update product quantity
        int newStock = currentStock - quantityToSell;
        ContentValues updateValues = new ContentValues();
        updateValues.put("quantity_remaining", newStock);

        db.update("Products", updateValues, "product_id = ?", new String[]{String.valueOf(productId)});

        Toast.makeText(this, "Product sold!", Toast.LENGTH_SHORT).show();

        quantityInput.setText("");
        priceInput.setText("");
    }

    // Generate current datetime string
    private String getCurrentDateTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                .format(new Date());
    }
}
