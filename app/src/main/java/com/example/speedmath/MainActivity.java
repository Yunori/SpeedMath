package com.example.speedmath;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.HashMap;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    TextView highScoreLbl;
    TextView lvlLbl;
    ProgressBar xpBar;
    ImageButton audioBtn;
    SharedPreferences pref;
    public static SoundPool soundPool;
    HashMap<Integer, Integer> soundPoolMap;
    boolean suppressBackButton = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setup();
        updateMenu();
        lastSetup();
    }

    public void audioFunc(View v) {
        v.setEnabled(false);
        if(pref.getBoolean("audio",true)) {
            pref.edit().putBoolean("audio", false).apply();
            audioBtn.setImageResource(R.drawable.audiooff);
        }
        else {
            pref.edit().putBoolean("audio", true).apply();
            audioBtn.setImageResource(R.drawable.audioon);
        }
        v.setEnabled(true);
    }

    public void setup() {
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);

        highScoreLbl = findViewById(R.id.highScoreLbl);
        lvlLbl = findViewById(R.id.lvlLbl);
        xpBar = findViewById(R.id.xpBar);
        audioBtn = findViewById(R.id.audioBtn);
        if(pref.getBoolean("audio", true))
            audioBtn.setImageResource(R.drawable.audioon);
        else
            audioBtn.setImageResource(R.drawable.audiooff);
    }

    public void updateMenu() {
        String highScore = getString(R.string.highscore) + " " + pref.getInt("hs", 0);
        highScoreLbl.setText(highScore);
        xpBar.setProgress(pref.getInt("xp",0));
        int level = pref.getInt("level",0);
        if(level == 0) {
            pref.edit().putInt("level",1).apply();
            level = 1;
        }
        String lvl = getString(R.string.lvl) + " " + level;
        lvlLbl.setText(lvl);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        updateMenu();
    }

    public void lastSetup() {
        soundPool = new SoundPool.Builder().setMaxStreams(10).build();
        soundPoolMap = new HashMap<>();
        soundPoolMap.put(1, soundPool.load(this, R.raw.touch, 1));
        soundPoolMap.put(2, soundPool.load(this, R.raw.error, 1));
    }

    public void openStats(View v) {
        disableButtons();
        if(pref.getBoolean("audio",true)) {
            soundPool.play(1, 1, 1, 1, 0, 1f);
        }
        finish();
        startActivity(new Intent(MainActivity.this, StatsActivity.class));
    }

    public void startGame(View v) {
        disableButtons();
        if(pref.getBoolean("audio",true)) {
            soundPool.play(1, 1, 1, 1, 0, 1f);
        }
        finish();
        startActivity(new Intent(MainActivity.this, GameTypeActivity.class));
    }

    public void disableButtons() {
        findViewById(R.id.stats).setEnabled(false);
        findViewById(R.id.start).setEnabled(false);
        suppressBackButton = true;
    }

    @Override
    public void onBackPressed() {
        if(!suppressBackButton) {
            disableButtons();
            finish();
        }
    }

}
