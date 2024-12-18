package com.example.dnddungeongamev1.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dnddungeongamev1.R;
import com.example.dnddungeongamev1.controller.GameEngine;
import com.example.dnddungeongamev1.model.StepCounter;
import com.google.android.gms.maps.GoogleMap;

public class GameView extends AppCompatActivity {
    private StepCounter stepCounter;
    private TextView countView;
    private TextView locationView;
    private MediaPlayer mediaPlayer;
    private GameEngine gameEngine;

    private GoogleMap mMap;

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
        playAudio(R.raw.sampleaudio);
        //locationView = findViewById(R.id.locationText);
        countView = findViewById(R.id.stepCountText);
        Button moveBtn = findViewById(R.id.moveBtn);
        Button interactBtn = findViewById(R.id.interactBtn);
        Button mapBtn = findViewById(R.id.mapBtn);
        Button vidOneBtn = findViewById(R.id.vidBtnGame);
        mediaPlayer = null;
        gameEngine = new GameEngine(this);


        gameEngine.setListener(new GameEngine.GameControllerListener() {
            @Override
            public void onPlayerMoved(String direction, int x, int y) {
                locationView.setText("Player moved " + direction + " to (" + x + ", " + y + ")");
            }

            @Override
            public void onInteraction(String action) {
                Toast.makeText(GameView.this, "Interaction: " + action, Toast.LENGTH_SHORT).show();
            }
        });


        if (moveBtn == null || interactBtn == null || mapBtn == null) {
            throw new NullPointerException("One or more buttons are null. Check your layout file IDs.");
        }

        stepCounter = new StepCounter(this);

        stepCounter.setStepListener(new StepCounter.StepListener() {

            @SuppressLint("StringFormatInvalid")
            public void onStepCountChanged(int stepCount) {
                countView.setText(getString(R.string.step_count, stepCount));
            }

            @Override
            public void onNoStepCounter() {
                countView.setText(getString(R.string.no_step_counter));
            }
        });

        moveBtn.setOnClickListener(this::showDirectionMenu);

        interactBtn.setOnClickListener(this::showInteractMenu);

        mapBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
        });

        vidOneBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, VideoActivity.class);
            startActivity(intent);
        });

    }

    private void showInteractMenu(View view)
    {
        PopupMenu popupMenu = new PopupMenu(this, view);

        popupMenu.getMenuInflater().inflate(R.menu.interact_menu, popupMenu.getMenu());

        popupMenu.setGravity(android.view.Gravity.CENTER);

        popupMenu.setOnMenuItemClickListener(menuItem ->
        {
            int id = menuItem.getItemId();

            if (id == R.id.take) {
                Toast.makeText(this, "Take selected", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.use) {
                Toast.makeText(this, "Use selected", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });
        popupMenu.show();
    }
    private void showDirectionMenu(View view)
    {

        PopupMenu popupMenu = new PopupMenu(this, view);

        popupMenu.getMenuInflater().inflate(R.menu.direction_menu, popupMenu.getMenu());

        popupMenu.setGravity(android.view.Gravity.CENTER);


        popupMenu.setOnMenuItemClickListener(menuItem ->
        {
            int id = menuItem.getItemId();

            if (id == R.id.north) {
                Toast.makeText(this, "Moving North", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.south) {
                Toast.makeText(this, "Moving South", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.east) {
                Toast.makeText(this, "Moving East", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.west) {
                Toast.makeText(this, "Moving West", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });

        popupMenu.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        stepCounter.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stepCounter.stop();
    }

    private void playAudio(int audioResId)
    {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(this, audioResId);
        if(mediaPlayer != null)
        {
            mediaPlayer.start();
            Toast.makeText(this, "Audio Playing", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error in playing audio", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                {
                    mMap.setMyLocationEnabled(true);
                }
            } else
            {

                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
