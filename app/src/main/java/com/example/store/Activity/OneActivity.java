package com.example.store.Activity;


import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.store.R;
import com.example.store.models.Products;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OneActivity extends AppCompatActivity {

    private ImageView productImage;
    private TextView productName, productCategory, productQty, productPrice;
    private DatabaseReference databaseReference;

    private static final String TAG = "OneActivity";

    private String value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one);

        // Initialize Views
        productImage = findViewById(R.id.prod_image);
        productName = findViewById(R.id.name);
        productCategory = findViewById(R.id.category);
        productQty = findViewById(R.id.prod_qty);
        productPrice = findViewById(R.id.price);

        databaseReference = FirebaseDatabase.getInstance().getReference("Products");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Products");

//        Toast.makeText(this, "This is a toast message!", Toast.LENGTH_SHORT).show();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String ImageUrl = snapshot.child("imageUrl").getValue(String.class);
                    String prodName = snapshot.child("productName").getValue(String.class);
                    String productCode = snapshot.child("productCode").getValue(String.class);
                    String prodCategory = snapshot.child("productCategory").getValue(String.class);
                    Double prodPrice = snapshot.child("productPrice").getValue(Double.class);
                    String prodQty = snapshot.child("productQty").getValue(String.class);
                    String productDescr = snapshot.child("productDescription").getValue(String.class);

                    value = prodName;
                    // Load image using Glide

                    productName.setText(prodName);
                    productCategory.setText(prodCategory);
                    productPrice.setText(" $" + prodPrice);
                    productQty.setText(prodQty);

                    Glide.with(OneActivity.this)
                            .load(ImageUrl)
                            .into(productImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Data loading cancelled: " + error.getMessage());
            }
        });

//        Toast.makeText(this, "This is a toast message! " + value, Toast.LENGTH_SHORT).show();

        // Fetch the first product from Firebase
//        loadFirstProduct();
    }

    private void loadFirstProduct() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                        Products product = productSnapshot.getValue(Products.class);
                        if (product != null) {
                            // Set data to views
                            productName.setText(product.getProductName());
                            productCategory.setText(product.getProductCategory());
                            productQty.setText(product.getProductQty());
                            productPrice.setText("$"+ product.getProductPrice());
                            product.setProductDescription(product.getProductDescription());
                            product.setProductCode(product.getProductCode());
                            // Load image using Glide
                            Glide.with(OneActivity.this)
                                    .load(R.drawable.prod1)
                                    .placeholder(R.drawable.prod1)
                                    .into(productImage);
                        } else {
                            // Log an error if the product is null
                            android.util.Log.e("FirebaseError", "Product is null!");
                        }
                        break; // Stop after loading the first item
                    }
                } else {
                    android.util.Log.e("FirebaseError", "No products found in database!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                android.util.Log.e("FirebaseError", "Database error: " + error.getMessage());
            }
        });
    }

}
