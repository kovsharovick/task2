package ru.vsu.cs.yesikov.seabattle;

public class Ship {
    private final int[] x;
    private final int[] y;
    private final int length;
    private int hits;
    private final int[] xArea;
    private final int[] yArea;

    public Ship(int length, int x, int y, boolean horizontal) {
        this.length = length;
        this.hits = 0;
        this.x = new int[length];
        this.y = new int[length];

        for (int i = 0; i < length; i++) {
            this.x[i] = horizontal ? x : x + i;
            this.y[i] = horizontal ? y + i : y;
        }

        xArea = new int[]{Math.max(x - 1, 0), Math.min(horizontal ? x + 1 : x + length, 9)};
        //xArea = new int[]{Math.max(x - 1, 0), Math.min(horizontal ? x + length + 1: x + 1, 9)};
        yArea = new int[]{Math.max(y - 1, 0), Math.min(horizontal ? y + length : y + 1, 9)};
        //yArea = new int[]{Math.max(y - 1, 0), Math.min(horizontal ? y + 1 : y + length + 1, 9)};
    }

    public int getLength() {
        return length;
    }

    public boolean checkHit(int x, int y) {
        for (int i = 0; i < length; i++) {
            if (this.x[i] == x && this.y[i] == y) {
                hits++;
                this.x[i] = -1;
                this.y[i] = -1;
                return true;
            }
        }
        return false;
    }

    public int[] getAreaX() {
        return xArea;
    }

    public int[] getAreaY() {
        return yArea;
    }

    public boolean isSunk() {
        return hits >= length;
    }

    public int[] getX() {
        return x;
    }

    public int[] getY() {
        return y;
    }
}