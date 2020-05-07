package com.example.speedmath;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameActivity extends AppCompatActivity {

    SharedPreferences pref;
    ScheduledExecutorService executor;

    Button option1;
    Button option2;
    Button option3;
    Button option4;
    TextView scoreLbl;
    TextView correctLbl;
    TextView expression;
    TextView definition;
    ProgressBar timeBar;

    List<Integer> mathTypes = new ArrayList<>();
    int mathAmount = 0;
    int difficulty = 0;
    int type = 0;
    int timeLimit = 0;
    int score = 0;
    int correct = 0;
    int tempScore = 0;
    int additionScore = 0;
    int subtractionScore = 0;
    int multiplicationScore = 0;
    int divisionScore = 0;
    int multiplier = 0;

    String expressionText;
    String definitionText;
    String solution;
    String fake1;
    String fake2;
    String fake3;
    int ansLoc;
    boolean stopRequired = false;
    boolean gameStopped = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        setup();
        getData();
        nextExpression();

        if(type != 2) {
            restartExecutor();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gameactionbarmenu, menu);
        if(pref.getBoolean("audio", true))
            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.audioon));
        else
            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.audiooff));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_exit) {
            item.setEnabled(false);
            onBackPressed();
        }
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

        timeBar = findViewById(R.id.timeBar);

        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);

        scoreLbl = findViewById(R.id.gameScore);
        correctLbl = findViewById(R.id.correctCounter);
        expression = findViewById(R.id.expression);
        definition = findViewById(R.id.definition);
    }

    public void getData() {

        mathTypes.clear();
        String[] tempNames = {"addition", "subtraction", "multiplication", "division"};

        for (int i = 0; i < 4; i++) {
            if(pref.getBoolean(tempNames[i], false)) {
                mathTypes.add(i);
            }
        }

        mathAmount = mathTypes.size();
        difficulty = pref.getInt("difficulty", 1);
        type = pref.getInt("type", 5);

        if(type == 0) {
            timeLimit = 5;
            multiplier = 2;
        } else if(type == 1) {
            timeLimit = 10;
            multiplier = 1;
        } else {
            timeBar.setVisibility(View.INVISIBLE);
            multiplier = 0;
        }
        System.out.println("TYPE:" + timeLimit);

        additionScore = 2 * difficulty;
        subtractionScore = 4 * difficulty;
        multiplicationScore = 6 * difficulty;
        divisionScore = 8 * difficulty;

    }

    Future<?> scheduled;

    public void restartExecutor() {

        if (stopRequired) {
            scheduled.cancel(true);
        } else {
            stopRequired = true;
        }

        Runnable runnable = new Runnable() {
            public void run() {
                endGame();
            }
        };

        executor = Executors.newScheduledThreadPool(1);
        scheduled = executor.scheduleAtFixedRate(runnable, timeLimit, 5, TimeUnit.SECONDS);

    }

    public void scoring() {
        correct += 1;
        score += (tempScore * multiplier);
        String scoreStr = getString(R.string.score) + score;
        String correctStr = getString(R.string.correct) + correct;
        scoreLbl.setText(scoreStr);
        correctLbl.setText(correctStr);
    }

    @Override
    public void onStop () {
        if(scheduled != null) {
            scheduled.cancel(true);
        }
        super.onStop();
    }

    public void resetTimeBar() {
        timeBar.setProgress(100);
        ObjectAnimator progressAnimator = ObjectAnimator.ofInt(timeBar, "progress", 1000, 0);
        progressAnimator.setDuration(timeLimit*1000);
        progressAnimator.setInterpolator(new LinearInterpolator());
        progressAnimator.start();
    }

    public void nextExpression() {

        if (type != 2) {
            resetTimeBar();
        }
        System.out.println("Next!!!!!");
        getNewExpression();

        expression.setText(expressionText);
        definition.setText(definitionText);

        if(ansLoc == 0) {
            option1.setText(solution);
            option2.setText(fake1);
            option3.setText(fake2);
            option4.setText(fake3);
        } else if(ansLoc == 1) {
            option1.setText(fake1);
            option2.setText(solution);
            option3.setText(fake2);
            option4.setText(fake3);
        } else if(ansLoc == 2) {
            option1.setText(fake1);
            option2.setText(fake2);
            option3.setText(solution);
            option4.setText(fake3);
        } else if(ansLoc == 3) {
            option1.setText(fake1);
            option2.setText(fake2);
            option3.setText(fake3);
            option4.setText(solution);
        }

    }

    public void getNewExpression() {

        Random randint = new Random();
        int index = mathTypes.get(randint.nextInt(mathAmount));
        ansLoc = randint.nextInt(4);

        if (index == 0) {

            int tempRand = 0;

            switch (difficulty) {
                case 1:
                    tempRand = 30;
                    break;
                case 2:
                    tempRand = 60;
                    break;
                case 3:
                    tempRand = 100;
                    break;
                case 4:
                    tempRand = 200;
                    break;
                case 5:
                    tempRand = 300;
                    break;
            }

            definitionText = "ADDITION";
            Random randAdd = new Random();
            int first = randAdd.nextInt(tempRand) + 1;
            int second = randAdd.nextInt(tempRand) + 1;
            int tempSolution = first + second;
            solution = Integer.toString(tempSolution);
            expressionText = first + " + " + second;
            tempScore = additionScore;

            getSimpleFakes(randAdd, tempSolution, tempRand);

        } else if (index == 1) {

            int tempRand = 0;

            switch (difficulty) {
                case 1:
                    tempRand = 30;
                    break;
                case 2:
                    tempRand = 60;
                    break;
                case 3:
                    tempRand = 100;
                    break;
                case 4:
                    tempRand = 200;
                    break;
                case 5:
                    tempRand = 300;
                    break;
            }

            definitionText = "SUBTRACTION";
            Random randAdd = new Random();
            int first = randAdd.nextInt(tempRand) + 1;
            int second = randAdd.nextInt(tempRand) + 1;
            int tempSolution = first - second;
            solution = Integer.toString(tempSolution);
            expressionText = first + " - " + second;
            tempScore = subtractionScore;

            getSimpleFakes(randAdd, tempSolution, tempRand);

        } else if (index == 2) {

            int tempRand = 0;

            switch (difficulty) {
                case 1:
                    tempRand = 5;
                    break;
                case 2:
                    tempRand = 9;
                    break;
                case 3:
                    tempRand = 12;
                    break;
                case 4:
                    tempRand = 18;
                    break;
                case 5:
                    tempRand = 24;
                    break;
            }

            definitionText = "MULTIPLICATION";
            Random randAdd = new Random();
            int first = randAdd.nextInt(tempRand) + 1;
            int second = randAdd.nextInt(tempRand) + 1;
            int tempSolution = first * second;
            solution = Integer.toString(tempSolution);
            expressionText = first + " × " + second;
            tempScore = multiplicationScore;

            getSimpleFakes(randAdd, tempSolution, tempRand);

        } else if (index == 3) {

            int tempRand = 0;

            switch (difficulty) {
                case 1:
                    tempRand = 5;
                    break;
                case 2:
                    tempRand = 10;
                    break;
                case 3:
                    tempRand = 15;
                    break;
                case 4:
                    tempRand = 20;
                    break;
                case 5:
                    tempRand = 30;
                    break;
            }

            definitionText = "DIVISION";
            Random randAdd = new Random();
            double first = randAdd.nextInt(tempRand) + 1;
            double second = randAdd.nextInt(tempRand) + 1;
            double tempSolution = first / second;
            System.out.println(tempSolution);
            tempScore = divisionScore;

            if(BigDecimal.valueOf(tempSolution).scale() > 2) {
                solution = Double.toString(round(tempSolution, 2));
            } else {
                solution = Double.toString(tempSolution);
            }

            expressionText = first + " ÷ " + second;

            getComplexFake(randAdd, tempSolution, tempRand);

        }

    }

    public void getComplexFake(Random randAdd, double tempSolution, int tempRand) {
        Double tempFake1, tempFake2, tempFake3;

        do {
            if (randAdd.nextInt(2) == 1) {
                tempFake1 = (tempSolution + randAdd.nextInt(tempRand) + 1);
            } else {
                tempFake1 = (tempSolution - randAdd.nextInt(tempRand) + 1);
            }

        } while(tempFake1.equals(tempSolution));

        if(BigDecimal.valueOf(tempFake1).scale() > 2) {
            fake1 = Double.toString(round(tempFake1, 2));
        } else {
            fake1 = Double.toString(tempFake1);
        }

        do {
            if (randAdd.nextInt(2) == 1) {
                tempFake2 = (tempSolution + randAdd.nextInt(tempRand) + 1);
            } else {
                tempFake2 = (tempSolution - randAdd.nextInt(tempRand) + 1);
            }

        } while(tempFake2.equals(tempSolution) || tempFake2.equals(tempFake1));

        if(BigDecimal.valueOf(tempFake2).scale() > 2) {
            fake2 = Double.toString(round(tempFake2, 2));
        } else {
            fake2 = Double.toString(tempFake2);
        }

        do {
            if (randAdd.nextInt(2) == 1) {
                tempFake3 = (tempSolution + randAdd.nextInt(tempRand) + 1);
            } else {
                tempFake3 = (tempSolution - randAdd.nextInt(tempRand) + 1);
            }

        } while(tempFake3.equals(tempSolution) || tempFake3.equals(tempFake1) || tempFake3.equals(tempFake2));

        if(BigDecimal.valueOf(tempFake3).scale() > 2) {
            fake3 = Double.toString(round(tempFake3, 2));
        } else {
            fake3 = Double.toString(tempFake3);
        }
    }

    public void getSimpleFakes(Random randAdd, int tempSolution, int tempRand) {

        int tempFake1, tempFake2, tempFake3;

        do {
            if (randAdd.nextInt(2) == 1) {
                tempFake1 = tempSolution + randAdd.nextInt(tempRand) + 1;
            } else {
                tempFake1 = tempSolution - randAdd.nextInt(tempRand) + 1;
            }
        } while(tempFake1 == tempSolution);
        fake1 = Integer.toString(tempFake1);

        do {
            if (randAdd.nextInt(2) == 1) {
                tempFake2 = tempSolution + randAdd.nextInt(tempRand) + 1;
            } else {
                tempFake2 = tempSolution - randAdd.nextInt(tempRand) + 1;
            }
        } while(tempFake2 == tempSolution || tempFake2 == tempFake1);
        fake2 = Integer.toString(tempFake2);

        do {
            if (randAdd.nextInt(2) == 1) {
                tempFake3 = tempSolution + randAdd.nextInt(tempRand) + 1;
            } else {
                tempFake3 = tempSolution - randAdd.nextInt(tempRand) + 1;
            }
        } while(tempFake3 == tempSolution || tempFake3 == tempFake1 || tempFake3 == tempFake2);
        fake3 = Integer.toString(tempFake3);
    }

    public void decisionFunc(int choice) {

        if (ansLoc == choice) {
            if(pref.getBoolean("audio",true)) {
                MainActivity.soundPool.play(1, 1, 1, 1, 0, 1f);
            }
            scoring();
            nextExpression();
            if(type != 2) {
                restartExecutor();
            }
        } else {
            if(type != 2) {
                endGame();
            } else {
                if(pref.getBoolean("audio",true)) {
                    MainActivity.soundPool.play(2, 1, 1, 1, 0, 1f);
                }
            }
        }

    }

    public void endGame() {
        if(!gameStopped) {
            gameStopped = true;
            scheduled.cancel(true);
            executor.shutdownNow();
            System.out.println("failed game over");
            pref.edit().putInt("tempscore", score).apply();
            pref.edit().putInt("correct", correct).apply();
            if(pref.getBoolean("audio",true)) {
                MainActivity.soundPool.play(2, 1, 1, 1, 0, 1f);
            }
            finish();
            startActivity(new Intent(GameActivity.this, GameOverActivity.class));
        }
    }

    @Override
    public void onBackPressed() {
        if(type != 2) {
            endGame();
        } else {
            if (pref.getBoolean("audio",true)) {
                MainActivity.soundPool.play(2, 1, 1, 1, 0, 1f);
            }
            pref.edit().putBoolean("practice", true).apply();
            finish();
            startActivity(new Intent(GameActivity.this, GameOverActivity.class));
        }
    }

    public void option1Func(View v) {
        decisionFunc(0);
    }

    public void option2Func(View v) {
        decisionFunc(1);
    }

    public void option3Func(View v) {
        decisionFunc(2);
    }

    public void option4Func(View v) {
        decisionFunc(3);
    }

    public double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

}