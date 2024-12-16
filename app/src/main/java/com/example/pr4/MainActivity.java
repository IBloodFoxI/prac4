package com.example.pr4;

import static android.content.Context.MODE_PRIVATE;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class MainActivity extends AppCompatActivity {

    private static final String SHARED_PREFS_KEY = "tictactoe_prefs";
    private static final String THEME_KEY = "theme";
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(SHARED_PREFS_KEY, MODE_PRIVATE);

        int currentTheme = sharedPreferences.getInt(THEME_KEY, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        AppCompatDelegate.setDefaultNightMode(currentTheme);

        Button buttonPlayerVsPlayer = findViewById(R.id.button_player_vs_player);
        Button buttonPlayerVsBot = findViewById(R.id.button_player_vs_bot);
        Button buttonChangeTheme = findViewById(R.id.button_change_theme);

        buttonPlayerVsPlayer.setOnClickListener(view -> startGameActivity(true));
        buttonPlayerVsBot.setOnClickListener(view -> startGameActivity(false));
        buttonChangeTheme.setOnClickListener(view -> changeTheme());
    }

    private void startGameActivity(boolean isPlayerVsPlayer) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("isPlayerVsPlayer", isPlayerVsPlayer);
        startActivity(intent);
    }

    private void changeTheme() {
        int currentTheme = AppCompatDelegate.getDefaultNightMode();

        if (currentTheme == AppCompatDelegate.MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(THEME_KEY, AppCompatDelegate.getDefaultNightMode());
        editor.apply();
    }
}
