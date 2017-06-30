package com.example.lethanhloi.testmaps;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class TinTucActivity extends AppCompatActivity {

    WebView webView;
    Button btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tin_tuc);
        webView=(WebView)findViewById(R.id.webView);
        Intent intent = getIntent();
        String duongLink= intent.getStringExtra("Link");
        webView.loadUrl(duongLink);
        webView.setWebViewClient(new WebViewClient());

        btnBack=(Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TinTucActivity.this,MapsActivity.class);
                startActivity(intent);
            }
        });
    }

}
