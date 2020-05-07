package com.example.speedmath;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class StatsActivity extends AppCompatActivity {

    SharedPreferences pref;

    TextView highScoreLbl;
    TextView totalScoreLbl;
    TextView totalCorrectLbl;
    TextView totalGamesLbl;
    boolean suppressBackButton = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        setup();
        populateFields();
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

        highScoreLbl = findViewById(R.id.statsHS);
        totalScoreLbl = findViewById(R.id.statsTotalScore);
        totalCorrectLbl = findViewById(R.id.statsTotalCorrect);
        totalGamesLbl = findViewById(R.id.statsGames);
    }

    public void populateFields() {
        highScoreLbl.setText("HIGHEST SCORE  " + pref.getInt("hs",0));
        totalScoreLbl.setText("COMBINED TOTAL SCORE  " + pref.getInt("totalscore",0));
        totalCorrectLbl.setText("TOTAL CORRECT ANSWERS  " + pref.getInt("totalcorrect",0));
        totalGamesLbl.setText("TOTAL PLAYED GAMES  " + pref.getInt("games",0));
    }

    public void toMain(View v) {
        disableButtons();
        if(pref.getBoolean("audio",true)) {
            MainActivity.soundPool.play(1, 1, 1, 1, 0, 1f);
        }
        finish();
        startActivity(new Intent(StatsActivity.this, MainActivity.class));
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
            toMain(null);
        }
    }

}
