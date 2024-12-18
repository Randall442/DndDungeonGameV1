package com.example.dnddungeongamev1.view;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dnddungeongamev1.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ACTIVITY_RECOGNITION = 100;
    private FirebaseAuth auth;
    private EditText loginText;
    private EditText pwdText;

    private FirebaseAnalytics analytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACTIVITY_RECOGNITION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACTIVITY_RECOGNITION},
                        REQUEST_ACTIVITY_RECOGNITION);
            }
        }




        auth = FirebaseAuth.getInstance();

        FirebaseApp.initializeApp(this);

        loginText = findViewById(R.id.loginText);
        pwdText = findViewById(R.id.pwdText);

        Button loginBtn = findViewById(R.id.loginBtn);
        Button registerBtn = findViewById(R.id.registerBtn);


        if (loginText == null) {
            throw new NullPointerException("loginText is null. Check activity_main.xml for R.id.loginText.");
        }
        if (pwdText == null) {
            throw new NullPointerException("pwdText is null. Check activity_main.xml for R.id.pwdText.");
        }
        if (loginBtn == null) {
            throw new NullPointerException("loginBtn is null. Check activity_main.xml for R.id.loginBtn.");
        }
        if (registerBtn == null) {
            throw new NullPointerException("registerBtn is null. Check activity_main.xml for R.id.registerBtn.");
        }

        loginBtn.setOnClickListener(v -> login());
        registerBtn.setOnClickListener(v -> register());

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_ACTIVITY_RECOGNITION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void register()
    {
        String email = loginText.getText().toString().trim();
        String pwd = pwdText.getText().toString().trim();

        if(email.isEmpty() || pwd.isEmpty()) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return;
        }
        auth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(this, task -> {
            if(task.isSuccessful()) {
                Toast.makeText(this, "Register Successful", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "Registration Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void login()
    {
        String email = loginText.getText().toString().trim();
        String pwd = pwdText.getText().toString().trim();

        if(email.isEmpty() || pwd.isEmpty()) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return;
        }
        auth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(this, task->{
            if(task.isSuccessful())
            {
                FirebaseUser user = auth.getCurrentUser();
                Intent intent = new Intent(MainActivity.this, GameView.class);
                startActivity(intent);
            }
            else
            {
                Toast.makeText(this, "Login Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}