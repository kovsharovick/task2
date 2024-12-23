package ru.vsu.cs.yesikov.seabattle;

public class KeyPoint {

    private final int x;
    private final int y;
    private final int size;

    public KeyPoint(int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() == this.getClass()) {
            return ((KeyPoint) obj).x == this.x && ((KeyPoint) obj).y == this.y;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (x * size) + y;
    }
}
