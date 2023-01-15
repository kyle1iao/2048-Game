package org.cis1200.TwentyFortyEight;

public class GameTile {
    private int tileValue;
    private boolean combined;

    public GameTile(int value) {
        this.tileValue = value;
    }

    public int getTileValue() {
        return this.tileValue;
    }

    public void setCombined(boolean combined) {
        this.combined = combined;
    }

    public boolean canCombineWith(GameTile otherGameTile) {
        boolean ccw = (!this.combined && otherGameTile != null && !otherGameTile.combined &&
                this.tileValue == otherGameTile.getTileValue());
        return ccw;
    }

    public int combineWith(GameTile otherGameTile) {
        if (canCombineWith(otherGameTile)) {
            this.tileValue *= 2;
            return this.tileValue;
        }
        return -1;
    }
}
