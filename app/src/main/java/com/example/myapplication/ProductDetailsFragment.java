package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.os.Vibrator;
import android.util.Log;

import com.example.myapplication.viewModel.CartViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;


public class ProductDetailsFragment extends Fragment {
    TextView productName , priceTxt , quantityTxt;
    ImageView productImg;
    Button productBtn , minusBtn , plusBtn;
    String productText;
    String productImage;
    int productPrice;
    FirebaseDatabase root;
    FirebaseFirestore firestore;
    String pName;
    String pId;
    int pPrice;
    String pImage;
    DatabaseReference reference , reference2;
    public static final String fileName = "UserData";
    public static final String keyName = "username";
    public static final String keyMobile = "mobile";
    public static final String keyEmail = "email";
    public static final String keyPassword = "password";
    SharedPreferences sharedPreferences;
    private String ProductId;
    private String ProductImage;
    private String ProductName;
    private String ProductPrice;
    private String ProductQuantity;
    Toolbar toolbar;
    private Vibrator vibrator;
    private ProgressBar progressBar;
    String userMobile;
    private String UserN;
    private String userEmail;
    User user;
    GoogleSignInAccount signInAccount;
    CartViewModel cartViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_product_details, container, false);

        productName = view.findViewById(R.id.productTxt);
        productImg = view.findViewById(R.id.productImg);
        productBtn = view.findViewById(R.id.cartBtn);
        priceTxt = view.findViewById(R.id.priceTxt);
        plusBtn = view.findViewById(R.id.plusBtn);
        minusBtn = view.findViewById(R.id.minusBtn);
        quantityTxt = view.findViewById(R.id.quantityTxt);
        progressBar = view.findViewById(R.id.progressBar);

        vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);

        firestore = FirebaseFirestore.getInstance();


        toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        cartViewModel = new ViewModelProvider(getActivity()).get(CartViewModel.class);

        signInAccount = GoogleSignIn.getLastSignedInAccount(getContext());
        if (signInAccount != null) {
            UserN = signInAccount.getDisplayName();
            userEmail = signInAccount.getEmail();
        }

        RoomDb roomDb = RoomDb.getDatabase(getContext());
        user = roomDb.userDao().getUser();
//        root = FirebaseDatabase.getInstance();
//        reference = root.getReference("Users").child(mobile).child("Cart");
//        Bundle bundle = this.getArguments();

        productText = getArguments().getString("name");
        productImage = getArguments().getString("image");
        productPrice = getArguments().getInt("price");

        Log.d("name" , "name: " + productText);

        productName.setText(productText);
        priceTxt.setText(String.valueOf(productPrice) + " EGP");
        Glide.with(this).load(productImage).into(productImg);

        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantityText = quantityTxt.getText().toString();

                int quantity = Integer.parseInt(quantityText);
                quantity ++;
                quantityTxt.setText(String.valueOf(quantity));
            }
        });

        minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String quantityText = quantityTxt.getText().toString();

                int quantity = Integer.parseInt(quantityText);
                if (quantity == 0)
                    return;
                else {
                    quantity --;
                    quantityTxt.setText(String.valueOf(quantity));
                }
            }
        });


