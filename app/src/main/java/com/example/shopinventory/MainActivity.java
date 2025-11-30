package com.example.shopinventory;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Add Products button
        ImageButton btnAddProducts = findViewById(R.id.btnAddProducts);
        btnAddProducts.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, AddProductsActivity.class));
        });

        // Reports button
        ImageButton btnReports = findViewById(R.id.btnReports);
        btnReports.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, Report.class));
        });

        // Restock Products button
        ImageButton btnRestockProducts = findViewById(R.id.btnRestockProducts);
        btnRestockProducts.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, RestockProductsActivity.class));
        });

        // Sold Products button
        ImageButton btnSoldProducts = findViewById(R.id.btnSoldProducts);
        btnSoldProducts.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, SellProductActivity.class));
        });
    }
}
