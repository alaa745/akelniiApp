package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class NoInternetFragment extends Fragment {
    RelativeLayout noInternet;
    MaterialButton tryButton;
    ImageView noImage;
    String destination;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_no_internet, container, false);

        noInternet = view.findViewById(R.id.noInternet);
        tryButton = view.findViewById(R.id.tryButton);
        noImage = view.findViewById(R.id.noInternetImage);


        tryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected()) {
                    destination = getArguments().getString("Destination");

                    if (destination.equals("login")) {
                        Navigation.findNavController(getView()).navigate(R.id.action_noInternetFragment_to_loginFragment);
                        getActivity().getFragmentManager().popBackStack();



                    }
                    else if (destination.equals("signup")){
                        Navigation.findNavController(getView()).navigate(R.id.action_noInternetFragment_to_signupFragment);
                        getActivity().getFragmentManager().popBackStack();

                    }
                }
                else {
                    Toast.makeText(getContext(), "INTERNET IS NOT AVAILABLE", Toast.LENGTH_SHORT).show();
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