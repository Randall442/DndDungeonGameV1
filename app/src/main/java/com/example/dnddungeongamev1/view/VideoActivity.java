package com.example.dnddungeongamev1.view;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dnddungeongamev1.R;

public class VideoActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_video);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        WebView webView = findViewById(R.id.webViewVidOne);

        Button playVidOne = findViewById(R.id.playBtnOne);
        Button playVidTwo = findViewById(R.id.playBtnTwo);

        playVidOne.setOnClickListener(view -> {
            playYouTubeVideo("https://www.youtube.com/watch?v=KS2IDmjDbts", webView);
        });
        playVidTwo.setOnClickListener(view -> {
            playYouTubeVideo("https://www.youtube.com/watch?v=pd8ouD5UFXo&list=PLJqE7QBvDyc_-q5sZQV3eUK6vwfznTt-6", webView);
        });

        //webView.loadUrl("https://www.youtube.com/watch?v=KS2IDmjDbts");

        //webView2.loadUrl("");
    }
    private void playYouTubeVideo(String url, WebView webView)
    {
        webView.setVisibility(View.VISIBLE);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);

        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);

    }
}