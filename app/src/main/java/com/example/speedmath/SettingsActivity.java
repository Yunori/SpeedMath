package com.example.speedmath;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

public class SettingsActivity extends AppCompatActivity {

    ToggleButton additionBtn;
    ToggleButton subtractionBtn;
    ToggleButton multiplicationBtn;
    ToggleButton divisionBtn;
    boolean suppressBackButton = false;

    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setup();
        setData();

        ViewGroup parent = findViewById(R.id.settingsLayout);
        View view = getLayoutInflater().inflate(R.layout.activity_settings, parent, false);
        isToggleActive(view);
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

        additionBtn = findViewById(R.id.additionToggle);
        subtractionBtn = findViewById(R.id.subtractionToggle);
        multiplicationBtn = findViewById(R.id.multiplicationToggle);
        divisionBtn = findViewById(R.id.divisionToggle);
    }

    public void setData() {
        if(pref.getInt("firstSettings", 0) == 0) {
            pref.edit().putInt("firstSettings", 1).apply();
            pref.edit().putBoolean("addition", true).apply();
            pref.edit().putBoolean("subtraction", false).apply();
            pref.edit().putBoolean("multiplication", false).apply();
            pref.edit().putBoolean("division", false).apply();
            additionBtn.setChecked(true);
        }

        if(pref.getBoolean("addition", true)) {
            additionBtn.setChecked(true);
        }
        if(pref.getBoolean("subtraction", true)) {
            subtractionBtn.setChecked(true);
        }
        if(pref.getBoolean("multiplication", true)) {
            multiplicationBtn.setChecked(true);
        }
        if(pref.getBoolean("division", true)) {
            divisionBtn.setChecked(true);
        }

    }

    public void isToggleActive(View v) {

        if(additionBtn.isChecked()) {
            additionBtn.setBackgroundColor(Color.parseColor("#ff99cc00"));
            additionBtn.setChecked(true);
            pref.edit().putBoolean("addition", true).apply();
        } else if(!additionBtn.isChecked()) {
            additionBtn.setBackgroundColor(Color.parseColor("#ff0099cc"));
            additionBtn.setChecked(false);
            pref.edit().putBoolean("addition", false).apply();
        }

        if(subtractionBtn.isChecked()) {
            subtractionBtn.setBackgroundColor(Color.parseColor("#ff99cc00"));
            subtractionBtn.setChecked(true);
            pref.edit().putBoolean("subtraction", true).apply();
        } else if(!subtractionBtn.isChecked()) {
            subtractionBtn.setBackgroundColor(Color.parseColor("#ff0099cc"));
            subtractionBtn.setChecked(false);
            pref.edit().putBoolean("subtraction", false).apply();
        }

        if(multiplicationBtn.isChecked()) {
            multiplicationBtn.setBackgroundColor(Color.parseColor("#ff99cc00"));
            multiplicationBtn.setChecked(true);
            pref.edit().putBoolean("multiplication", true).apply();
        } else if(!multiplicationBtn.isChecked()) {
            multiplicationBtn.setBackgroundColor(Color.parseColor("#ff0099cc"));
            multiplicationBtn.setChecked(false);
            pref.edit().putBoolean("multiplication", false).apply();
        }

        if(divisionBtn.isChecked()) {
            divisionBtn.setBackgroundColor(Color.parseColor("#ff99cc00"));
            divisionBtn.setChecked(true);
            pref.edit().putBoolean("division", true).apply();
        } else if(!divisionBtn.isChecked()) {
            divisionBtn.setBackgroundColor(Color.parseColor("#ff0099cc"));
            divisionBtn.setChecked(false);
            pref.edit().putBoolean("division", false).apply();
        }
    }


    public void toMain(View v) {
        disableButtons();
        if(pref.getBoolean("audio",true)) {
            MainActivity.soundPool.play(1, 1, 1, 1, 0, 1f);
        }
        finish();
        startActivity(new Intent(SettingsActivity.this, GameTypeActivity.class));
    }

    public void toDifficultyPage(View v) {
        disableButtons();
        int temp = 0;
        if(pref.getBoolean("addition", false)) {
            temp += 1;
        }
        if(pref.getBoolean("subtraction", false)) {
            temp += 1;
        }
        if(pref.getBoolean("multiplication", false)) {
            temp += 1;
        }
        if(pref.getBoolean("division", false)) {
            temp += 1;
        }

        if(temp > 0) {
            if(pref.getBoolean("audio",true)) {
                MainActivity.soundPool.play(1, 1, 1, 1, 0, 1f);
            }
            finish();
            startActivity(new Intent(SettingsActivity.this, DifficultyActivity.class));
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(SettingsActivity.this).create();
            alertDialog.setTitle("NO MATH TYPE SELECTED");
            alertDialog.setMessage("PLEASE SELECT AT LEAST ONE MATH TYPE");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }
    }

    public void disableButtons() {
        findViewById(R.id.settingsCancel).setEnabled(false);
        findViewById(R.id.settingsStart).setEnabled(false);
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
