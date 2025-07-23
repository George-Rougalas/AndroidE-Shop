package com.example.store.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.example.store.Helper.ManagementCart;
import com.example.store.R;
import com.example.store.models.Products;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductDetails extends AppCompatActivity {
    private LinearLayout backBtn, plusCartBtn;
    private Button cartBtn, imageCartBtn;
    private ImageButton FaveBtn;
    private Products object;
    private int numberOrder = 1;
    private ManagementCart managementCart;
    private ImageView itemPic;

    private List<Products> cartItems = new ArrayList<>();

    private TextView nameTextView, categoryTextView, priceTextView, descriptionTextView;

    String imageUrl, name, product_code, qty, category, product_description;
    Double price;
    int number_in_cart;
    int updatedCount;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productdetails);
        imageCartBtn = findViewById(R.id.imageCartBtn);
        Log.d("Debug", "imageCartBtn: " + imageCartBtn);
        managementCart = new ManagementCart(this);
        initView();

        // Get data from intent
        Intent intent = getIntent();
        if (intent != null) {
            imageUrl = intent.getStringExtra("productImage");
            name = intent.getStringExtra("productName");
            product_code = intent.getStringExtra("productCode");
            price = intent.getDoubleExtra("productPrice", 0.0);
            qty = intent.getStringExtra("productQty");
            category = intent.getStringExtra("productCategory");
            product_description = intent.getStringExtra("productDescription");

            // Set data to views
            nameTextView.setText(name);
            priceTextView.setText(String.format("$ %s", price));
            categoryTextView.setText(category);
            descriptionTextView.setText(product_description);

            // Load image using Glide
            Glide.with(this).load(imageUrl).into(itemPic);
        }

        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!cartItems.isEmpty()) {
                    number_in_cart = updatedCount;
                    Intent intent = new Intent(ProductDetails.this, CartActivity.class);
                    intent.putExtra("object", (ArrayList<Products>) cartItems);
                    intent.putExtra("productName", name);
                    intent.putExtra("productCode", product_code);
                    intent.putExtra("productPrice", price);
                    intent.putExtra("count", number_in_cart);
                    intent.putExtra("productDescr", product_description);
                    startActivity(intent);
                } else {
                    Toast.makeText(ProductDetails.this, "No products selected", Toast.LENGTH_SHORT).show();
                }
            }
        });

        FaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //my code
                boolean wished;
                ImageButton imageButton3;
                SharedPreferences preferences = getSharedPreferences("wish", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                if (preferences.getBoolean(name, false)) {
                    //if item is already in favourites, unfavourite it
                    wished = false;
                    imageButton3 = findViewById(R.id.FaveBtn);
                    imageButton3.setImageResource(android.R.drawable.btn_star_big_off);
                } else {
                    //if item is not in favourites, favourite it
                    wished = true;
                    imageButton3 = findViewById(R.id.FaveBtn);
                    imageButton3.setImageResource(android.R.drawable.btn_star_big_on);
                }
                editor.putBoolean(name, wished);
                editor.apply();
            }
        });


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProductDetails.this, MainActivity.class));
            }
        });

        imageCartBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                addToCart(v);
            }
        });

        // Load settings
        loadSettings();
    }

    private void initView() {

        nameTextView = findViewById(R.id.textView11);
        categoryTextView = findViewById(R.id.textView7);
        priceTextView = findViewById(R.id.textView12);
        itemPic = findViewById(R.id.itemPic);
        descriptionTextView = findViewById(R.id.textView13);
        backBtn = findViewById(R.id.imageBackBtn);
        cartBtn = findViewById(R.id.imageCartBtn);
        FaveBtn = findViewById(R.id.FaveBtn);

    }

    public void incrementProductCount(View view) {
        // Locate the TextView that shows the product count
        TextView productCountTextView = findViewById(R.id.textView14);

        // Get the current count from the TextView
        int currentCount = Integer.parseInt(productCountTextView.getText().toString());

        // Increment the count
        updatedCount = currentCount + 1;

        // Update the TextView with the new count
        productCountTextView.setText(String.valueOf(updatedCount));


        Products product = new Products(imageUrl, number_in_cart,category,product_code,name, price, qty, product_description, 0.0, 0.0); // Replace with real data
        cartItems.add(product);
    }


    public void decrementProductCount(View view) {
        // Locate the TextView that shows the product count
        TextView productCountTextView = findViewById(R.id.textView14);

        // Get the current count from the TextView
        int currentCount = Integer.parseInt(productCountTextView.getText().toString());

        updatedCount = currentCount - 1;

        if(currentCount == 0){
            productCountTextView.setText(String.valueOf(currentCount));
        }
        else {
            productCountTextView.setText(String.valueOf(updatedCount));
            cartItems.remove(cartItems.size() - 1);
        }

        // Optionally, reload the activity to reflect other changes (if needed)
        // finish();
        // startActivity(getIntent());
    }

    private void openCartActivity() {
        Intent intent = new Intent(ProductDetails.this, CartActivity.class);
        intent.putExtra("cartItems", (Serializable) cartItems);
        startActivity(intent);
    }

    public void addToCart(View view) {
        if (!cartItems.isEmpty()) {
            Intent intent = new Intent(ProductDetails.this, CartActivity2.class);
            intent.putExtra("cartItems", (Serializable) cartItems); // Ensure Products implements Serializable!
            intent.putExtra("productCode", product_code);
            startActivity(intent);
        } else {
            Toast.makeText(this, "No products selected", Toast.LENGTH_SHORT).show();
        }
    }
    public void loadSettings(){
        SharedPreferences settingsPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
        //background color
        String backgroundColor = settingsPreferences.getString("BackgroundColor", "Pink");
        ConstraintLayout constraintView = findViewById(R.id.constraintView);
        switch (backgroundColor) {
            case "Pink":
                constraintView.setBackgroundResource(R.drawable.background_pink);
                break;
            case "Blue":
                constraintView.setBackgroundResource(R.drawable.background_blue);
                break;
            case "Green":
                constraintView.setBackgroundResource(R.drawable.background_green);
                break;
        }
        //font size
        String text_size = settingsPreferences.getString("FontSize","Medium");
        int size_value = 14;
        switch (text_size) {
            case "Small":
                size_value = 10;
                break;
            case "Medium":
                //size_value = 14;
                break;
            case "Big":
                size_value = 20;
                break;
        }
        TextView textView = findViewById(R.id.textView13);
        textView.setTextSize(size_value);
    }
}
