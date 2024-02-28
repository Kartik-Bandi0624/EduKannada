package com.example.edukannada.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.edukannada.R;

public class Payment extends AppCompatActivity {
    Button btnPayment, goBack;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        btnPayment = findViewById(R.id.payment);
        goBack = findViewById(R.id.goBack);

        btnPayment.setOnClickListener(v -> {
            startActivity(new Intent(this, WelcomeActivity.class));
            finish();
        });
        goBack.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginForm.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            Toast.makeText(this, "Exited from this application", Toast.LENGTH_SHORT).show();
        });
    }
}