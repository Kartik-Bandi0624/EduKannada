package com.example.edukannada.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.edukannada.R;

public class LauncherThree extends AppCompatActivity {
    WebView webView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher_three);

        webView2 = findViewById(R.id.webView2);
        webView2.setWebViewClient(new WebViewClient());
        webView2.loadUrl("https://www.youtube.com/watch?v=j3SshEOCBW0");
        WebSettings webSettings2 =webView2.getSettings();
        webSettings2.setJavaScriptEnabled(true);
    }
    @Override
    public void onBackPressed() {
        if(webView2.isFocused() && webView2.canGoBack()){
            webView2.goBack();
        }else {
            super.onBackPressed();
        }
    }
}