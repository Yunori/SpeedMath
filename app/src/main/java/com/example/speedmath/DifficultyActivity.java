package com.example.speedmath;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

public class DifficultyActivity extends AppCompatActivity {

    int difficulty;

    ToggleButton diffToggle1;
    ToggleButton diffToggle2;
    ToggleButton diffToggle3;
    ToggleButton diffToggle4;
    ToggleButton diffToggle5;
    boolean suppressBackButton = false;

    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty);

        setup();
        setData();

        ViewGroup parent = findViewById(R.id.difficultyLayout);
        View v = getLayoutInflater().inflate(R.layout.activity_difficulty, parent, false);
        toggleChanged(v, false);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbarmenu, menu);
        if(pref.getBoolean("audio", true))
            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.audioon));
        else
            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.audiooff));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_audio) {
            item.setEnabled(false);
            if(pref.getBoolean("audio",true)) {
                pref.edit().putBoolean("audio", false).apply();
                item.setIcon(ContextCompat.getDrawable(this, R.drawable.audiooff));
            }
            else {
                pref.edit().putBoolean("audio", true).apply();
                item.setIcon(ContextCompat.getDrawable(this, R.drawable.audioon));
            }
            item.setEnabled(true);
        }
        return super.onOptionsItemSelected(item);
    }

    public void setup() {
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        diffToggle1 = findViewById(R.id.diff1Toggle);
        diffToggle2 = findViewById(R.id.diff2Toggle);
        diffToggle3 = findViewById(R.id.diff3Toggle);
        diffToggle4 = findViewById(R.id.diff4Toggle);
        diffToggle5 = findViewById(R.id.diff5Toggle);
    }

    public void setData() {
        if(pref.getInt("difficulty", 6) == 6) {
            pref.edit().putInt("difficulty", 1).apply();
            difficulty = 1;
        } else {
            difficulty = pref.getInt("difficulty", 6);
        }

        if(difficulty == 1) {
            diffToggle1.setChecked(true);
        } else if(difficulty == 2) {
            diffToggle2.setChecked(true);
        } else if(difficulty == 3) {
            diffToggle3.setChecked(true);
        } else if(difficulty == 4) {
            diffToggle4.setChecked(true);
        } else if(difficulty == 5) {
            diffToggle5.setChecked(true);
        }
    }

    public void changeDifficulty(View v) {
        toggleChanged(v, true);
    }

    public void toggleChanged(View v, Boolean realPress) {

        if(realPress) {
            ToggleButton currentToggle = (ToggleButton) v;
            if(diffToggle1.getText() != currentToggle.getText()) {
                diffToggle1.setChecked(false);
                diffToggle1.setBackgroundColor(Color.parseColor("#ff0099cc"));
            }
            if(diffToggle2.getText() != currentToggle.getText()) {
                diffToggle2.setChecked(false);
                diffToggle2.setBackgroundColor(Color.parseColor("#ff0099cc"));
            }
            if(diffToggle3.getText() != currentToggle.getText()) {
                diffToggle3.setChecked(false);
                diffToggle3.setBackgroundColor(Color.parseColor("#ff0099cc"));
            }
            if(diffToggle4.getText() != currentToggle.getText()) {
                diffToggle4.setChecked(false);
                diffToggle4.setBackgroundColor(Color.parseColor("#ff0099cc"));
            }
            if(diffToggle5.getText() != currentToggle.getText()) {
                diffToggle5.setChecked(false);
                diffToggle5.setBackgroundColor(Color.parseColor("#ff0099cc"));
            }
        }

        if(diffToggle1.isChecked()) {
            diffToggle1.setBackgroundColor(Color.parseColor("#ff99cc00"));
            diffToggle1.setChecked(true);
            pref.edit().putInt("difficulty", 1).apply();
        }

        if(diffToggle2.isChecked()) {
            diffToggle2.setBackgroundColor(Color.parseColor("#ff99cc00"));
            diffToggle2.setChecked(true);
            pref.edit().putInt("difficulty", 2).apply();
        }

        if(diffToggle3.isChecked()) {
            diffToggle3.setBackgroundColor(Color.parseColor("#ff99cc00"));
            diffToggle3.setChecked(true);
            pref.edit().putInt("difficulty", 3).apply();
        }

        if(diffToggle4.isChecked()) {
            diffToggle4.setBackgroundColor(Color.parseColor("#ff99cc00"));
            diffToggle4.setChecked(true);
            pref.edit().putInt("difficulty", 4).apply();
        }

        if(diffToggle5.isChecked()) {
            diffToggle5.setBackgroundColor(Color.parseColor("#ff99cc00"));
            diffToggle5.setChecked(true);
            pref.edit().putInt("difficulty", 5).apply();
        }
    }

    public void toSettings(View v) {
        disableButtons();
        if(pref.getBoolean("audio",true)) {
            MainActivity.soundPool.play(1, 1, 1, 1, 0, 1f);
        }
        finish();
        startActivity(new Intent(DifficultyActivity.this, SettingsActivity.class));
    }

    public void startGame(View v) {
        disableButtons();
        if(pref.getBoolean("audio",true)) {
            MainActivity.soundPool.play(1, 1, 1, 1, 0, 1f);
        }
        finish();
        startActivity(new Intent(DifficultyActivity.this, GameActivity.class));
    }

    public void disableButtons() {
        findViewById(R.id.startGameBtn).setEnabled(false);
        findViewById(R.id.diffcultyBack).setEnabled(false);
        suppressBackButton = true;
    }

    @Override
    public void onBackPressed() {
        if(!suppressBackButton) {
            disableButtons();
            toSettings(null);
        }
    }

}
