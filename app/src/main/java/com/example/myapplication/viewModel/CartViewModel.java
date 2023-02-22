package com.example.myapplication.viewModel;

import android.app.Application;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.Cart;
import com.example.myapplication.repository.Repository;

import java.util.List;

public class CartViewModel extends AndroidViewModel {
    private Repository repository;
    private LiveData<List<Cart>> carts;
    private MutableLiveData<Double> basketTotal = new MutableLiveData<>();
    private final static MutableLiveData<Double> itemsTotal = new MutableLiveData<>();
    private final double DELIVERY = 10.0;
    private final double TAX = 0.0;

    public CartViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        carts = repository.getCart();
    }


    public void insertCart(Cart cart){
        repository.insertCart(cart);
    }

    public void updateCart(String productId , int updatedPrice , int productQuantity){
        repository.updateCart(productId , updatedPrice , productQuantity);
    }

    public LiveData<List<Cart>> getCarts(){
        return carts;
    }


//    @BindingAdapter("android:text")
//    public void setItemsTotal(Double itemsTotal) {
//        this.itemsTotal = itemsTotal;
//    }


    @BindingAdapter("android:text")
    public static void setBasketTotal(TextView textView , double basketTotal) {
        Log.d("updatedBasket" , String.valueOf(basketTotal));
    }

    public MutableLiveData<Double> getBasketTotal() {
        return basketTotal;
    }



    @BindingAdapter("android:text")
    public static void setItemsTotal(TextView textView , double itemsTotal1) {
//        if (itemsTotal1 == 0.0)
//            return;
//        itemsTotal.setValue(itemsTotal1);
        Log.d("updated" , String.valueOf(itemsTotal1));
//        textView.setText(String.valueOf(itemsTotal));
    }


    public MutableLiveData<Double> getItemsTotal() {
        return itemsTotal;
    }

    public double getDELIVERY() {
        return DELIVERY;
    }

    public double getTAX() {
        return TAX;
    }
}
