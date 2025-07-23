package com.example.store.Activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.store.Adapter.CartListAdapter;
import com.example.store.Adapter.CartListAdapter2;
import com.example.store.R;
import com.example.store.models.Order;
import com.example.store.models.Products;

import java.io.Serializable;
import java.util.List;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class CartActivity2 extends AppCompatActivity {
    String product_code;

    private ListView cartListView;
    private TextView emptyCartText;
    private TextView totalAmountText;
    private CartListAdapter2 cartListAdapter;
    private List<Products> cartItems;

    private TextView customerName;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart2);

        // Initialize views
        cartListView = findViewById(R.id.view3);
        emptyCartText = findViewById(R.id.emptyTxt);
        totalAmountText = findViewById(R.id.totalTxt);

        Intent intent = getIntent();
        // Retrieve cart items passed from ProductDetails
        cartItems = (List<Products>) getIntent().getSerializableExtra("cartItems");
        product_code = intent.getStringExtra("productCode");

        // Check if the cart is empty or not
        if (cartItems != null && !cartItems.isEmpty()) {
            // Hide the "empty cart" message
            emptyCartText.setVisibility(View.GONE);

            // Set up the adapter for the ListView
            cartListAdapter = new CartListAdapter2(this, cartItems);
            cartListView.setAdapter(cartListAdapter);

            // Update the total amount
            updateTotalAmount();
        } else {
            // Show the "empty cart" message
            emptyCartText.setVisibility(View.VISIBLE);
        }

        ImageView backBtn = findViewById(R.id.backBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartActivity2.this, MainActivity.class));
            }
        });

        customerName = findViewById(R.id.customerName);
        String fullName = loadGreeting();


        databaseReference = FirebaseDatabase.getInstance().getReference("orders");

        Button orderButton = findViewById(R.id.orderButton);
        orderButton.setOnClickListener(v -> placeOrder(fullName));
    }

    /**
     * Calculates and updates the total amount of the cart.
     */
    private void updateTotalAmount() {
        double total = 0.0;

        if (cartItems != null) {
            for (Products product : cartItems) {
                if (product.getProductPrice() != null) {
                    total += product.getProductPrice();
                } else {
                    Log.e("CartActivity2", "Warning: Null product price encountered.");
                }
            }
        } else {
            Log.e("CartActivity2", "cartItems is null!");
        }

        try {
            totalAmountText.setText(String.format(Locale.US, "Total: $%.2f", total));
        } catch (Exception e) {
            Log.e("CartActivity2", "Error formatting total amount: " + e.getMessage());
            totalAmountText.setText("Total: $" + total); // Fallback
        }
    }


    private String loadGreeting() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
        String fullName = sharedPreferences.getString("FullName", "Emma"); // Default to "Emma"
        customerName.setText(fullName);
        return fullName;
    }

    private void placeOrder(String fullName) {
        // Λήψη των δεδομένων του πελάτη (π.χ. από ένα EditText)

        // Λήψη των προϊόντων από το καλάθι
        List<Products> cartItems = (List<Products>) getIntent().getSerializableExtra("cartItems");

        // Δημιουργία ενός μοναδικού ID για την παραγγελία
        String orderId = databaseReference.push().getKey();

        // Λήψη του τρέχοντος timestamp
        String timestamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
                .format(new Date());

        // Αποστολή κάθε προϊόντος ως ξεχωριστή παραγγελία
        for (Products product : cartItems) {
            Order order = new Order(fullName, product_code, timestamp);

            // Αποστολή στη Firebase
            databaseReference.child(orderId).setValue(order)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(CartActivity2.this, "Η παραγγελία καταχωρήθηκε!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(CartActivity2.this, "Σφάλμα κατά την καταχώρηση της παραγγελίας.", Toast.LENGTH_SHORT).show();
                    });
        }
    }
}