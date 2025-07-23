package com.example.store.Activity;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.store.R;
import com.example.store.models.Products;
import com.example.store.models.Products;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WishlistActivity extends AppCompatActivity implements LocationListener {
    private DatabaseReference databaseReference;
    private List<Products> productList;
    //Firebase database;
//    List<String> productCodes;
//    List<String> productNames;
    List<Products> wished;
//    List<Double> storeLat;
//    List<Double> storeLong;
    Spinner spinner;
    SharedPreferences preferences;
    LocationManager locationManager;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private static final String TAG = "ProductDetails"; //put the target page here //CHANGE TARGET IN NOTIFICATION
    private static final String CHANNEL_ID = "Store_Notification";
    private static final int NOTIFICATION_ID = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_wishlist);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //Load settings
        loadSettings();
        //load products + databse connection
        FirebaseApp.initializeApp(this);//useless?
        databaseReference = FirebaseDatabase.getInstance().getReference("Products");
        productList = new ArrayList<>();
        loadProductData();

        //initialize location manager and preferences
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        preferences = getSharedPreferences("wish", MODE_PRIVATE);
        spinner = findViewById(R.id.spinner); //add the correct ID

//
//        //Location Beginners
//        storeLat  = new ArrayList<>();
//        storeLong  = new ArrayList<>();
//        productCodes  = new ArrayList<>();
//        productNames  = new ArrayList<>();
//        //wished  = new ArrayList<>(); //initialized in checkWishes

//        //Wished items
//        productCodes = database.getColumn("productCodes");
//        productNames = database.getColumn("productNames");

        //the whole point
        checkWishes();

//        //TESTING
//        productCodes = List.of("product1", "product2", "product3");
//        productNames = List.of("product1", "product2", "product3");
//        //TESTING

        //Store information
        //storeLat = database.getColumn(Store_Lat));
        //storeLong = database.getColumn(Store_Long)); //fetch double // obviously in multiple values not length=1

