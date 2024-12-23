package ru.vsu.cs.yesikov.seabattle;

import java.util.Objects;

public class KeyPoint {

    private final int x;
    private final int y;

    public KeyPoint(int x, int y, int size) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KeyPoint point = (KeyPoint) o;
        return x == point.x && y == point.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
