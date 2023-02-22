package com.example.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.biometric.BiometricPrompt;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;


public class HomeFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener{
    DatabaseReference databaseReference;
    TextView textView , hiTxt , orderTxt;
    ArrayList<Product> images;
    RecyclerView recyclerView;
    RecyclerView recyclerView2;
    DrawerLayout drawerLayout;
    DashboardAdapter adapter;
    popularAdapter adapter2;
    BottomNavigationView bottomNavigationView;
    FirebaseFirestore firestore;
    String ProductId , ProductPrice , ProductName , ProductQuantity;
    FloatingActionButton cartBtn;
    NavigationView navigationView;
    Toolbar toolbar;
    SliderView sliderView;
    private String ProductImage;
    String googleUserEmail;
    String userName;
    User user;
    List<Cart> cartList = null;
    GoogleSignInAccount signInAccount;
    List<GoogleUserCart> googleUserCartList = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);


        hiTxt = view.findViewById(R.id.hiTxt);
        orderTxt = view.findViewById(R.id.orderTxt);
        cartBtn = view.findViewById(R.id.cartButton);
        bottomNavigationView = view.findViewById(R.id.bottom_navigation);
        recyclerView = view.findViewById(R.id.rv_D);
        recyclerView2 = view.findViewById(R.id.rv_);
        drawerLayout = view.findViewById(R.id.drawer);
        toolbar = view.findViewById(R.id.toolbar);

