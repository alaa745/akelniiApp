package com.example.myapplication;

import static androidx.navigation.Navigation.findNavController;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import at.favre.lib.crypto.bcrypt.BCrypt;



//MainActivity -> Begain

public class LoginFragment extends Fragment {
    EditText mobile;
    EditText uname;
    EditText password;
    ImageView logbt;
    Toolbar toolbar;
    ProgressDialog progressDialog;
    public static final String fileName = "UserData";
    public static final String keyName = "username";
    public static final String keyMobile = "mobile";
    public static final String keyEmail = "email";
    public static final String keyPassword = "password";
    SharedPreferences sharedPreferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        mobile = view.findViewById(R.id.mobile);
        uname = view.findViewById(R.id.uname);
        password = view.findViewById(R.id.password);
        logbt = view.findViewById(R.id.loginbtn);


//        if (sharedPreferences.getBoolean("isLogin",true)){
//            Activity a = (Activity) getContext();
//
//            Navigation.findNavController(a, R.id.nav_host_fragment).navigate(R.id.action_loginFragment_to_homeFragment);
//
//        }


//        getActivity().getSupportFragmentManager().addOnBackStackChangedListener(this);

        // showing the back button in action bar


        toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();

            }
        });

        logbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("logging In...");
                progressDialog.setCancelable(true);
                progressDialog.show();
                validateLogin(view);
            }
        });

        return view;
    }


    private Boolean validateUserName() {

        String val = uname.getText().toString();
        if (val.isEmpty()) {
            uname.setError("Username cannot be empty");
            return false;
        } else if (val.length() >= 15) {
            uname.setError("Username is too long!");
            return false;
        } else {
            uname.setError(null);
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
//        } else if (val.matches(pattern)) {
//            pass.setError("Password is too weak");
//            return false;
        } else {
            password.setError(null);
            return true;
        }
    }

    public void validateLogin(View v) {

        //false | false | true ---> ture
        // true | ture | false ---> true
        //
        if (!validateUserName() | !validatePhoneNo() | !validatePass()) {
            Log.d("valid" , String.valueOf(!validateUserName()) + " " + !validatePhoneNo() + " " + !validatePass());
            if (progressDialog.isShowing()){
                progressDialog.dismiss();
            }
            return;
        }
        final String passwordd = password.getText().toString();
        String userN = uname.getText().toString().trim();
        String phone = mobile.getText().toString().trim();


//        if (phone.charAt(0) == '0'){
//            phone = phone.substring(1);
//        }
//        String completeMobile = "+020"+ phone;

        Query check = FirebaseDatabase.getInstance().getReference("Users").orderByChild("mobile").equalTo(phone);
        check.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    mobile.setError(null);

                    String systemPass = snapshot.child(phone).child("password").getValue(String.class);
                    BCrypt.Result passHash = BCrypt.verifyer().verify(passwordd.toCharArray(), systemPass);
                    String systemName = snapshot.child(phone).child("name").getValue(String.class);
                    String email = snapshot.child(phone).child("email").getValue(String.class);

                    if (passHash.verified && systemName.equals(userN)) {

                        uname.setError(null);
                        password.setError(null);
                        RoomDb roomDb = RoomDb.getDatabase(getContext());
                        User user2 = new User(userN , email , phone , passwordd);
                        roomDb.userDao().insertUser(user2);
                        Log.d("userr" , "User: success");

//                        Bundle bundle = new Bundle();
//                        Fragment fragment = new HomeFragment();
//                        bundle.putString("name" , userN);
//                        bundle.putString("mobile" , phone);
//                        bundle.putString("email" , email);
//                        bundle.putString("password" , passwordd);
//                        fragment.setArguments(bundle);

//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        editor.putString(keyName , userN);
//                        editor.putString(keyPassword , passwordd);
//                        editor.putString(keyMobile , phone);
//                        editor.putString(keyEmail , email);
//                        editor.commit();

                        Activity a = (Activity) getContext();

                        Navigation.findNavController(a, R.id.nav_host_fragment).navigate(R.id.action_loginFragment_to_homeFragment);

                        if (progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }

                    } else {

                        if (!passHash.verified && !systemName.equals(userN)) {
                            if (progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
                            uname.setError("Invalid username!");
                            password.setError("Invalid Password!");

                        }
//                            Toast.makeText(Login.this , "Invalid username and password!" , Toast.LENGTH_LONG).show();
                        else if (!systemName.equals(userN)) {
                            if (progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
                            uname.setError("Username does not match!!");
                        }
//                            Toast.makeText(Login.this , "Username does not match!" , Toast.LENGTH_LONG).show();
                        else {
                            if (progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
                            password.setError("Password does not match!");
                        }
//                            Toast.makeText(Login.this , "Password does not match!" , Toast.LENGTH_LONG).show();
                    }
                } else {
//                    progressBar.setVisibility(View.GONE);
                    if (progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    mobile.setError("Mobile Number does not exist");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}