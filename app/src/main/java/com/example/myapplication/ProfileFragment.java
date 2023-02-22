package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import at.favre.lib.crypto.bcrypt.BCrypt;

public class ProfileFragment extends Fragment {

    EditText userName , userEmail , userPassword;
    Button updateBtn;
    TextView hello;
    public static final String fileName = "UserData";
    public static final String keyName = "username";
    public static final String keyMobile = "mobile";
    public static final String keyEmail = "email";
    public static final String keyPassword = "password";
    String _mobile , _name , _email , _password;
    SharedPreferences sharedPreferences;
    User user;
    DatabaseReference reference;
    private String PasswordHash;
    Toolbar toolbar;
    GoogleSignInAccount signInAccount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view = inflater.inflate(R.layout.fragment_profile, container, false);

         userName = view.findViewById(R.id.usernameProfile);
         userEmail = view.findViewById(R.id.emailProfile);
         userPassword = view.findViewById(R.id.passProfile);
         updateBtn = view.findViewById(R.id.updateBtn);
         toolbar = view.findViewById(R.id.toolbar);
         hello = view.findViewById(R.id.helloTxt);


        ((AppCompatActivity)requireActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        sharedPreferences = getActivity().getSharedPreferences(fileName, Context.MODE_PRIVATE);

        signInAccount = GoogleSignIn.getLastSignedInAccount(getContext());
        if (signInAccount == null) {
            _name = getArguments().getString("name");
            _mobile = getArguments().getString("mobile");
            _email = getArguments().getString("email");
            _password = getArguments().getString("password");

            user = new User(_name , _email , _mobile , _password);
        }
        else {
            _name = signInAccount.getDisplayName();
            _email = signInAccount.getEmail();
            userPassword.setEnabled(false);
        }

        reference = FirebaseDatabase.getInstance().getReference("Users");
//        RoomDb roomDb = RoomDb.getDatabase(getContext());
//        user = roomDb.userDao().getUserByMobile(_mobile);

        hello.setText("Hello " + _name.substring(0 , 1).toUpperCase() + _name.substring(1));
        showUserData();

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update(view);
            }
        });
         return view;
    }


    public void showUserData(){
        userName.setText(_name);
        userEmail.setText(_email);
        userPassword.setText(_password);
    }

    public Boolean isGoogleUser(){
        if (signInAccount != null)
            return true;
        else
            return false;
    }

    public void update(View view){
        if (!isGoogleUser()) {
            if (updateBtn.getText().toString().equalsIgnoreCase("update")){
                if (!isNameChanged() || !isEmailChanged() || !isPasswordChanged()){
                    updateBtn.setText("EDIT");
                    Toast.makeText(getContext() , "Data has not been updated" , Toast.LENGTH_SHORT).show();
                    userName.setEnabled(false);
                    userEmail.setEnabled(false);
                    userPassword.setEnabled(false);
                }
                else {
                    updateBtn.setText("EDIT");
                    Toast.makeText(getContext(), "Data has been updated", Toast.LENGTH_SHORT).show();
                    userName.setEnabled(false);
                    userEmail.setEnabled(false);
                    userPassword.setEnabled(false);
                }
            }
            else {
                updateBtn.setText("UPDATE");
                userName.setEnabled(true);
                userEmail.setEnabled(true);
                userPassword.setEnabled(true);
            }

        }
        else {
            Snackbar snackbar = Snackbar
                    .make(getView(), "You Cannot Edit Your Information As You SingIn With Google" , Snackbar.LENGTH_SHORT);
            snackbar.show();
            return;
        }

//        if (isNameChanged() || isEmailChanged() || isPasswordChanged()){
//            updateBtn.setText("EDIT");
//            Toast.makeText(getContext() , "Data has been updated" , Toast.LENGTH_SHORT).show();
//            userName.setEnabled(false);
//            userEmail.setEnabled(false);
//            userPassword.setEnabled(false);
//        }

    }

    public boolean isNameChanged(){
        if (!user.getName().equals(userName.getText().toString())){
            reference.child(user.mobile).child("name").setValue(userName.getText().toString());
            return true;
        }
        else
            return false;
    }

    public boolean isEmailChanged(){
        if (!user.getEmail().equals(userEmail.getText().toString())){
            reference.child(user.mobile).child("email").setValue(userEmail.getText().toString());
            return true;
        }
        else
            return false;
    }

    public boolean isPasswordChanged(){
        if (!user.getPassword().equals(userPassword.getText().toString())){
            PasswordHash = BCrypt.withDefaults().hashToString(12 , userPassword.getText().toString().toCharArray());
            reference.child(user.mobile).child("password").setValue(PasswordHash);

            return true;
        }
        else
            return false;
    }
}