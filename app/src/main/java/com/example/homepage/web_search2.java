package com.example.homepage;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

public class web_search2 extends AppCompatActivity {
    private WebView webView;
    private EditText urlEditText;
    private Button openUrlButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_search2);

        webView = findViewById(R.id.webView);
        urlEditText = findViewById(R.id.urlEditText);
        openUrlButton = findViewById(R.id.openUrlButton);

        webView.setWebViewClient(new SafeWebViewClient());

        openUrlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = urlEditText.getText().toString().trim();

                if (containsInappropriateWords(searchText)) {
                    Toast.makeText(web_search2.this, "Inappropriate content", Toast.LENGTH_SHORT).show();
                } else {
                    String searchUrl = "https://www.google.com/search?q=" + searchText;
                    webView.loadUrl(searchUrl);
                }
            }
        });
    }

    private boolean containsInappropriateWords(String searchText) {
        String[] inappropriateWords = {"sex", "porn", "suicide", "kill","horny","condom", "fuck","naked","bang","spank"};

        for (String word : inappropriateWords) {
            if (searchText.toLowerCase().contains(word)) {
                return true;
            }
        }

        return false;
    }

    private class SafeWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String url = request.getUrl().toString();
            return super.shouldOverrideUrlLoading(view, request);
        }
    }
}