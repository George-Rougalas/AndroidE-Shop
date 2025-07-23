package com.example.store.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.store.R;
import com.example.store.models.Products;

import java.util.List;
public class CartListAdapter2 extends ArrayAdapter<Products>{

    private Context context;
    private List<Products> cartItems;

    public CartListAdapter2(Context context, List<Products> cartItems) {
        super(context, R.layout.activity_cart, cartItems);
        this.context = context;
        this.cartItems = cartItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.cart_item_layout, parent, false);

        TextView productNameTextView = rowView.findViewById(R.id.productName);
        TextView productPriceTextView = rowView.findViewById(R.id.productPrice);

        Products product = cartItems.get(position);
        productNameTextView.setText(product.getProductName());
        productPriceTextView.setText(String.format("$%.2f", product.getProductPrice()));

        return rowView;
    }
}
