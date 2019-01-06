package malknaor.android.minesweeper;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Chronometer;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static malknaor.android.minesweeper.R.anim.bounce;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "GameActivity";
    public static final String BOARD_ROWS_COUNT = "BOARD_ROWS_COUNT";
    public static final String BOARD_COLS_COUNT = "BOARD_COLS_COUNT";
    public static final String GAME_BOARD = "GAME_BOARD";
    public static final String IS_FLAGGING_MODE = "IS_FLAGGING_MODE";
    public static final String IS_DIGGING_MODE = "IS_DIGGING_MODE";
    public static final String IS_GAME_PAUSED = "IS_GAME_PAUSED";
    public static final String BOARD_MINES_LIST = "BOARD_MINES_LIST";
    public static final String CHRONOMETER = "CHRONOMETER";
    public static final String TIME = "TIME";

    private int gameBoardRowsCount;
    private int gameBoardColsCount;
    private GameBoard gameBoard;
    private List<BoardCell> boardMinesList;
    private View.OnClickListener boardCellsListener;
    private boolean isFlaggingMode;
    private boolean isDiggingMode;
    private boolean isGamePaused;
    private boolean quitAndSave;
    private Bitmap flagBitMap;
    private long chronometerLastPauseTime;
    // Buttons for Animation
    private ImageButton currentSelectedImageButton;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //set content view AFTER ABOVE sequence (to avoid crash)
        setContentView(R.layout.activity_game);

        flagBitMap = combineImages(R.drawable.cell_hidden_md, R.drawable.cell_button_flag_md);

        // Game Timer
        final Chronometer chronometer = findViewById(R.id.chronometer);

        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer cArg) {
                long time = SystemClock.elapsedRealtime() - cArg.getBase();
                int h = (int) (time / 3600000);
                int m = (int) (time - h * 3600000) / 60000;
                int s = (int) (time - h * 3600000 - m * 60000) / 1000;
                String hh = h < 10 ? "0" + h : h + "";
                String mm = m < 10 ? "0" + m : m + "";
                String ss = s < 10 ? "0" + s : s + "";
                cArg.setText(hh + ":" + mm + ":" + ss);

                if (!isGamePaused) {
                    chronometerLastPauseTime = SystemClock.elapsedRealtime();
                }
            }
        });

        // Game Board
        gameBoardRowsCount = this.getIntent().getIntExtra(BOARD_ROWS_COUNT, 0);
        gameBoardColsCount = this.getIntent().getIntExtra(BOARD_COLS_COUNT, 0);

        gameBoard = new GameBoard(this, gameBoardRowsCount, gameBoardColsCount);
        boardMinesList = gameBoard.getMinesInBoard();
        isDiggingMode = true;
        isFlaggingMode = false;
        isGamePaused = false;
        quitAndSave = false;

        final GridLayout gridLayout = findViewById(R.id.gameBoardLayout);
        boardCellsListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: boardCellListener onClick was called");
                BoardCell boardCell = (BoardCell) v;

                if (isDiggingMode) {
                    if (!(gameBoard.getGameBoard()[boardCell.getRowIndex()][boardCell.getColIndex()]).getIsMine()) {
                        gameBoard.increaseCellsRevealed();
                        boardCell.setPressed(true);
                        boardCell.setClickable(false);

                        switch (gameBoard.getGameBoard()[boardCell.getRowIndex()][boardCell.getColIndex()].getMinesAroundCount()) {
                            case 0:
                                gameBoard.getGameBoard()[boardCell.getRowIndex()][boardCell.getColIndex()].setImageResource(R.drawable.cell_hidden_pressed_md);
                                gameBoard.revealNearByNeighbors(boardCell);
                                break;
                            case 1:
                                gameBoard.getGameBoard()[boardCell.getRowIndex()][boardCell.getColIndex()].setImageResource(R.drawable.cell_one_md);
                                break;
                            case 2:
                                gameBoard.getGameBoard()[boardCell.getRowIndex()][boardCell.getColIndex()].setImageResource(R.drawable.cell_two_md);
                                break;
                            case 3:
                                gameBoard.getGameBoard()[boardCell.getRowIndex()][boardCell.getColIndex()].setImageResource(R.drawable.cell_three_md);
                                break;
                            case 4:
                                gameBoard.getGameBoard()[boardCell.getRowIndex()][boardCell.getColIndex()].setImageResource(R.drawable.cell_four_md);
                                break;
                            case 5:
                                gameBoard.getGameBoard()[boardCell.getRowIndex()][boardCell.getColIndex()].setImageResource(R.drawable.cell_five_md);
                                break;
                            case 6:
                                gameBoard.getGameBoard()[boardCell.getRowIndex()][boardCell.getColIndex()].setImageResource(R.drawable.cell_six_md);
                                break;
                            case 7:
                                gameBoard.getGameBoard()[boardCell.getRowIndex()][boardCell.getColIndex()].setImageResource(R.drawable.cell_seven_md);
                                break;
                            case 8:
                                gameBoard.getGameBoard()[boardCell.getRowIndex()][boardCell.getColIndex()].setImageResource(R.drawable.cell_eight_md);
                                break;
                        }

                        //////// GAME ENDED - User Won The Game, next move to view player scores and time
                        if (gameBoard.checkIfUserWin()) {
                            Toast.makeText(GameActivity.this, "Well Played, You Won", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(GameActivity.this, WinActivity.class);
                            intent.putExtra("DIFFICULTY", getIntent().getStringExtra("DIFFICULTY"));
                            chronometer.stop();
                            intent.putExtra(TIME, chronometer.getText());
                            startActivity(intent);
                            GameActivity.super.onBackPressed();
                        }
                    } else {
                        for (BoardCell cell : boardMinesList) {
                            boardCell.setPressed(true);
                            gameBoard.getGameBoard()[cell.getRowIndex()][cell.getColIndex()].setImageResource(R.drawable.cell_mine_md);
                            boardCell.setClickable(false);
                        }

                        //////// GAME OVER - User Lose, next move to view player scores and time
                        gridLayout.setClickable(false);
                        Toast.makeText(GameActivity.this, "You Lost, You can always try Again", Toast.LENGTH_LONG).show();

                        chronometer.stop();
                        Intent intent = new Intent(GameActivity.this, LoseActivity.class);
                        intent.putExtra(TIME, chronometer.getText());
                        startActivity(intent);
                        GameActivity.super.onBackPressed();
                    }
                } else if (isFlaggingMode) {
                    // if not flagged - Change current cellImage to Flagged
                    // else - Change current cellImage to UnFlagged
                    if (!gameBoard.getGameBoard()[boardCell.getRowIndex()][boardCell.getColIndex()].isFlagged()) {
                        gameBoard.getGameBoard()[boardCell.getRowIndex()][boardCell.getColIndex()].setImageBitmap(flagBitMap);
                        gameBoard.getGameBoard()[boardCell.getRowIndex()][boardCell.getColIndex()].setFlagged(true);
                    } else {
                        gameBoard.getGameBoard()[boardCell.getRowIndex()][boardCell.getColIndex()].setImageResource(R.drawable.cell_hidden_md);
                        gameBoard.getGameBoard()[boardCell.getRowIndex()][boardCell.getColIndex()].setFlagged(false);
                    }
                }
            }
        };

        gridLayout.setRowCount(gameBoardRowsCount);
        gridLayout.setColumnCount(gameBoardColsCount);
        for (int i = 0; i < gameBoardRowsCount; i++) {
            for (int j = 0; j < gameBoardRowsCount; j++) {
                gridLayout.addView(gameBoard.getGameBoard()[i][j]);
                gameBoard.getGameBoard()[i][j].setImageResource(R.drawable.cell_hidden_md);
                gameBoard.getGameBoard()[i][j].setOnClickListener(boardCellsListener);
            }
        }

        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();

        final TextView board_mines_count_tv = findViewById(R.id.board_mines_count_tv);
        board_mines_count_tv.setText(gameBoard.getMinesInBoard().size() + "");

        // Game Mode Buttons
        final ImageButton digModeBtn = findViewById(R.id.gameBtnDig);
        digModeBtn.setOnClickListener(GameActivity.this);
        digModeBtn.getBackground().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);

        final ImageButton flagModeBtn = findViewById(R.id.gameBtnFlag);
        flagModeBtn.setOnClickListener(GameActivity.this);
    }

    /**
     * Create Bitmap from two images
     *
     * @param frame
     * @param image
     * @return new bitmap from the two images
     */
    private Bitmap combineImages(int frame, int image) {
        Bitmap box = BitmapFactory.decodeResource(getResources(), frame);
        Bitmap close = BitmapFactory.decodeResource(getResources(), image);
        Bitmap bitmapCreate = Bitmap.createBitmap(box.getWidth(), box.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas comboImage = new Canvas(bitmapCreate);

        comboImage.drawBitmap(box, 0, 0, null);
        comboImage.drawBitmap(close, 10, 10, null);

        try {
            box.recycle();
            close.recycle();
            box = null;
            close = null;
        } catch (Exception e) {
            Log.d(TAG, "combineImages: Exception was thrown while trying to recycle bitmap");
        }

        return bitmapCreate;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.in_game_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        isGamePaused = true;

        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.continue_menu_option:
                ((Chronometer) findViewById(R.id.chronometer)).setBase(((Chronometer) findViewById(R.id.chronometer)).getBase() + SystemClock.elapsedRealtime() - chronometerLastPauseTime);
                ((Chronometer) findViewById(R.id.chronometer)).start();
                isGamePaused = false;
                Toast.makeText(this, "Let's Continue Playing", Toast.LENGTH_SHORT).show();
                break;

            case R.id.quit_and_save_menu_option:
                // TODO - Save Game State
                Toast.makeText(this, "Game Saved, See You Later", Toast.LENGTH_SHORT).show();
                quitAndSave = true;
                createBackPressDialog().show();
                break;

            case R.id.quit_menu_option:
                Toast.makeText(this, "See You Later", Toast.LENGTH_SHORT).show();
                quitAndSave = false;
                createBackPressDialog().show();
                break;

            default:
                ((Chronometer) findViewById(R.id.chronometer)).setBase(((Chronometer) findViewById(R.id.chronometer)).getBase() + SystemClock.elapsedRealtime() - chronometerLastPauseTime);
                ((Chronometer) findViewById(R.id.chronometer)).start();
                isGamePaused = false;
                quitAndSave = false;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO - Save Instance state on device storage

        outState.putSerializable(GAME_BOARD, gameBoard);
        outState.putBoolean(IS_DIGGING_MODE, isDiggingMode);
        outState.putBoolean(IS_FLAGGING_MODE, isFlaggingMode);
        outState.putBoolean(IS_GAME_PAUSED, isGamePaused);
        outState.putSerializable(BOARD_MINES_LIST, (ArrayList<BoardCell>) boardMinesList);
        outState.putLong(CHRONOMETER, ((Chronometer) findViewById(R.id.chronometer)).getBase());

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        gameBoard = (GameBoard) savedInstanceState.getSerializable(GAME_BOARD);
        isDiggingMode = savedInstanceState.getBoolean(IS_DIGGING_MODE);
        isFlaggingMode = savedInstanceState.getBoolean(IS_FLAGGING_MODE);
        isGamePaused = savedInstanceState.getBoolean(IS_GAME_PAUSED);
        boardMinesList = (ArrayList<BoardCell>) savedInstanceState.getSerializable(BOARD_MINES_LIST);
        ((Chronometer) findViewById(R.id.chronometer)).setBase(savedInstanceState.getLong(CHRONOMETER));
    }

    @Override
    public void onBackPressed() {
        // TODO - Before going back save game state
        Log.d(TAG, "onBackPressed: onBackPressed event was called");
        isGamePaused = true;
        createBackPressDialog().show();
    }

    @Override
    public void onClick(View v) {
        currentSelectedImageButton = (ImageButton) v;
        Animation animation = AnimationUtils.loadAnimation(GameActivity.this, bounce);

        v.startAnimation(animation);
        v.animate().withEndAction(new Runnable() {
            @Override
            public void run() {
                ImageButton btn = currentSelectedImageButton;

                switch (btn.getId()) {
                    case R.id.gameBtnDig:
                        if (!isDiggingMode) {
                            Toast.makeText(GameActivity.this, "Dig Mode Activated", Toast.LENGTH_SHORT).show();
                            isDiggingMode = true;
                            btn.getBackground().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                            isFlaggingMode = false;
                            findViewById(R.id.gameBtnFlag).getBackground().clearColorFilter();
                            btn.invalidate();
                            findViewById(R.id.gameBtnFlag).invalidate();
                        } else {
                            Toast.makeText(GameActivity.this, "Dig Mode Is Already On", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case R.id.gameBtnFlag:
                        if (!isFlaggingMode) {
                            Toast.makeText(GameActivity.this, "Flag Mode Activated", Toast.LENGTH_SHORT).show();
                            isFlaggingMode = true;
                            btn.getBackground().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                            isDiggingMode = false;
                            findViewById(R.id.gameBtnDig).getBackground().clearColorFilter();
                            btn.invalidate();
                            findViewById(R.id.gameBtnDig).invalidate();
                        } else {
                            Toast.makeText(GameActivity.this, "Flag Mode Is Already On", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        }).start();
    }

    /**
     * Create a Back Press dialog asking if the user ti continue playing or quit
     *
     * @return Back Press dialog
     */
    private AlertDialog createBackPressDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if (!quitAndSave) {
            builder.setTitle("Quit?");

            builder.setPositiveButton("Stay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.d(TAG, "onClick: BackPressedAlertDialog  - Button Stay was clicked");
                    dialog.dismiss();
                }
            });

            builder.setNegativeButton("Leave", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.d(TAG, "onClick: BackPressedAlertDialog - Button Leave was clicked without saving");
                    GameActivity.super.onBackPressed();
                }
            });
        } else {
            builder.setTitle("Quit And Save?");

            builder.setPositiveButton("Stay", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.d(TAG, "onClick: BackPressedAlertDialog  - Button Stay was clicked");
                    dialog.dismiss();
                }
            });

            builder.setNegativeButton("Leave", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO - save to shared preferences
                    Log.d(TAG, "onClick: BackPressedAlertDialog - Button Leave was clicked with saving");
                    GameActivity.super.onBackPressed();
                }
            });
        }

        return builder.create();
    }
}
