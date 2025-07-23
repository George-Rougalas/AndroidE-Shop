package com.example.store.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.example.store.Activity.ProductDetails;
import com.example.store.R;
import com.example.store.models.Products;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Products> productsList;
    private Context context;


    public ProductAdapter(Context context,List<Products> productsList) {
        this.context = context;
        this.productsList = productsList;
    }


    @NonNull
    @Override
    public  ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.products_row_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductViewHolder holder, int position) {
        Products product = productsList.get(position);

        holder.productName.setText(product.getProductName());
        holder.productPrice.setText("$ " + product.getProductPrice());
        holder.productQty.setText(product.getProductQty());
        holder.productCategory.setText(product.getProductCategory());

        int drawableResourceId = context.getResources().getIdentifier(productsList.get(position).getImageUrl(),
                "drawable", holder.itemView.getContext().getPackageName());

        Glide.with(context)
                .load(product.getImageUrl())
                .placeholder(R.drawable.prod1) // Optional placeholder image
                .error(R.drawable.prod1) // Optional error image
                .transform(new GranularRoundedCorners(30, 30, 0, 0))
                .into(holder.productImage);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), ProductDetails.class);
            intent.putExtra("object", productsList.get(position));
            intent.putExtra("productImage", product.getImageUrl());
            intent.putExtra("productName", product.getProductName());
            intent.putExtra("productCode", product.getProductCode());
            intent.putExtra("productPrice", product.getProductPrice());
            intent.putExtra("productQty", product.getProductQty());
            intent.putExtra("productCategory", product.getProductCategory());
            intent.putExtra("productDescription", product.getProductDescription());
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder{

        ImageView productImage;
        TextView productName, productCategory, productQty, productPrice;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.prod_image);
            productName = itemView.findViewById(R.id.name);
            productCategory = itemView.findViewById(R.id.category);
            productQty = itemView.findViewById(R.id.prod_qty);
            productPrice = itemView.findViewById(R.id.price);
        }
    }
}
