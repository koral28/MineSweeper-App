package malknaor.android.minesweeper;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LoseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lose_activity);

        //font
        Typeface myFont2 = Typeface.createFromAsset(getAssets(), "fonts/AmaticSC-Regular.ttf");
        Typeface myFont = Typeface.createFromAsset(getAssets(), "fonts/SuezOne-Regular.ttf");

        TextView youLost = findViewById(R.id.you_lose);
        youLost.setTypeface(myFont);
        TextView timeLose = findViewById(R.id.lose_time);
        timeLose.setTypeface(myFont2);
        TextView userLose = findViewById(R.id.user_time);
        userLose.setTypeface(myFont2);

        userLose.setText(this.getIntent().getStringExtra("TIME"));

        //music
        MediaPlayer mySound;
        //music
        mySound = MediaPlayer.create(this, R.raw.aww);
        mySound.start();

        Button playAgain = findViewById(R.id.play_again_lose);
        playAgain.setTypeface(myFont2);

        playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoseActivity.this, GameDifficultySelectionActivity.class);
                startActivity(intent);
                LoseActivity.super.onBackPressed();
            }
        });

        Button mainMenuBtn = findViewById(R.id.main_menu_lose);

        mainMenuBtn.setTypeface(myFont2);
        mainMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoseActivity.super.onBackPressed();
            }
        });

        Button exit = findViewById(R.id.exit_lose);

        exit.setTypeface(myFont2);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}