//        sharedPreferences = getActivity().getSharedPreferences(fileName, Context.MODE_PRIVATE);
//
//
//
//        String UserN = sharedPreferences.getString(keyName , "Null");
//        String userMail = sharedPreferences.getString(keyEmail , "Null");
//        userMobile = sharedPreferences.getString(keyMobile , "Null");


        Log.d("onCreate" , "OnCreate");

        signInAccount = GoogleSignIn.getLastSignedInAccount(getContext());
        RoomDb roomDb = RoomDb.getDatabase(getContext());
        if (signInAccount != null) {
            userName = signInAccount.getDisplayName();
            googleUserEmail = signInAccount.getEmail();
        }

        user = roomDb.userDao().getUser();
        hiTxt.setText("Hi " + user.getName().substring(0 , 1).toUpperCase()+user.getName().substring(1));

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        navigationView = view.findViewById(R.id.nav);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity() , drawerLayout , toolbar , R.string.navi_open , R.string.navi_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        firestore = FirebaseFirestore.getInstance();

        images = new ArrayList<Product>();

        databaseReference = FirebaseDatabase.getInstance().getReference("Images");

        databaseReference = FirebaseDatabase.getInstance().getReference();


        if (signInAccount == null) {
            cartList = roomDb.cartDao().getCartByMobile(user.getMobile());

            for (int i = 0; i < cartList.size(); i++) {
                Log.d("Cartbeforee", "Cart Name: " + cartList.get(i).getProductName());
            }
            if (!cartList.isEmpty()){
                cartBtn.setVisibility(View.VISIBLE);
            }
            else
                cartBtn.setVisibility(View.GONE);
        }
        else {
            googleUserCartList = roomDb.googleCartDao().getCartByEmail(googleUserEmail);

            for (int i = 0; i < googleUserCartList.size(); i++) {
                Log.d("Cartbeforee", "Cart Name: " + googleUserCartList.get(i).getProductName());
            }
            if (!googleUserCartList.isEmpty()){
                cartBtn.setVisibility(View.VISIBLE);
            }
            else
                cartBtn.setVisibility(View.GONE);
        }


        getDataFromFirebase(view);


        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_cartFragment);
            }
        });

        cartBtn.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (signInAccount == null) {
                    BadgeDrawable badgeDrawable = BadgeDrawable.create(getContext());
                    badgeDrawable.setNumber(cartList.size());
                    badgeDrawable.setBadgeTextColor(getResources().getColor(R.color.white));
//
                    //Important to change the position of the Badge
                    badgeDrawable.setHorizontalOffset(30);
                    badgeDrawable.setVerticalOffset(20);

                    BadgeUtils.attachBadgeDrawable(badgeDrawable, cartBtn, null);

                    cartBtn.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                else {
                    BadgeDrawable badgeDrawable = BadgeDrawable.create(getContext());
                    badgeDrawable.setNumber(googleUserCartList.size());
                    badgeDrawable.setBadgeTextColor(getResources().getColor(R.color.white));
//
                    //Important to change the position of the Badge
                    badgeDrawable.setHorizontalOffset(30);
                    badgeDrawable.setVerticalOffset(20);

                    BadgeUtils.attachBadgeDrawable(badgeDrawable, cartBtn, null);

                    cartBtn.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home:
                        Bundle bundle = new Bundle();
                        Fragment profileFragment = new ProfileFragment();
                        if (signInAccount == null) {
                            bundle.putString("name", user.getName());
                            bundle.putString("mobile", user.getMobile());
                            bundle.putString("email", user.getEmail());
                            bundle.putString("password", user.getPassword());
                            profileFragment.setArguments(bundle);
                        }
                        Navigation.findNavController(getView()).navigate(R.id.action_homeFragment_to_profileFragment , bundle);
                        break;

                    case R.id.bottomLogout:
                        FirebaseAuth.getInstance().signOut();
                        Navigation.findNavController(getView()).navigate(R.id.action_homeFragment_to_beginFragment);
                        break;
                }
                return true;
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("onResume" , "OnResume");

    }

    public void getDataFromFirebase(View view){

        firestore.collection("Products").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for(QueryDocumentSnapshot documentSnapshots : task.getResult()){
                        ProductId = documentSnapshots.get("ProductId").toString();
                        ProductImage = documentSnapshots.get("ProductImage").toString();
                        ProductName = documentSnapshots.get("ProductName").toString();
                        ProductPrice = documentSnapshots.get("ProductPrice").toString();
                        ProductQuantity = documentSnapshots.get("ProductQuantity").toString();


                        Product product = new Product(ProductImage , ProductName , ProductId , Integer.parseInt(ProductPrice) , Integer.parseInt(ProductQuantity) );
                        product.setProductId(ProductId);
                        product.setLink(ProductImage);
                        product.setProductName(ProductName);
                        product.setProductPrice(Integer.parseInt(ProductPrice));
                        product.setProductQuantity(Integer.parseInt(ProductQuantity));
                        images.add(product);

                        RoomDb roomDb = RoomDb.getDatabase(getContext());
                        roomDb.productDao().insertProduct(product);
                    }

                    RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext() , 2);
                    recyclerView = view.findViewById(R.id.rv_D);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setHasFixedSize(true);


                    RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getContext() , RecyclerView.HORIZONTAL , false);
                    recyclerView2 = view.findViewById(R.id.rv_);
                    recyclerView2.setLayoutManager(layoutManager2);
                    recyclerView2.setHasFixedSize(true);

                    adapter = new DashboardAdapter(images , getContext());
                    recyclerView.setAdapter(adapter);
                    recyclerView.setHasFixedSize(true);
                    adapter.notifyDataSetChanged();

                    adapter2 = new popularAdapter(images , getContext());
                    recyclerView2.setAdapter(adapter2);
                    recyclerView2.setHasFixedSize(true);
                    adapter2.notifyDataSetChanged();

                    sliderView = view.findViewById(R.id.image_slider);

                    slideAdapter sliderAdapter = new slideAdapter(images , getContext());

                    sliderView.setSliderAdapter(sliderAdapter);
                    sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
                    sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
                    sliderView.startAutoCycle();

                }
            }
        });

    }

    public void close(View view) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.profile){
            Bundle bundle = new Bundle();
            Fragment profileFragment = new ProfileFragment();
            if (signInAccount == null) {
                bundle.putString("name", user.getName());
                bundle.putString("mobile", user.getMobile());
                bundle.putString("email", user.getEmail());
                bundle.putString("password", user.getPassword());
                profileFragment.setArguments(bundle);
            }

            Navigation.findNavController(getView()).navigate(R.id.action_homeFragment_to_profileFragment , bundle);
            return true;
        }
        else if (item.getItemId() == R.id.logout){
            FirebaseAuth.getInstance().signOut();
            Navigation.findNavController(getView()).navigate(R.id.action_homeFragment_to_beginFragment);
            return true;
        }
        return false;
    }

}