//        productBtn.setOnTouchListener(new HapticListener());
        productBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (signInAccount == null) {
                    progressBar.setVisibility(View.VISIBLE);
                    firestore.collection("Products").document(productText).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                vibrator.vibrate(50);

                                ProductId = task.getResult().get("ProductId").toString();
                                Log.d("id", ProductId);


                                RoomDb roomDb = RoomDb.getDatabase(getContext());

                                Product product = roomDb.productDao().getProductById(ProductId);

                                List<Cart> cartList = roomDb.cartDao().getCartByMobile(user.getMobile());

                                for (int i = 0; i < cartList.size(); i++) {
                                    Log.d("Cartbefore", "Cart Price: " + cartList.get(i).getProductPrice());
                                }

                                pName = product.getProductName();
                                pId = product.getProductId();
                                pPrice = product.getProductPrice();
                                pImage = product.getLink();


                                Boolean exist = roomDb.cartDao().is_Exist(pId);

                                int price = pPrice * Integer.parseInt(quantityTxt.getText().toString());
                                int quantity = Integer.parseInt(quantityTxt.getText().toString());
                                int currentPrice = 0, currentQuantity = 0;
                                if (exist) {
                                    List<Cart> cartList2 = roomDb.cartDao().getCartByMobile(user.getMobile());
                                    for (int i = 0; i < cartList2.size(); i++) {
                                        if (cartList2.get(i).getProductName().equals(productText)) {
                                            currentPrice = cartList2.get(i).getProductPrice();
                                            currentQuantity = cartList2.get(i).getProductQuantity();
                                        }
                                    }
                                    price += currentPrice;
                                    quantity += currentQuantity;
                                    cartViewModel.updateCart(pId, price, quantity);
                                } else {
                                    Cart cart = new Cart(pId, pImage, pName, price, quantity, user.getMobile());
                                    cartViewModel.insertCart(cart);

//                                    roomDb.cartDao().insertRecord(cart);
                                }
                                Log.d("exist ", String.valueOf(exist));

                                List<Cart> cartList2 = roomDb.cartDao().getCartByMobile(user.getMobile());

                                for (int i = 0; i < cartList2.size(); i++) {
                                    Log.d("CartAfter", "Cart Price: " + cartList2.get(i).getProductPrice());
                                }

                                Navigation.findNavController(view).navigate(R.id.action_productDetailsFragment_to_homeFragment);
                            }

                        }
                    });
                }
                else {
                    progressBar.setVisibility(View.VISIBLE);
                    firestore.collection("Products").document(productText).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                vibrator.vibrate(50);

                                ProductId = task.getResult().get("ProductId").toString();
                                Log.d("id", ProductId);


                                RoomDb roomDb = RoomDb.getDatabase(getContext());

                                Product product = roomDb.productDao().getProductById(ProductId);

                                List<GoogleUserCart> cartList = roomDb.googleCartDao().getCartByEmail(userEmail);

                                for (int i = 0; i < cartList.size(); i++) {
                                    Log.d("Cartbefore", "Cart Price: " + cartList.get(i).getProductPrice());
                                }

                                pName = product.getProductName();
                                pId = product.getProductId();
                                pPrice = product.getProductPrice();
                                pImage = product.getLink();


                                Boolean exist = roomDb.googleCartDao().is_Exist(pId);

                                int price = pPrice * Integer.parseInt(quantityTxt.getText().toString());
                                int quantity = Integer.parseInt(quantityTxt.getText().toString());
                                int currentPrice = 0, currentQuantity = 0;
                                if (exist) {
                                    List<GoogleUserCart> cartList2 = roomDb.googleCartDao().getCartByEmail(userEmail);
                                    for (int i = 0; i < cartList2.size(); i++) {
                                        if (cartList2.get(i).getProductName().equals(productText)) {
                                            currentPrice = cartList2.get(i).getProductPrice();
                                            currentQuantity = cartList2.get(i).getProductQuantity();
                                        }
                                    }
                                    price += currentPrice;
                                    quantity += currentQuantity;
                                    roomDb.googleCartDao().updateProduct(pId, price, quantity);
                                } else {
                                    GoogleUserCart cart = new GoogleUserCart(pId, pImage, pName, price, quantity, userEmail);
                                    roomDb.googleCartDao().insertRecord(cart);
                                }
                                Log.d("exist ", String.valueOf(exist));

                                List<GoogleUserCart> cartList2 = roomDb.googleCartDao().getCartByEmail(userEmail);

                                for (int i = 0; i < cartList2.size(); i++) {
                                    Log.d("CartAfter", "Cart Price: " + cartList2.get(i).getProductPrice());
                                }

                                Navigation.findNavController(view).navigate(R.id.action_productDetailsFragment_to_homeFragment);
                            }

                        }
                    });
                }
            }
        });

        return view;
    }
}