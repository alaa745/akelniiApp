package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SignupFragment extends Fragment {

    TextView regtxt;
    EditText uname;
    EditText password;
    EditText email;
    EditText mobile;
    String Name , Email , Password;
    Toast toast;
    FirebaseDatabase root;
    DatabaseReference reference;
    int counter = 0;
    Toolbar toolbar;
    Button regBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_signup, container, false);




        regBtn = view.findViewById(R.id.regbt);
        regtxt = view.findViewById(R.id.regtxt);
        uname = view.findViewById(R.id.uname);
        email = view.findViewById(R.id.email);
        mobile = view.findViewById(R.id.mobile);
        password = view.findViewById(R.id.password);

        toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();

            }
        });

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRegister(v);
            }
        });



        return view;
    }

    private Boolean validateUserName(){

        String val = uname.getText().toString();
        if (val.isEmpty()){
            uname.setError("Username cannot be empty");
            return false;
        }else if(val.length() >= 15){
            uname.setError("Username is too long!");
            return false;
        }else {
            uname.setError(null);
            return true;
        }

    }

    private Boolean validateEmail(){

        String val = email.getText().toString();
        String pattern = "[a-zA-z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()){
            email.setError("Email cannot be empty");
            return false;
        }else if(!val.matches(pattern)){
            email.setError("Invalid email address");
            return false;
        }else {
            email.setError(null);
            return true;
        }

    }

    private Boolean validatePhoneNo() {

        String val = mobile.getText().toString();

        if (val.equals("+20")) {
            mobile.setError("Mobile cannot be empty");
            return false;
        } else {
            mobile.setError(null);
            return true;
        }
    }

    private Boolean validatePass() {

        String val = password.getText().toString();
        String pattern = "^" + "(?=.*[a-zA-Z])" + "(?=.*[@#$%^&+=])" + "(?=\\s+$)" + ".{4,}" + "$";

        if (val.isEmpty()) {
            password.setError("Password cannot be empty");
            return false;
        } else {
            password.setError(null);
            return true;
        }
    }

    public void onRegister(View view) {

        if (!validateUserName() | !validateEmail() | !validatePhoneNo() | !validatePass()){
            return;
        }

        root = FirebaseDatabase.getInstance();
        reference = root.getReference("users");
//        Intent intent = new Intent(getBaseContext() , MainActivity2.class);
        Name = uname.getText().toString();
        Email = email.getText().toString();
        String phone = mobile.getText().toString();
        Password = password.getText().toString();


//        if (phone.charAt(0) == '0'){
//            phone = phone.substring(1);
//        }
//        String completeMobile = "+" + countryCodePicker.getSelectedCountryCode() + phone;



        Query check = FirebaseDatabase.getInstance().getReference("Users").orderByChild("mobile").equalTo(phone);
        check.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    mobile.setError(null);

                    String systemName = snapshot.child(phone).child("name").getValue(String.class);

//                            Toast.makeText(Login.this , "Invalid username and password!" , Toast.LENGTH_LONG).show();

                    Toast.makeText(getContext() , "This user is exist login please!" , Toast.LENGTH_SHORT).show();

                }
                else {
//                    Bundle intent = new Intent(getContext() , verifyOTP.class);
//                    intent.putExtra("Name" , Name);
//                    intent.putExtra("Email" , Email);
//                    intent.putExtra("Password" , Password);
//                    intent.putExtra("mobile" , phone);

                    Bundle bundle = new Bundle();
                    Fragment fragment = new VerifyOtpFragment();
                    bundle.putString("name" , Name);
                    bundle.putString("email" , Email);
                    bundle.putString("password" , Password);
                    bundle.putString("mobile" , phone);

                    fragment.setArguments(bundle);

                    Navigation.findNavController(view).navigate(R.id.action_signupFragment_to_verifyOtpFragment , bundle);
//                    User user = new User(Name , Email , phone , Password);
//                    counter++;
//                    reference.child(String.valueOf(phone)).setValue(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext() , error.getMessage() , Toast.LENGTH_SHORT).show();
            }
        });


//        try {
//            UsersHelper usersHelper = new UsersHelper(Name , Email , completeMobile , Password);
//            counter++;
//            reference.child(String.valueOf(completeMobile)).setValue(usersHelper);
//
//            Toast.makeText(context , "Successful Sign Up" , duration).show();
//        }catch (Exception e){
//            Toast.makeText(context , "Invalid" , duration).show();
//        }

//        intent.putExtra("name" , Name);
//        mainActivity2.setfName(Name);
//
//        startActivity(intent);
    }
}