package com.example.edukannada.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.edukannada.R;

public class LauncherTwo extends AppCompatActivity {
    WebView webView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher_two);

        webView1 = findViewById(R.id.webView1);
        webView1.setWebViewClient(new WebViewClient());
        webView1.loadUrl("https://www.youtube.com/watch?v=a4o2uE_qVyI");
        WebSettings webSettings1 =webView1.getSettings();
        webSettings1.setJavaScriptEnabled(true);
    }

    @Override
    public void onBackPressed() {
        if(webView1.isFocused() && webView1.canGoBack()){
            webView1.goBack();
        }else {
            super.onBackPressed();
        }
    }
}