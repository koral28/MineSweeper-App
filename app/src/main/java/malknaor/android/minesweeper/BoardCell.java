package malknaor.android.minesweeper;

import android.content.Context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BoardCell extends android.support.v7.widget.AppCompatImageButton implements Serializable {
    private static final String TAG = "BoardCell";

    private int rowIndex;
    private int colIndex;
    private int minesAroundCount;
    private boolean isMine;
    private boolean isFlagged;
    private List<BoardCell> neighborsAround;

    public BoardCell(Context context) {
        super(context);
    }

    public BoardCell(Context context, int rowIndex, int colIndex, boolean isMine) {
        super(context);

        this.rowIndex = rowIndex;
        this.colIndex = colIndex;
        this.minesAroundCount = 0;
        this.isMine = isMine;
        isFlagged = false;
        neighborsAround = new ArrayList<>();
        setBackgroundResource(R.drawable.board_buttons_shape);
        setAdjustViewBounds(true);
        setScaleType(ScaleType.CENTER_INSIDE);
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public int getColIndex() {
        return colIndex;
    }

    public int getMinesAroundCount() {
        return minesAroundCount;
    }

    public void setMinesAroundCount(int minesAroundCount) {
        this.minesAroundCount = minesAroundCount;
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public void setFlagged(boolean flagged) {
        isFlagged = flagged;
    }

    public boolean getIsMine() {
        return isMine;
    }

    public List<BoardCell> getNeighbors() {
        return neighborsAround;
    }
}