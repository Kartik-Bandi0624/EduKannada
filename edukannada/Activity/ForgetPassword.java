package com.example.edukannada.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.edukannada.HelperClass.GlobalServerUrl;
import com.example.edukannada.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;


public class ForgetPassword extends AppCompatActivity {

    TextView submit_forgotpwd;
    EditText emailid;

    TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        emailid = findViewById(R.id.forgotpasswordemailid);

        login = findViewById(R.id._login);

        login.setOnClickListener(view -> {
            Intent intent = new Intent(ForgetPassword.this, LoginForm.class);
            startActivity(intent);
        });

        submit_forgotpwd = findViewById(R.id.buttonforgotpassword);
        submit_forgotpwd.setOnClickListener(view -> {
            String sEmailid = emailid.getText().toString();
            if (TextUtils.isEmpty(sEmailid)) {
                emailid.setError("Please enter email id");
            } else {
                validateEmailidForForgotPassword(sEmailid);
            }
        });

    }

    void validateEmailidForForgotPassword(String sEmailid) {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

        asyncHttpClient.get(GlobalServerUrl.url + "ForgotPassword?emailid=" + sEmailid, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                if (i == 200) { //The HTTP 200 OK success status response code indicates that the request has succeeded.
                    Intent intent = new Intent(getApplicationContext(), LoginForm.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "Username and password is sent to your email id", Toast.LENGTH_LONG).show();

                } else if (i == 202) { //The 202 Accepted status code means that the request has been accepted for processing,
                    // but the processing has not been finished yet.
                    emailid.setError("Invalid email id. Please Enter Register emailid");
                    Toast.makeText(getApplicationContext(), "Invalid email id. Please Enter Register emailid", Toast.LENGTH_LONG).show();

                } else if (i == 204) {//The HTTP 204 No Content success status response code indicates that a request has succeeded,
                    // but that the client doesn't need to navigate away from its current page
                    Toast.makeText(getApplicationContext(), "Your account has expired, renew your account", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                if (i >= 400 && i < 500) {

                    Toast.makeText(getApplicationContext(), "Client error,please try after some time", Toast.LENGTH_LONG).show();

                } else if (i >= 500) {

                    Toast.makeText(getApplicationContext(), "Server error,please try after some time", Toast.LENGTH_LONG).show();

                } else if (i >= 300 && i < 400) {

                    Toast.makeText(getApplicationContext(), "Connection Error,please try after some time", Toast.LENGTH_LONG).show();

                } else if (i == 0) {
                    Toast.makeText(getApplicationContext(), "Network Connection Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}