package malknaor.android.minesweeper;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import static malknaor.android.minesweeper.R.anim.bounce;
import static malknaor.android.minesweeper.R.anim.fadein;

public class MainActivity extends AppCompatActivity implements DialogInterface.OnClickListener {
    private AnimationDrawable animationDrawable;
    //animation
    private LinearLayout mainLayout;
    //music
    private MediaPlayer mySound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Animation animation = AnimationUtils.loadAnimation(MainActivity.this, bounce);

        //font in english
        Typeface myFont2 = Typeface.createFromAsset(getAssets(), "fonts/AmaticSC-Regular.ttf");
        Typeface myFont = Typeface.createFromAsset(getAssets(), "fonts/SuezOne-Regular.ttf");

        TextView myText = findViewById(R.id.minesweeper);
        myText.setTypeface(myFont);

        //music
        mySound = MediaPlayer.create(this, R.raw.morning);
        mySound.start();

        //it hides the top line with the app name
        getSupportActionBar().hide();

        // init constraintLayout
        //background animation- active gradient
        ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.constraintLayout);
        // initializing animation drawable by getting background from constraint layout
        animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        // setting enter fade animation duration to 5 seconds
        animationDrawable.setEnterFadeDuration(1000);
        // setting exit fade animation duration to 2 seconds
        animationDrawable.setExitFadeDuration(900);
        //button new game leads to choose difficulty
        Button newGame = findViewById(R.id.new_game);
        newGame.setTypeface(myFont2);

        newGame.setOnClickListener(new View.OnClickListener() {
            Animation animation = AnimationUtils.loadAnimation(MainActivity.this, fadein);

            @Override
            public void onClick(View view) {
                view.startAnimation(animation);
                view.animate().withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(MainActivity.this, GameDifficultySelectionActivity.class);
                        startActivity(intent);
                    }
                }).start();
            }
        });

        //button score table leads to high scores
        Button highScores = findViewById(R.id.high_score);
        highScores.setTypeface(myFont2);
        highScores.setOnClickListener(new View.OnClickListener() {
            Animation animation = AnimationUtils.loadAnimation(MainActivity.this, fadein);

            @Override
            public void onClick(View view) {
                view.startAnimation(animation);
                view.animate().withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(MainActivity.this, ScoreTableActivity.class);
                        startActivity(intent);
                    }
                }).start();
            }
        });

        //exit from main screen
        Button exit = findViewById(R.id.exit);
        exit.setTypeface(myFont2);
        exit.setOnClickListener(new View.OnClickListener() {
            Animation animation = AnimationUtils.loadAnimation(MainActivity.this, fadein);

            @Override
            public void onClick(View view) {
                view.startAnimation(animation);
                view.animate().withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Confirm Shutdown").setMessage("Are you sure that you want to exit?")
                                .setPositiveButton("YES", MainActivity.this)
                                .setNegativeButton("NO", MainActivity.this)
                                .setCancelable(false).show();
                    }
                }).start();
            }
        });

        //sound
        final ImageButton sound = findViewById(R.id.voice_settings_btn);
        sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mySound.isPlaying()) {
                    mySound.pause();
                    ((ImageButton) view).setImageResource(R.drawable.ic_volume_off_black_24dp);
                    view.setBackground(getResources().getDrawable(R.drawable.round_bottom));
                } else {
                    mySound.start();
                    ((ImageButton) view).setImageResource(R.drawable.ic_volume_up_black_24dp);
                    view.setBackground(getResources().getDrawable(R.drawable.round_bottom));
                }
            }
        });

        //information about the game dialog
        ImageButton info = findViewById(R.id.instructions_btn);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.instructions_dialog, null);

                builder.setView(mView);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        //send us an email feedback
        ImageButton mailUs = findViewById(R.id.feedback_btn);
        mailUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mySound.pause();
                sound.setImageResource(R.drawable.ic_volume_off_black_24dp);
                sound.setBackground(getResources().getDrawable(R.drawable.round_bottom));

                final Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.setPackage("com.google.android.gm");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"koral.levi22@gmail.com", "malknaor@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Minesweeper Feedback");
                intent.putExtra(Intent.EXTRA_TEXT, "Dear Koral and Naor, " + System.getProperty("line.separator") +
                        System.getProperty("line.separator") +
                        System.getProperty("line.separator") +
                        "Enter your feedback:)");
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Shutdown").setMessage("Are you sure that you want to exit?")
                .setPositiveButton("YES", this).setNegativeButton("NO", this).show();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        switch (i) {
            case DialogInterface.BUTTON_POSITIVE:
                Toast.makeText(this, "See you later!", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                Toast.makeText(this, "Cool!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    //background animation - active gradient
    @Override
    protected void onResume() {
        super.onResume();
        if (animationDrawable != null && !animationDrawable.isRunning()) {
            // start the animation
            animationDrawable.start();
        }

        if (mySound != null && mySound.isPlaying()) {
            mySound.start();
        } else if (mySound != null && !mySound.isPlaying()) {
            mySound.pause();
        }
    }

    //background animation- active gradient
    @Override
    protected void onPause() {
        super.onPause();
        if (animationDrawable != null && animationDrawable.isRunning()) {
            // stop the animation
            animationDrawable.stop();
        }

        if (mySound != null) {
            mySound.pause();
        }

        super.onPause();
    }
}