//        //TESTING
//        storeLat = List.of(42.65, 50.4, 31.0);
//        storeLong = List.of(-130.2, -89.1, -100.01); //fetch double // obviously in multiple values not length=1
//        //TESTING

        // Initialize the permission request launcher or whatever
        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        // Permission is granted. Continue the action or workflow in your app.
                        Log.d(TAG, "POST_NOTIFICATIONS permission granted");
                    } else {
                        // Explain to the user that the feature is unavailable because the
                        // feature requires a permission that the user has denied.
                        Log.d(TAG, "POST_NOTIFICATIONS permission denied");
                    }
                }
        );
        createNotificationChannel();
        //I can't ask for multiple Permissions in one action, so sneak them whenever possible
        askNotificationPermission();
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
                        //data might be numbers, check, CHANGE ME
                        String imageUrl = productSnapshot.child("imageUrl").getValue(String.class);
                        String prodName = productSnapshot.child("productName").getValue(String.class);
                        String prodCategory = productSnapshot.child("productCategory").getValue(String.class);
                        Double prodPrice = productSnapshot.child("productPrice").getValue(Double.class);
                        String prodQty = productSnapshot.child("productQty").getValue(String.class);
                        String prodDescription = productSnapshot.child("productDescription").getValue(String.class);
                        String prodCode = productSnapshot.child("productCode").getValue(String.class);
                        Double storeLat = productSnapshot.child("storeLat").getValue(Double.class);
                        Double storeLong = productSnapshot.child("storeLong").getValue(Double.class);


                        // Create a new Product object
                        Products product = new Products(imageUrl, 0, prodCategory, prodCode,
                                prodName, prodPrice, prodQty, prodDescription, storeLat, storeLong);
                        // Add to the list
                        productList.add(product);
                    }

                }
                else {
                    Toast.makeText(WishlistActivity.this, "Null Entity " , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(WishlistActivity.this, "Error loading data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Firebase", "Data loading cancelled: " + error.getMessage());
            }
        });
    }

    public void checkStores(View view){ //for button labeled "find nearby stores"
        //check location permission, then continue
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},123);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                0, 0, this);
        //get current location
        Location myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        //what store you picked
        String selectedName = spinner.getSelectedItem().toString();
        Products selectedProduct = null;
        for (Products productList: productList){
            if (productList.getProductName().equals(selectedName))
                selectedProduct = productList;
        }

        Location storeLocation = new Location("");
        storeLocation.setLatitude(selectedProduct.getStoreLat());
        storeLocation.setLongitude(selectedProduct.getStoreLong());

        double new_distance = storeLocation.distanceTo(myLocation);
        //send notification
        sendNotification(new_distance, selectedProduct);
    }
    private void sendNotification(double distance, Products selectedProduct) {
        //here we put the target activity
        Intent intent = new Intent(this, ProductDetails.class); //CHANGE ME / ADD
        //test vigorously
        //intent.putExtra("object", selectedProduct.get(position)); //lame
        intent.putExtra("productImage", selectedProduct.getImageUrl());
        intent.putExtra("productName", selectedProduct.getProductName());
        intent.putExtra("productPrice", selectedProduct.getProductPrice());
        intent.putExtra("productQty", selectedProduct.getProductQty());
        intent.putExtra("productCategory", selectedProduct.getProductCategory());
        intent.putExtra("productDescription", selectedProduct.getProductDescription());
        //end of test
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        //notification creator
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.location_notification) // Replace with your icon
                .setContentTitle("Store located!")// Replace with your notification title
                .setContentText("There is a store that sells " + selectedProduct.getProductName() + " at " + distance + " kilometers.")// Replace with your notification text
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        //notification sender
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            askNotificationPermission();
            return;
        }
        notificationManager.notify(NOTIFICATION_ID, builder.build());
        //no limit on how far the user can be from a store,
        // possible to implement but then we can't show off the notification
    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Store Channel";
            String description = "Notifications for nearby Stores";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    private void askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
                // FCM SDK (and your app) can post notifications.
                Log.d(TAG, "POST_NOTIFICATIONS permission already granted");
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }
    public void checkWishes() {
        //initialize wished
//        wished = new ArrayList<>();
//        for (int i = 0; i < productCodes.size(); i++)
//            wished.add(
//                    preferences.getBoolean(productCodes.get(i), false)
//            );
//
        //add entries to Spinner
        ArrayList<String> arrayList = new ArrayList<>();
        boolean noWishes= true;
        for (Products productList: productList){
            if (preferences.getBoolean(productList.getProductName(), false)) {
                arrayList.add(productList.getProductName());
                noWishes = false;
            }
        }
//        for (int i = 0; i < productNames.size(); i++) {
//            if (preferences.getBoolean(productCodes.get(i), false)) {
//                arrayList.add(productNames.get(i));
//                noWishes = false;
//            }
//        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        //update UI
        if (noWishes) {
            //set the text
            TextView textView = findViewById(R.id.textView3);//CHANGE ME
            textView.setVisibility(View.VISIBLE);
            //set the spinner
            spinner.setVisibility(View.INVISIBLE);
            //set the main button
            Button button = findViewById(R.id.button2);//CHANGE ME
            button.setVisibility(View.INVISIBLE);
        } else {
            //set the text
            TextView textView = findViewById(R.id.textView3);//CHANGE ME
            textView.setVisibility(View.INVISIBLE);
            //set the spinner
            spinner.setVisibility(View.VISIBLE);
            //set the button
            Button button = findViewById(R.id.button2);//CHANGE ME
            button.setVisibility(View.VISIBLE);
        }
    }
    public void checkWishes(View view) {
        checkWishes();
    }
    public void goBack(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }
    @Override
    public void onLocationChanged(@NonNull Location location) {//DON'T DELETE ME
    }
    public void loadSettings(){
        SharedPreferences settingsPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
        //background color
        String backgroundColor = settingsPreferences.getString("BackgroundColor", "Pink");
        ConstraintLayout constraintView = findViewById(R.id.constraintView2);
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
//        //font size
//        String text_size = settingsPreferences.getString("FontSize","Medium");
//        int size_value = 14;
//        switch (text_size) {
//            case "Small":
//                size_value = 10;
//                break;
//            case "Medium":
//                //size_value = 14;
//                break;
//            case "Big":
//                size_value = 20;
//                break;
//        }
//        TextView textView = findViewById(R.id.textView13);
//        textView.setTextSize(size_value);
    }
}