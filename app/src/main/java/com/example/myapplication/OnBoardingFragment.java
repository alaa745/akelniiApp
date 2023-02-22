package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class OnBoardingFragment extends Fragment {
    ViewPager viewPager;
    LinearLayout dots;
    SliderAdapter sliderAdapter;
    Button getBtn;
    String destination;
    Toolbar toolbar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_on_boarding, container, false);

        viewPager = view.findViewById(R.id.slider);
        dots = view.findViewById(R.id.dots);
        getBtn = view.findViewById(R.id.getStarted);
        toolbar = view.findViewById(R.id.toolbar);
        sliderAdapter = new SliderAdapter(getContext());
        viewPager.setAdapter(sliderAdapter);

        ((AppCompatActivity)requireActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });

        getBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                destination = getArguments().getString("Destination");
                if (isConnected()){
                    if (destination.equals("login")){
                        Navigation.findNavController(getView()).navigate(R.id.action_onBoardingFragment_to_loginFragment);
                    }
                    else if (destination.equals("signup"))
                        Navigation.findNavController(getView()).navigate(R.id.action_onBoardingFragment_to_signupFragment);
                }
                else {
                    Bundle bundle = new Bundle();
                    Fragment fragment = new NoInternetFragment();
                    bundle.putString("Destination" , destination);
                    fragment.setArguments(bundle);

                    Navigation.findNavController(getView()).navigate(R.id.action_onBoardingFragment_to_noInternetFragment , bundle);
                }
            }
        });


        return view;
    }

    public Boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null){
            if (networkInfo.isConnected())
                return true;
            else
                return false;
        }
        else
            return false;
    }
}