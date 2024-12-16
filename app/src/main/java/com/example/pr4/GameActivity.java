package com.example.pr4;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private Button[] buttons = new Button[9];
    private boolean isPlayerTurn = true; // true - крестик, false - нолик
    private int[][] winConditions = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, // строки
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, // столбцы
            {0, 4, 8}, {2, 4, 6} // диагонали
    };
    private int player1Wins = 0;
    private int player2Wins = 0;
    private int draws = 0;
    private SharedPreferences sharedPreferences;
    private TextView statistics;
    private boolean isPlayerVsPlayer;
    private Button buttonRestart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        sharedPreferences = getSharedPreferences("tictactoe_prefs", MODE_PRIVATE);
        loadStatistics();

        statistics = findViewById(R.id.statistics);
        GridLayout gridLayout = findViewById(R.id.gridLayout);
        buttonRestart = findViewById(R.id.button_restart);
        buttonRestart.setOnClickListener(view -> resetGame());

        for (int i = 0; i < buttons.length; i++) {
            int finalI = i;
            buttons[i] = findViewById(getResources().getIdentifier("button" + i, "id", getPackageName()));
            buttons[i].setOnClickListener(view -> handleButtonClick(finalI));
        }

        isPlayerVsPlayer = getIntent().getBooleanExtra("isPlayerVsPlayer", true);
        showStatistics();
    }

    private void handleButtonClick(int index) {
        if (!buttons[index].getText().toString().equals("") || checkForGameEnd()) return;

        buttons[index].setText(isPlayerTurn ? "X" : "O");

        if (checkForWin()) {
            if (isPlayerTurn) {
                player1Wins++;
            } else {
                player2Wins++;
            }
            saveStatistics();
            showStatistics();
        } else if (isBoardFull()) {
            draws++;
            saveStatistics();
            showStatistics();
        } else {
            isPlayerTurn = !isPlayerTurn; // Сменить ход

            if (!isPlayerVsPlayer && !isPlayerTurn) {        botMove();
            }
        }
    }

    private void botMove() {
        Random random = new Random();
        int index;
        do {
            index = random.nextInt(9);
        } while (!buttons[index].getText().toString().equals(""));

        buttons[index].setText("O");

        if (checkForWin()) {
            player2Wins++;
            saveStatistics();
            showStatistics();
        } else if (isBoardFull()) {
            draws++;
            saveStatistics();
            showStatistics();
        } else {
            isPlayerTurn = true;
        }
    }

    private boolean checkForWin() {
        for (int[] condition : winConditions) {
            if (buttons[condition[0]].getText().toString().equals(buttons[condition[1]].getText().toString()) &&
                    buttons[condition[0]].getText().toString().equals(buttons[condition[2]].getText().toString()) &&
                    !buttons[condition[0]].getText().toString().equals("")) {
                return true;
            }
        }
        return false;
    }

    private boolean isBoardFull() {
        for (Button button : buttons) {
            if (button.getText().toString().equals("")) return false;
        }
        return true;
    }

    private boolean checkForGameEnd() {
        return checkForWin() || isBoardFull();
    }

    private void showStatistics() {
        statistics.setText(String.format("Статистика:\nИгрок 1 (X): Побед - %d\nИгрок 2 (O): Побед - %d\nНичьи: %d", player1Wins, player2Wins, draws));
    }

    private void loadStatistics() {
        player1Wins = sharedPreferences.getInt("player1Wins", 0);
        player2Wins = sharedPreferences.getInt("player2Wins", 0);
        draws = sharedPreferences.getInt("draws", 0);
    }

    private void saveStatistics() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("player1Wins", player1Wins);
        editor.putInt("player2Wins", player2Wins);
        editor.putInt("draws", draws);
        editor.apply();
    }

    private void resetGame() {
        for (Button button : buttons) {
            button.setText("");
        }
        isPlayerTurn = true;
        showStatistics();
    }
}