package malknaor.android.minesweeper;

import java.io.Serializable;

/**
 * TableEntry
 */
public class TableEntry implements Serializable{
    private String playerName;
    private String playerTime;
    private String playerDifficulty;

    public TableEntry(String playerName, String playerTime, String playerDifficulty) {
        this.playerName = playerName;
        this.playerTime = playerTime;
        this.playerDifficulty = playerDifficulty;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getPlayerTime() {
        return playerTime;
    }

    public String getPlayerDifficulty() {
        return playerDifficulty;
    }
}
