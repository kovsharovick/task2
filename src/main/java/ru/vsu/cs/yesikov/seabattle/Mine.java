package ru.vsu.cs.yesikov.seabattle;

public class Mine extends Ship {

    public Mine(int x, int y) {
        super(1, x, y, true);
    }

    @Override
    public boolean checkHit(int x, int y) {
        return this.getX()[0] == x && this.getY()[0] == y;
    }
}
