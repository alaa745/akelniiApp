package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myapplication.databinding.FragmentCartBinding;
import com.example.myapplication.viewModel.CartViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.List;


public class CartFragment extends Fragment {
    RecyclerView recyclerView;
    RecyclerView recyclerView2;
    FinalAdapter adapter;
    public static final String fileName = "UserData";
    public static final String keyName = "username";
    public static final String keyMobile = "mobile";
    public static final String keyEmail = "email";
    public static final String keyPassword = "password";
    SharedPreferences sharedPreferences;
    double itemsTotal;
    private CartViewModel cartViewModel;
    double basketTotal;
    Button checkOut;
    EditText promoCode;
    final double DELIVERY = 10.0 , TAX = 0.0;
    Toolbar toolbar;
    TextView itemTotalText, delivery , tax , totalPrice;
    String userMobile , userEmail;
    User user;
    boolean promoApplied = false;
    TextView submit;
    GoogleSignInAccount signInAccount;
    FragmentCartBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater , R.layout.fragment_cart , container , false);
        View view = binding.getRoot();
//        View view = inflater.inflate(R.layout.fragment_cart, container, false);


        itemTotalText = view.findViewById(R.id.itemsText);
        delivery = view.findViewById(R.id.DeliveryPrice);
        tax = view.findViewById(R.id.TaxPrice);
        promoCode = view.findViewById(R.id.promoCode);
        submit = view.findViewById(R.id.submit);
        totalPrice = view.findViewById(R.id.TotalPrice);
        toolbar = view.findViewById(R.id.toolbar);
        checkOut = view.findViewById(R.id.checkOutBtn);

        cartViewModel = new ViewModelProvider(getActivity()).get(CartViewModel.class);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView = view.findViewById(R.id.finalRv);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        FinalAdapter finalAdapter = new FinalAdapter();
        recyclerView.setAdapter(finalAdapter);
        RoomDb roomDb = RoomDb.getDatabase(getContext());
        user = roomDb.userDao().getUser();
        calcBasket();
        cartViewModel.getCarts().observe(getViewLifecycleOwner(), new Observer<List<Cart>>() {
            @Override
            public void onChanged(List<Cart> carts) {
                finalAdapter.setCartList(carts);
            }
        });

        cartViewModel.getItemsTotal().observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double newItemsTotal) {
                Log.d("itemsTotal" , String.valueOf(newItemsTotal.doubleValue()));
                binding.itemsText.setText(String.valueOf(newItemsTotal.doubleValue()));
            }
        });
        promoCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                submit.setTextColor(Color.parseColor("#FFA8A8B3"));
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                submit.setTextColor(Color.parseColor("#FF5722"));
                if (promoCode.getText().toString().isEmpty())
                    beforeTextChanged(charSequence , i , i1 , i2);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        sharedPreferences = getContext().getSharedPreferences(fileName, Context.MODE_PRIVATE);
        userMobile = sharedPreferences.getString(keyMobile , "Null");

        signInAccount = GoogleSignIn.getLastSignedInAccount(getContext());
        if (signInAccount != null){
            userEmail = signInAccount.getEmail();
        }
        ((AppCompatActivity)requireActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

//        getDataFromFirebase(view);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                while (!promoApplied)
                    applyPromoCode();
            }
        });
        return view;
    }

    public void applyPromoCode(){
        promoApplied = true;
        double promo = 20.0/100.0;
        double discount = basketTotal * promo;
        basketTotal = basketTotal - discount;
        Log.d("discount" , String.valueOf(discount));
        Log.d("total" , String.valueOf(basketTotal));
        totalPrice.setText(String.valueOf(basketTotal) + " EGP");
        return;
    }
    public void calcBasket(){

        RoomDb roomDb1 = RoomDb.getDatabase(getContext());
        if (!isGoogleUser()) {
            List<Cart> cartList = roomDb1.cartDao().getCartByMobile(user.getMobile());
            for (int i = 0; i < cartList.size(); i++) {
                itemsTotal = cartList.get(i).getProductPrice() + itemsTotal;
            }
        }
        else {
            List<GoogleUserCart> cartList = roomDb1.googleCartDao().getCartByEmail(userEmail);
            for (int i = 0; i < cartList.size(); i++) {
                itemsTotal = cartList.get(i).getProductPrice() + itemsTotal;
            }
        }
        cartViewModel.getItemsTotal().setValue(itemsTotal);
//        cartViewModel.setItemsTotal(itemTotalText, itemsTotal);
        basketTotal = itemsTotal + DELIVERY + TAX;
//        itemTotal.setText(String.valueOf(itemsTotal) + " EGP");
        delivery.setText(String.valueOf(DELIVERY) + " EGP");
        tax.setText(TAX + " EGP");
        totalPrice.setText(String.valueOf(basketTotal) + " EGP");

    }

    public Boolean isGoogleUser(){
        if (signInAccount != null){
            return true;
        }
        else
            return false;
    }

}