package malknaor.android.minesweeper;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WinActivity extends Activity {
    private String playerTime;
    private String playerDifficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_activity);

        playerTime = this.getIntent().getStringExtra("TIME");
        playerDifficulty = this.getIntent().getStringExtra("DIFFICULTY");

        TextView userTime = findViewById(R.id.user_time);
        TextView userDifficulty = findViewById(R.id.user_difficulty);
        userTime.setText(playerTime);
        userDifficulty.setText(playerDifficulty);

        //font
        Typeface myFont2 = Typeface.createFromAsset(getAssets(), "fonts/AmaticSC-Regular.ttf");
        Typeface myFont = Typeface.createFromAsset(getAssets(), "fonts/SuezOne-Regular.ttf");

        final TextView congratulations = findViewById(R.id.congratulations);
        congratulations.setTypeface(myFont);
        final TextView youMadeIt = findViewById(R.id.you_made_it);
        youMadeIt.setTypeface(myFont2);
        final TextView time = findViewById(R.id.winner_time);
        time.setTypeface(myFont2);
        userTime.setTypeface(myFont2);
        final TextView difficulty_win = findViewById(R.id.difficulty_win);
        difficulty_win.setTypeface(myFont2);
        final TextView user_difficulty = findViewById(R.id.user_difficulty);
        user_difficulty.setTypeface(myFont2);

        //music
        MediaPlayer mySound;
        //music
        mySound = MediaPlayer.create(this, R.raw.win);
        mySound.start();

        Button scoreTable = findViewById(R.id.score_table_win);
        scoreTable.setTypeface(myFont2);
        scoreTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WinActivity.this, ScoreTableActivity.class);
                startActivity(intent);
                WinActivity.super.onBackPressed();
            }
        });

        Button mainMenuWin = findViewById(R.id.main_menu_win);
        mainMenuWin.setTypeface(myFont2);
        mainMenuWin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WinActivity.super.onBackPressed();
            }
        });

        Button exit = findViewById(R.id.exit_win);

        exit.setTypeface(myFont2);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WinActivity.super.onBackPressed();
            }
        });

        //enter your name to score table
        final Button submit = findViewById(R.id.submit);
        submit.setTypeface(myFont2);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WinActivity.this, UserNameActivity.class);
                intent.putExtra("DIFFICULTY", playerDifficulty);
                intent.putExtra("TIME", playerTime);
                startActivity(intent);
                WinActivity.super.onBackPressed();
            }
        });
    }
}

