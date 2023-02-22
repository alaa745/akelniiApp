package com.example.myapplication;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class VerifyOtpFragment extends Fragment {
    Button verify;
    PinView number;
    TextView code;
    TextView verification;
    TextView description;
    FirebaseAuth mAuth;
    String systemCode;
    FirebaseDatabase root;
    DatabaseReference reference;
    String Name;
    String Email;
    String Password;
    String PasswordHash;
    String mobile;
    ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_verify_otp, container, false);

        Name = getArguments().getString("name");
        Email = getArguments().getString("email");
        Password = getArguments().getString("password");
        mobile = getArguments().getString("mobile");

        verify = view.findViewById(R.id.verify);
        number = view.findViewById(R.id.number);
        code = view.findViewById(R.id.code);
        verification = view.findViewById(R.id.verification);
        description = view.findViewById(R.id.description);
        progressBar = view.findViewById(R.id.progressBar2);
        description.setText("Enter One Time Password for " + mobile);

        mAuth = FirebaseAuth.getInstance();

        sendVerification(mobile);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickVerify(view);
            }
        });

        return view;
    }

    public void sendVerification(String mobile){

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(mobile)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(getActivity())                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            systemCode = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code!= null){
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(getContext() , e.getMessage() , Toast.LENGTH_SHORT).show();
        }
    };

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(systemCode , code);
        signInUSer(credential);
    }

    private void signInUSer(PhoneAuthCredential credential) {
        progressBar.setVisibility(View.VISIBLE);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    PasswordHash = BCrypt.withDefaults().hashToString(12 , Password.toCharArray());
                    Log.d("register" , PasswordHash);

                    RoomDb roomDb = RoomDb.getDatabase(getContext());
                    User user2 = new User(Name , Email , mobile , Password);
                    roomDb.userDao().insertUser(user2);
                    Log.d("userr" , "User: success");

                    root = FirebaseDatabase.getInstance();
                    reference = root.getReference("Users");
                    User user = new User(Name , Email , mobile , PasswordHash);
                    reference.child(String.valueOf(mobile)).setValue(user);


//                    Fragment login = new LoginFragment();

                    Navigation.findNavController(getView()).navigate(R.id.action_verifyOtpFragment_to_loginFragment);
                }
                else
                    Toast.makeText(getContext() , "Please Enter A Valid OTP Code!" , Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onClickVerify(View view){
//        Intent intent = new Intent(getBaseContext() , MainActivity2.class);

        String code = number.getText().toString();
        if (!code.isEmpty())
            verifyCode(code);
        else
            number.setError("Please Enter The OTP");
    }

}