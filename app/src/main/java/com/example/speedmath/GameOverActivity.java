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

import java.util.Random;

public class GameOverActivity extends AppCompatActivity {

    SharedPreferences pref;

    TextView titleLbl;
    TextView messageLbl;
    TextView scoreLbl;
    TextView correctLbl;
    boolean suppressBackButton = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        setup();
        populateFields();
        calculateValues();
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

        messageLbl = findViewById(R.id.gameOverMsg);
        titleLbl = findViewById(R.id.gameOverTitle);
        scoreLbl = findViewById(R.id.gameOverScore);
        correctLbl = findViewById(R.id.gameOverCorrect);
    }

    public void populateFields() {

        int lastScore = pref.getInt("tempscore", 0);

        if(lastScore > pref.getInt("hs", 0)) {
            messageLbl.setText("NEW HIGHSCORE!");
        } else {
            if (lastScore == 0) {
                if(pref.getBoolean("practice",false)) {
                    pref.edit().putBoolean("practice", false).apply();
                    messageLbl.setText("READY FOR A REAL RUN?");
                } else {
                    messageLbl.setText("WHAT HAPPENED THERE?");
                }
            } else {
                String tempMsg = "";
                Random random = new Random();
                switch (random.nextInt(3)) {
                    case 0:
                        tempMsg = "WELL DONE, KEEP PRACTICING!";
                        break;
                    case 1:
                        tempMsg = "GOOD, BUT YOU CAN DO BETTER!";
                        break;
                    case 2:
                        tempMsg = "GOOD EFFORT, KEEP GOING!";
                        break;
                }
                messageLbl.setText(tempMsg);
            }
        }

        scoreLbl.setText("SCORE  " + lastScore);
        correctLbl.setText("CORRECT ANSWERS  " + pref.getInt("correct", 0));
    }

    public void calculateValues() {
        int temp = pref.getInt("tempscore", 0);

        if(pref.getInt("hs", 0) < temp) {
            pref.edit().putInt("hs", temp).apply();
        }

        temp = pref.getInt("totalcorrect",0);
        temp += pref.getInt("correct",0);
        pref.edit().putInt("totalcorrect", temp).apply();

        temp = pref.getInt("totalscore", 0);
        temp += pref.getInt("tempscore", 0);
        pref.edit().putInt("totalscore", temp).apply();

        temp = pref.getInt("games", 0);
        temp += 1;
        pref.edit().putInt("games", temp).apply();

        temp = pref.getInt("xp", 0);
        temp += pref.getInt("tempscore",0);

        int level = 0;
        while(temp > 100) {
            level += 1;
            temp -= 100;
        }
        pref.edit().putInt("xp", temp).apply();

        if (level > 0) {
            temp = pref.getInt("level", 1);
            temp += level;
            pref.edit().putInt("level", temp).apply();
        }

        pref.edit().putInt("tempscore",0).apply();
        pref.edit().putInt("correct",0).apply();
    }

    public void returnToMain(View v) {
        disableButtons();
        if(pref.getBoolean("audio",true)) {
            MainActivity.soundPool.play(1, 1, 1, 1, 0, 1f);
        }
        finish();
        startActivity(new Intent(GameOverActivity.this, MainActivity.class));
    }

    public void disableButtons() {
        findViewById(R.id.fowardFromGameOver).setEnabled(false);
        suppressBackButton = true;
    }

    @Override
    public void onBackPressed() {
        if(!suppressBackButton) {
            returnToMain(null);
            finish();
        }
    }

}
