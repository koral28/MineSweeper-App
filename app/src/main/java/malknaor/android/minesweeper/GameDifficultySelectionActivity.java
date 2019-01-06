package malknaor.android.minesweeper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import static malknaor.android.minesweeper.R.anim.blink_anim;

public class GameDifficultySelectionActivity extends Activity {
    private static final String TAG = "GameDifficultySelection";

    private final int beginnersBoardSize = 10;
    private final int advancedBoardSize = 20;
    private final int professionalBoardSize = 30;
    private final int maxBoardSize = 40;
    private int customBoardRowsCount = 0;
    private int customBoardColsCount = 0;
    private String BOARD_ROWS_COUNT = "BOARD_ROWS_COUNT";
    private String BOARD_COLS_COUNT = "BOARD_COLS_COUNT";
    private String DIFFICULTY = "DIFFICULTY";
    private int boardSize = beginnersBoardSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_difficulty_selection_activity);

        //font in english
        final TextView difficulty = findViewById(R.id.difficulty_text_view);
        final Button levels = findViewById(R.id.levels);
        final Button easy = findViewById(R.id.easy_menu_option);
        final Button medium = findViewById(R.id.medium_menu_option);
        final Button hard = findViewById(R.id.hard_menu_option);
        Button custom = findViewById(R.id.custom_menu_option);

        Button goBtn = findViewById(R.id.goBtn);
        Typeface myFont = Typeface.createFromAsset(getAssets(), "fonts/AmaticSC-Regular.ttf");
        Typeface myFont2 = Typeface.createFromAsset(getAssets(), "fonts/SuezOne-Regular.ttf");
        difficulty.setTypeface(myFont2);
        levels.setTypeface(myFont);
        goBtn.setTypeface(myFont);

        goBtn.setOnClickListener(new View.OnClickListener() {
            Animation animation = AnimationUtils.loadAnimation(GameDifficultySelectionActivity.this, blink_anim);

            @Override
            public void onClick(View view) {

                view.startAnimation(animation);
                view.animate().withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        //save difficulty
                        SharedPreferences sp = getSharedPreferences("Levels", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("levels", levels.getText().toString());
                        editor.apply();

                        Intent intent = new Intent(GameDifficultySelectionActivity.this, GameActivity.class);
                        intent.putExtra(BOARD_ROWS_COUNT, boardSize);
                        intent.putExtra(BOARD_COLS_COUNT, boardSize);
                        intent.putExtra(DIFFICULTY, levels.getText());
                        startActivity(intent);
                        GameDifficultySelectionActivity.super.onBackPressed();
                    }
                }).start();
            }
        });

        //pop up menu
        levels.setOnClickListener(new View.OnClickListener() {
            Animation animation = AnimationUtils.loadAnimation(GameDifficultySelectionActivity.this, blink_anim);
            View currentView;

            @Override
            public void onClick(View view) {
                currentView = view;

                view.startAnimation(animation);
                view.animate().withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        PopupMenu popupMenu = new PopupMenu(GameDifficultySelectionActivity.this, currentView);
                        getMenuInflater().inflate(R.menu.difficulty_select_menu, popupMenu.getMenu());

                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                switch (menuItem.getItemId()) {
                                    case R.id.easy_menu_option:
                                        Toast.makeText(GameDifficultySelectionActivity.this, "Easy level selected", Toast.LENGTH_SHORT).show();
                                        levels.setText(R.string.easy);
                                        boardSize = beginnersBoardSize;
                                        return true;
                                    case R.id.medium_menu_option:
                                        Toast.makeText(GameDifficultySelectionActivity.this, "Medium level selected", Toast.LENGTH_SHORT).show();
                                        levels.setText(R.string.medium);
                                        boardSize = advancedBoardSize;
                                        return true;
                                    case R.id.hard_menu_option:
                                        Toast.makeText(GameDifficultySelectionActivity.this, "Hard level selected", Toast.LENGTH_SHORT).show();
                                        levels.setText(R.string.hard);
                                        boardSize = professionalBoardSize;
                                        return true;
                                    case R.id.custom_menu_option:
                                        Toast.makeText(GameDifficultySelectionActivity.this, "Custom selected", Toast.LENGTH_SHORT).show();
                                        levels.setText(R.string.custom);
                                        createCustomBoardSizeDialog().show();
                                        return true;
                                    default:
                                        Toast.makeText(GameDifficultySelectionActivity.this, "Custom selected", Toast.LENGTH_SHORT).show();
                                        levels.setText(R.string.easy);
                                        boardSize = beginnersBoardSize;
                                        return false;
                                }
                            }
                        });

                        popupMenu.show();
                    }
                }).start();
            }
        });
    }

    private AlertDialog createCustomBoardSizeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText boardSizeEditText = new EditText(builder.getContext());

        builder.setTitle("Board Size Input");
        boardSizeEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(boardSizeEditText);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "onClick: CustomBoardSizeDialog - Button ok click");
                try {
                    customBoardRowsCount = Integer.parseInt(boardSizeEditText.getText().toString());
                    /*customBoardColsCount = Integer.parseInt(boardSizeEditText.getText().toString());*/

                    if ((customBoardRowsCount >= beginnersBoardSize && customBoardRowsCount <= maxBoardSize)) {
                        boardSize = customBoardRowsCount;
                    } else {
                        throw new NumberFormatException();
                    }

                } catch (NumberFormatException e) {
                    Toast.makeText(GameDifficultySelectionActivity.this, "Invalid size,\n Try a size between " + beginnersBoardSize + " - " + maxBoardSize, Toast.LENGTH_SHORT).show();
                    boardSize = beginnersBoardSize;
                    ((Button) findViewById(R.id.levels)).setText(R.string.easy);
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "onClick: CustomBoardSizeDialog - Button cancel click");
                dialog.cancel();
            }
        });

        return builder.create();
    }
}
