package com.example.store.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.store.Adapter.CartListAdapter;
import com.example.store.Adapter.CartListAdapter2;
import com.example.store.Helper.ChangeNumberItemsListener;
import com.example.store.Helper.ManagementCart;
import com.example.store.R;
import com.example.store.models.Products;

import java.util.List;

public class CartActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private ManagementCart managementCart;
    private TextView totalFeeTxt, taxTxt, deliveryTxt, totalTxt, emptyTxt;
    private double tax;
    private ScrollView scrollView;

    private ListView cartListView;
    private TextView emptyCartText;
    private TextView totalAmountText;
    private ImageView backBtn;

    private List<Products> cartItems;

    private CartListAdapter2 cartListAdapter2;

    String count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartListView = findViewById(R.id.recylcler_view);
        emptyCartText = findViewById(R.id.emptyTxt);
        totalAmountText = findViewById(R.id.totalTxt);

//        managementCart = new ManagementCart(this);

        Intent intent = getIntent();
        if (intent != null) {
            count = intent.getStringExtra("count");
            int currentCount = Integer.parseInt(count);
        }
//        cartItems = (List<Products>) getIntent().getSerializableExtra("object");

//        if (cartItems != null && !cartItems.isEmpty()) {
//            emptyCartText.setVisibility(View.GONE); // Hide "empty cart" message
//            cartListAdapter2 = new CartListAdapter2(this, cartItems);
//            cartListView.setAdapter(cartListAdapter2);
//
//            updateTotalAmount();
//        } else {
//            emptyCartText.setVisibility(View.VISIBLE); // Show "empty cart" message
//        }


    }

    private void initList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new CartListAdapter(managementCart.getListCart(),this, new ChangeNumberItemsListener() {
            @Override
            public void change() {
                calculateCart();
            }
        });

        recyclerView.setAdapter(adapter);

        if(managementCart.getListCart().isEmpty()) {
            emptyTxt.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
        }
        else{
            emptyTxt.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
        }
    }

    private void calculateCart() {
        double percentTax = 0.03;
        double delivery = 10.0;
        tax = Math.round((managementCart.getTotalFee() * percentTax * 100.0)) / 100.0;
        double total = Math.round((managementCart.getTotalFee() + tax + delivery) * 100.0) / 100.0;
        double itemTotal = Math.round((managementCart.getTotalFee() * 100.0)) / 100.0;
        totalFeeTxt.setText("$" + itemTotal);
        taxTxt.setText("$" + tax);
        deliveryTxt.setText("$" + delivery);
        totalTxt.setText("$" + total);

    }

    private void setVariable() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        totalFeeTxt = findViewById(R.id.totalFeeTxt);
        taxTxt = findViewById(R.id.taxTxt);
        deliveryTxt = findViewById(R.id.deliveryTxt);
        totalTxt = findViewById(R.id.totalTxt);
        recyclerView = findViewById(R.id.recylcler_view);
        scrollView = findViewById(R.id.scrollView2);
        backBtn = findViewById(R.id.backBtn);
        emptyTxt = findViewById(R.id.emptyTxt);
    }

    private void updateTotalAmount() {
        double total = 0.0;
        for (Products product : cartItems) {
            total += product.getProductPrice();
        }
        totalAmountText.setText(String.format("$%.2f", total));
    }
}