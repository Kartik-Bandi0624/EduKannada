package com.example.edukannada.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.edukannada.R;

public class ComputerVideo extends AppCompatActivity {

    TextView compOne, compTwo, compThree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_computer_video);

        compOne = findViewById(R.id.comp_one);
        compTwo = findViewById(R.id.comp_two);
        compThree = findViewById(R.id.comp_three);

        compOne.setOnClickListener(v -> {
            startActivity(new Intent(this, LauncherOne.class));
        });

        compTwo.setOnClickListener(v -> {
            startActivity(new Intent(this, LauncherTwo.class));
        });

        compThree.setOnClickListener(v -> {
            startActivity(new Intent(this, LauncherThree.class));
        });
    }
}