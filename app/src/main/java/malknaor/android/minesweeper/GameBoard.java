package malknaor.android.minesweeper;

import android.content.Context;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameBoard implements Serializable{
    private static final String TAG = "GameBoard";

    private int gameBoardRowsCount;
    private int gameBoardColsCount;
    private int gameBoardMinesCount;
    private BoardCell[][] gameBoard;
    private List<BoardCell> bombsInBoard;
    private int MIN_X;
    private int MIN_Y;
    private int MAX_X;
    private int MAX_Y;
    private int maxCellsToReveal;
    private int cellsRevealed;

    public GameBoard(Context context, int gameBoardRowsCount, int gameBoardColsCount) {
        Log.d(TAG, "GameBoard: creating new GameBoard");

        this.gameBoardRowsCount = gameBoardRowsCount;
        this.gameBoardColsCount = gameBoardColsCount;
        MIN_X = 0;
        MIN_Y = 0;
        MAX_X = gameBoardRowsCount - 1;
        MAX_Y = gameBoardColsCount - 1;
        cellsRevealed = 0;

        gameBoardMinesCount = gameBoardRowsCount * gameBoardColsCount / 6;

        maxCellsToReveal = gameBoardRowsCount * gameBoardColsCount - gameBoardMinesCount;
        gameBoard = new BoardCell[gameBoardRowsCount][gameBoardColsCount];

        Random random = new Random();
        bombsInBoard = new ArrayList<>();

        for (int i = 0; i < gameBoardMinesCount; i++) {
            int row, col;

            do {
                row = random.nextInt(gameBoardRowsCount);
                col = random.nextInt(gameBoardRowsCount);
            } while (gameBoard[row][col] != null);

            gameBoard[row][col] = new BoardCell(context, row, col, true);
            bombsInBoard.add(gameBoard[row][col]);
        }

        for (int i = 0; i < gameBoardRowsCount; i++) {
            for (int j = 0; j < gameBoardColsCount; j++) {
                if (gameBoard[i][j] == null) {
                    gameBoard[i][j] = new BoardCell(context, i, j, false);
                }
            }
        }

        for (int i = 0; i < gameBoardRowsCount; i++) {
            for (int j = 0; j < gameBoardColsCount; j++) {
                if (!(gameBoard[i][j].getIsMine())) {
                    calcNearByBombs(gameBoard[i][j]);
                }
            }
        }
    }

    public BoardCell[][] getGameBoard() {
        return gameBoard;
    }

    public List<BoardCell> getMinesInBoard() {
        return bombsInBoard;
    }

    /**
     * Count near by mines, and if no mines around will add the neighbors boardCells
     * @param boardCell
     */
    private void calcNearByBombs(BoardCell boardCell) {
        int startPosX = (boardCell.getRowIndex() - 1 < MIN_X) ? boardCell.getRowIndex() : boardCell.getRowIndex() - 1;
        int startPosY = (boardCell.getColIndex() - 1 < MIN_Y) ? boardCell.getColIndex() : boardCell.getColIndex() - 1;
        int endPosX = (boardCell.getRowIndex() + 1 > MAX_X) ? boardCell.getRowIndex() : boardCell.getRowIndex() + 1;
        int endPosY = (boardCell.getColIndex() + 1 > MAX_Y) ? boardCell.getColIndex() : boardCell.getColIndex() + 1;

        for (int i = startPosX; i <= endPosX; i++) {
            for (int j = startPosY; j <= endPosY; j++) {
                if (gameBoard[i][j].getIsMine()) {
                    gameBoard[boardCell.getRowIndex()][boardCell.getColIndex()].setMinesAroundCount(boardCell.getMinesAroundCount() + 1);
                }
            }
        }

        if (boardCell.getMinesAroundCount() == 0) {
            for (int i = startPosX; i <= endPosX; i++) {
                for (int j = startPosY; j <= endPosY; j++) {
                    if ((!gameBoard[i][j].getIsMine()) && (gameBoard[i][j] != boardCell)) {
                        gameBoard[boardCell.getRowIndex()][boardCell.getColIndex()].getNeighbors().add(gameBoard[i][j]);
                    }
                }
            }
        }
    }

    /**
     * Reavel
     * @param boardCell
     */
    public void revealNearByNeighbors(BoardCell boardCell) {
        Log.d(TAG, "revealNearByNeighbors: revealNearByNeighbors was called");

        if (boardCell.getMinesAroundCount() == 0) {
            boardCell.setPressed(true);
            boardCell.setClickable(false);

            for (BoardCell neighbor : boardCell.getNeighbors()) {
                if (!neighbor.isPressed()) {
                    if (neighbor.getMinesAroundCount() == 0) {
                        neighbor.setPressed(true);
                        neighbor.setClickable(false);
                        neighbor.setBackgroundResource(R.drawable.cell_hidden_pressed_md);
                        increaseCellsRevealed();
                    }

                    revealNearByNeighbors(neighbor);
                }
            }
        } else if (!boardCell.isPressed()){
            boardCell.performClick();
        }
    }

    public boolean checkIfUserWin() {
        return this.maxCellsToReveal == this.cellsRevealed;
    }

    public synchronized void increaseCellsRevealed() {
        this.cellsRevealed++;
    }
}
