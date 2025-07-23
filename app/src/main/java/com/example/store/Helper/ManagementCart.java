package com.example.store.Helper;

import android.content.Context;
import android.widget.Toast;

import com.example.store.models.Products;

import java.util.ArrayList;

public class ManagementCart {
    private Context context;
    private TinyDB tinyDB;

    public ManagementCart(Context context) {
        this.context = context;
        this.tinyDB = new TinyDB(context);
    }

    public void insertFood(Products item){
        ArrayList<Products> listPop = getListCart();
        boolean existAlready = false;
        int n = 0;
        for(int i = 0; i < listPop.size(); i++) {
            if (listPop.get(i).getProductName().equals(item.getProductName())) {
                existAlready = true;
                n = i;
                break;
            }
        }

        if(existAlready){
            listPop.get(n).setNumberinCart(item.getNumberinCart());
        }
        else{
            listPop.add(item);
        }
        tinyDB.putListObject("CartList", listPop);
        Toast.makeText(context, "Added to the Cart", Toast.LENGTH_SHORT).show();
    }

    public ArrayList<Products> getListCart() {
        return tinyDB.getListObject("CartList");
    }

    public void minusNumberItem(ArrayList<Products>listItem, int position, ChangeNumberItemsListener changeNumberItemsListener){
        if(listItem.get(position).getNumberinCart() == 1){
            listItem.remove(position);
        }
        else{
            listItem.get(position).setNumberinCart(listItem.get(position).getNumberinCart() - 1);
        }
        tinyDB.putListObject("CartList", listItem);
        changeNumberItemsListener.change();
    }

    public void plusNumberItem(ArrayList<Products>listItem, int position, ChangeNumberItemsListener changeNumberItemsListener){
        listItem.get(position).setNumberinCart(listItem.get(position).getNumberinCart() + 1);
        tinyDB.putListObject("CartList", listItem);
        changeNumberItemsListener.change();
    }

    public Double getTotalFee(){
        ArrayList<Products> listItem = getListCart();
        double fee = 0;
        for(int i = 0; i < listItem.size(); i++){
            fee += fee + (listItem.get(i).getProductPrice()*listItem.get(i).getNumberinCart());
        }
        return fee;
    }


}
