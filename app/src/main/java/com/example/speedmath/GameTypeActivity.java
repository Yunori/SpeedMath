package com.example.speedmath;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class GameTypeActivity extends AppCompatActivity {
    SharedPreferences pref;
    boolean suppressBackButton = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_type);

        setup();
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
    }

    public void toSettings(View v) {
        disableButtons();
        Button btn = (Button) v;

        if (btn.getText().equals("CHALLENGE")) {
            System.out.println("TYPE SET TO 0");
            pref.edit().putInt("type", 0).apply();
        } else if (btn.getText().equals("CASUAL")) {
            pref.edit().putInt("type", 1).apply();
        } else if (btn.getText().equals("PRACTICE")) {
            pref.edit().putInt("type", 2).apply();
        }

        if(pref.getBoolean("audio",true)) {
            MainActivity.soundPool.play(1, 1, 1, 1, 0, 1f);
        }
        finish();
        startActivity(new Intent(GameTypeActivity.this, SettingsActivity.class));
    }

    public void backToStart(View v) {
        disableButtons();
        if(pref.getBoolean("audio",true)) {
            MainActivity.soundPool.play(1, 1, 1, 1, 0, 1f);
        }
        finish();
        startActivity(new Intent(GameTypeActivity.this, MainActivity.class));
    }

    public void disableButtons() {
        findViewById(R.id.backFromType).setEnabled(false);
        findViewById(R.id.forwardFromType).setEnabled(false);
        findViewById(R.id.forwardFromType1).setEnabled(false);
        findViewById(R.id.forwardFromType2).setEnabled(false);
        suppressBackButton = true;
    }

    @Override
    public void onBackPressed() {
        if(!suppressBackButton) {
            disableButtons();
            backToStart(null);
        }
    }

}
