package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;

import java.util.ArrayList;

public class burgerFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener{
    String ProductId , ProductPrice , ProductName , ProductQuantity;

    ArrayList<Product> images;
    private String ProductImage;
    RecyclerView recyclerView;
    BurgerAdapter adapter;
    FirebaseFirestore firestore;
    NavigationView navigationView;
    Toolbar toolbar;
    DrawerLayout drawerLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_burger, container, false);
        firestore = FirebaseFirestore.getInstance();
        images = new ArrayList<Product>();
        recyclerView = view.findViewById(R.id.recycler);
        toolbar = view.findViewById(R.id.burgerToolbar);
        navigationView = view.findViewById(R.id.burgerNav);
        drawerLayout = view.findViewById(R.id.burgerDrawer);

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        navigationView = view.findViewById(R.id.burgerNav);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity() , drawerLayout , toolbar , R.string.navi_open , R.string.navi_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        getDataFromFirebase(view);

        return view;
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

                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext() , RecyclerView.VERTICAL , false);
                    recyclerView = view.findViewById(R.id.recycler);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setHasFixedSize(true);


                    adapter = new BurgerAdapter(images , getContext());
                    recyclerView.setAdapter(adapter);
                    recyclerView.setHasFixedSize(true);
                    adapter.notifyDataSetChanged();


                }
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.profile){
            Navigation.findNavController(getView()).navigate(R.id.action_homeFragment_to_profileFragment);
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