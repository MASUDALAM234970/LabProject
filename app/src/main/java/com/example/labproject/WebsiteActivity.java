package com.example.labproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class WebsiteActivity extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_website);

        webView = findViewById(R.id.webView);


        String intent = getIntent().getStringExtra("ducmc");
        String intent1 = getIntent().getStringExtra("ducmc1");
        String intent2 = getIntent().getStringExtra("alam");
        String intent3 = getIntent().getStringExtra("ducmc3");





        // Enable JavaScript (optional, depends on your website)
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);


     //   webView.setWebViewClient(new MyWebViewClient());
        webView.setWebViewClient(new WebViewClient());

        // Set a WebChromeClient to handle progress and title changes (optional)


        // Load a website
         webView.loadUrl(intent2);
        webView.loadUrl(intent3);

        webView.loadUrl(intent1);
        webView.loadUrl(intent);

    }

    @Override
    public void onBackPressed() {

        if (webView.canGoBack())
        {
            webView.goBack();
        }
        else {
            super.onBackPressed();
        }

    }
}