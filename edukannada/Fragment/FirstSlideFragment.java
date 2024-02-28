package com.example.edukannada.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.edukannada.Activity.Payment;
import com.example.edukannada.R;
import com.example.edukannada.databinding.ActivityMainBinding;
import com.example.edukannada.databinding.FragmentFirstSlideBinding;

public class FirstSlideFragment extends Fragment {

//    Activity context;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        context = getActivity();
        View view = inflater.inflate(R.layout.fragment_first_slide, container, false);

        return view;
    }

//    public  void onStart(){
//        super.onStart();
//       Button buy =(Button)context.findViewById(R.id.btnBuy);
//        buy.setOnClickListener(v ->{
//            Intent intent = new Intent(context, Payment.class);
//            startActivity(intent);
//        });
//    }
}