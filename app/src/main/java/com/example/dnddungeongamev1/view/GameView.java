package com.example.dnddungeongamev1.view;

import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dnddungeongamev1.R;
import com.example.dnddungeongamev1.model.StepCounter;

public class GameView extends AppCompatActivity {
    StepCounter stepCounter = new StepCounter();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.game_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.gameView), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        stepCounter = new StepCounter();

        Button moveBtn = findViewById(R.id.moveBtn);
        Button interactBtn = findViewById(R.id.interactBtn);

    }
}
