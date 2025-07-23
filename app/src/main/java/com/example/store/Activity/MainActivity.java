package com.example.store.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.store.Adapter.ProductAdapter;
import com.example.store.Adapter.ProductCategoryAdapter;
import com.example.store.R;
import com.example.store.databinding.ActivityMainBinding;
import com.example.store.models.ProductCategory;
import com.example.store.models.Products;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    private ProductAdapter productAdapter;


    private AppBarConfiguration mAppBarConfiguration;

    FirebaseDatabase database = FirebaseDatabase.getInstance();

    ProductCategoryAdapter productCategoryAdapter;
    RecyclerView productCatRecycler, prodItemRecycler;

    private TextView textView5;
    private DatabaseReference databaseReference;

    private List<Products> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Load settings
        loadSettings();

        FirebaseApp.initializeApp(this);

//        List<ProductCategory> productCategoryList = new ArrayList<>();
//        productCategoryList.add(new ProductCategory(2, "Most Popular"));
//        productCategoryList.add(new ProductCategory(3, "Most Expensive"));
//        productCategoryList.add(new ProductCategory(4, "Cheapest"));
//
//        setProductRecycler(productCategoryList);

//        setProdItemRecycler();

        textView5 = findViewById(R.id.textView5);

        // Check if user is logged in
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (auth.getCurrentUser() != null) {
            // Retrieve the user's display name
            String displayName = auth.getCurrentUser().getDisplayName();

            // Fallback if the display name is null
            if (displayName == null || displayName.isEmpty()) {
                displayName = "User";
            }

            // Update the TextView
            textView5.setText("Hello, " + displayName + "!");
        } else {
            // If no user is logged in, navigate to the login screen or show default text
            textView5.setText("Hello, Guest!");
            startActivity(new Intent(MainActivity.this, loginActivity.class));
            finish();
        }


        bottomNavigation();

        loadGreeting();

        databaseReference = FirebaseDatabase.getInstance().getReference("Products");

        prodItemRecycler = findViewById(R.id.product_recycler);
        prodItemRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Initialize product list and adapter
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(this, productList);
        prodItemRecycler.setAdapter(productAdapter);

        // Load products from Firebase
        loadProductData();
    }

    private void bottomNavigation() {
//        LinearLayout homeBtn = findViewById(R.id.homeBtn);
//        LinearLayout cartBtn = findViewById(R.id.cartBtn);
        LinearLayout settingsBtn = findViewById(R.id.settingsBtn);
        LinearLayout wishListBtn = findViewById(R.id.wishListBtn);

        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
        });

        wishListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, WishlistActivity.class));
            }
        });
//
//        homeBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, MainActivity.class));
//            }
//        });
//
//        cartBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, CartActivity2.class));
//            }
//        });

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


//    private void setProductRecycler(List<ProductCategory> productCategoryList){
//
//        productCatRecycler = findViewById(R.id.cat_recycler);
//        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
//        productCatRecycler.setLayoutManager(layoutManager);
//        productCategoryAdapter = new ProductCategoryAdapter(this, productCategoryList);
//        productCatRecycler.setAdapter(productCategoryAdapter);
//
//    }

    private void setProdItemRecycler(){
        ArrayList<Products> productsList = new ArrayList<>();

        productsList.add(new Products("1", 0,"Juice","1","Cherry Juice", 5.0, "500 ml","", 0.0, 0.0 ));
        productsList.add(new Products("2", 0 ,"Juice","2","Mango Juice",5.0, "500 ml","", 0.0, 0.0));
        productsList.add(new Products("3", 0 ,"Juice","3","Orange Juice",5.0, "500 ml","", 0.0, 0.0));

//        databaseReference = FirebaseDatabase.getInstance().getReference("products");
//        fetchProductsFromFirebase();
//
        prodItemRecycler = findViewById(R.id.product_recycler);
        prodItemRecycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        productAdapter = new ProductAdapter( this,productsList);
        prodItemRecycler.setAdapter(productAdapter);


    }


    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the greeting in case the name was updated
        loadGreeting();
    }

    private void loadGreeting() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
        String fullName = sharedPreferences.getString("FullName", "User"); // Default to "Emma"
        textView5.setText("Hello " + fullName);
    }

    private void loadProductData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();
                if (snapshot.exists()) {
                    int i= 0;
                    for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                        // Extract data from Firebase
                        String imageUrl = productSnapshot.child("imageUrl").getValue(String.class);
                        String prodName = productSnapshot.child("productName").getValue(String.class);
                        String productCode = productSnapshot.child("productCode").getValue(String.class);
                        String prodCategory = productSnapshot.child("productCategory").getValue(String.class);
                        Double prodPrice = productSnapshot.child("productPrice").getValue(Double.class);
                        String prodQty = productSnapshot.child("productQty").getValue(String.class);
                        String prodDescription = productSnapshot.child("productDescription").getValue(String.class);
                        Double storeLat = productSnapshot.child("storeLat").getValue(Double.class);
                        Double storeLong = productSnapshot.child("storeLong").getValue(Double.class);

                        // Create a new Product object
                        Products product = new Products(imageUrl,0, prodCategory, productCode, prodName, prodPrice , prodQty, prodDescription, storeLat, storeLong);

                        // Add to the list
                        productList.add(product);
                    }

                    // Notify the adapter that data has changed
                    productAdapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(MainActivity.this, "Null Entity " , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Error loading data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Firebase", "Data loading cancelled: " + error.getMessage());
            }
        });
    }
    public void loadSettings(){
        SharedPreferences settingsPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
        //background color
        String backgroundColor = settingsPreferences.getString("BackgroundColor", "Pink");
        ConstraintLayout constraintView = findViewById(R.id.constraintView3);
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
        TextView textView = findViewById(R.id.textView6);
        textView.setTextSize(size_value);
    }

}