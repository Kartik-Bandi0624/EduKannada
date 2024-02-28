package com.example.edukannada.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.edukannada.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class CreateAccount extends AppCompatActivity {

    EditText phone1, otp, userName, emailId, passWord;

    Button btnGenOTP, btnVerify;

    FirebaseAuth mAuth;

    String verificationID;

    ProgressBar progressBar;
    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
            final String code = credential.getSmsCode();
            if (code != null) {
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(CreateAccount.this, "Verification failed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken token) {
            super.onCodeSent(s, token);
            verificationID = s;
            Toast.makeText(CreateAccount.this, "Code sent", Toast.LENGTH_SHORT).show();
            btnVerify.setEnabled(true);
            progressBar.setVisibility(View.INVISIBLE);
        }
    };


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        userName = findViewById(R.id.username);
        emailId = findViewById(R.id.email);
        passWord = findViewById(R.id.password);
        phone1 = findViewById(R.id.phone);
        btnGenOTP = findViewById(R.id.otp_gen);
        progressBar = findViewById(R.id.progressbar);
        otp = findViewById(R.id.otp_enter);
        btnVerify = findViewById(R.id.otp_verify);
        mAuth = FirebaseAuth.getInstance();

        btnGenOTP.setOnClickListener(v -> {
            if (TextUtils.isEmpty(userName.getText().toString())) {
                userName.setError("Username can not be empty");
                userName.requestFocus();
            } else if (TextUtils.isEmpty(emailId.getText().toString())) {
                validateEmailId();
                emailId.requestFocus();
            } else if (TextUtils.isEmpty(passWord.getText().toString())) {
                validatePassword();
                passWord.requestFocus();
            } else if (TextUtils.isEmpty(phone1.getText().toString())) {
                validatePhoneNo();
                phone1.requestFocus();
            } else {
                String number = phone1.getText().toString();
                progressBar.setVisibility(View.VISIBLE);
                btnGenOTP.setVisibility(View.INVISIBLE);
                sendVerificationCode(number);
            }
        });

        btnVerify.setOnClickListener(v -> {
            if (TextUtils.isEmpty(otp.getText().toString())) {
                Toast.makeText(CreateAccount.this, "Wrong OTP entered", Toast.LENGTH_SHORT).show();
            } else {
                verifyCode(otp.getText().toString());
            }
        });
    }

    private void sendVerificationCode(String phoneNumber) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth).setPhoneNumber("+91 " + phoneNumber)       // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(this)                 // (optional) Activity for callback binding
                // If no activity is passed, reCAPTCHA verification can not be used.
                .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void verifyCode(String Code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID, Code);
        signInByCredentials(credential);
    }

    private void signInByCredentials(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(CreateAccount.this, "Login Successful", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(CreateAccount.this, LoginForm.class));
                }
            }
        });
    }

    private boolean validateEmailId() {
        if (!emailId.getText().toString().contains("@gmail.com")) {
            emailId.setError("email must contains @gmail.com");
            return false;
        }
        return true;
    }

    private boolean validatePassword() {
        if (passWord.length() == 0) {
            passWord.setError("Password is required");
            return false;
        } else if (passWord.length() < 8) {
            passWord.setError("Password must be minimum 8 characters");
            return false;
        }
        return true;
    }

    private boolean validatePhoneNo() {
        if (phone1.getText().toString().length() >= 10) {
            phone1.setError("Phone number must be at-least 10 characters");
            return false;
        } else if (TextUtils.isEmpty(phone1.getText().toString())) {
            phone1.setError("Phone number cannot be empty");
            return false;
        }
        return true;
    }
}

//        register.setOnClickListener(v -> {
//            progressDialog.show();
//            username = userName.getText().toString();
//            emailId = email.getText().toString();
//
//            final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//            firebaseAuth.createUserWithEmailAndPassword(emailId, username).addOnCompleteListener(task -> {
//                progressDialog.hide();
//                validateEmailId();
//                validatePassword();
//                if (task.isSuccessful()) {
//                    firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if (task.isSuccessful()) {
//                                startActivity(new Intent(CreateAccount.this, LoginForm.class));
//                                Toast.makeText(CreateAccount.this, "User Registered Successfully, Please check your mail", Toast.LENGTH_LONG).show();
//                                email.setText("");
//                                userName.setText("");
//                            } else {
//                                Toast.makeText(CreateAccount.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//                } else {
//                    Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            });
//        });
//    }