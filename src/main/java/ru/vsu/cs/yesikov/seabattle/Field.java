package ru.vsu.cs.yesikov.seabattle;

import java.util.ArrayList;
import java.util.HashMap;

import static ru.vsu.cs.yesikov.seabattle.Attack.*;

public class Field {
    private final int size;
    private int countPlacedShip = 0;
    private final int[] currentKillArea = new int[4];
    private int[] currentKill;
    private final HashMap<Integer, ArrayList<Ship>> ships = new HashMap<>();
    private final HashMap<KeyPoint, Mine> mines = new HashMap<>();
    private final HashMap<KeyPoint, MineSweeper> minesSweeper = new HashMap<>();
    private final HashMap<KeyPoint, Integer> areaPoint = new HashMap<>();
    private final HashMap<KeyPoint, Integer> areaShips = new HashMap<>();
    private final HashMap<KeyPoint, Integer> areaAttack = new HashMap<>();

    public Field(int size) {
        ships.put(1, new ArrayList<>());
        ships.put(2, new ArrayList<>());
        ships.put(3, new ArrayList<>());
        ships.put(4, new ArrayList<>());
        this.size = size;
    }

    public int[] getKillArea() {
        return currentKillArea;
    }

    public int[] getKillShip() {
        return currentKill;
    }

    public boolean placeShip(int size, int x, int y, boolean horizontal) {
        Ship ship = new Ship(size, x, y, horizontal);
        KeyPoint key;
        boolean canPlace = true;
        for (int xi : ship.getX()) {
            for (int yi : ship.getY()) {
                key = new KeyPoint(xi, yi, this.size);
                if ((areaPoint.containsKey(key) && areaPoint.get(key) == yi)
                        || (areaShips.containsKey(key) && areaShips.get(key) == yi)) {
                    canPlace = false;
                    break;
                }
            }
            if (!canPlace) {
                break;
            }
        }

        if (canPlace) {
            ships.get(size).add(ship);
            countPlacedShip++;
            for (int xi = ship.getAreaX()[0]; xi <= ship.getAreaX()[1]; xi++) {
                for (int yi = ship.getAreaY()[0]; yi <= ship.getAreaY()[1]; yi++) {
                    areaPoint.put(new KeyPoint(xi, yi, this.size), yi);
                }
            }

            for (int xi : ship.getX()) {
                for (int yi : ship.getY()) {
                    areaShips.put(new KeyPoint(xi, yi, this.size), yi);
                }
            }
            return true;
        }

        return false;
    }

    public boolean placeMine(int x, int y) {
        Mine mine = new Mine(x, y);
        KeyPoint key = new KeyPoint(x, y, size);
        boolean canPlace = !((areaPoint.containsKey(key) && areaPoint.get(key) == y)
                || (mines.containsKey(key) && mines.get(key).getY()[0] == y));
        if (canPlace) {
            mines.put(key, mine);
            for (int xi = mine.getAreaX()[0]; xi <= mine.getAreaX()[1]; xi++) {
                for (int yi = mine.getAreaY()[0]; yi <= mine.getAreaY()[1]; yi++) {
                    areaPoint.put(new KeyPoint(xi, yi, size), yi);
                }
            }
            return true;
        }
        return false;
    }

    public boolean placeMineSweeper(int x, int y) {
        MineSweeper mineSweeper = new MineSweeper(x, y);
        KeyPoint key = new KeyPoint(x, y, size);
        boolean canPlace = !((areaPoint.containsKey(key) && areaPoint.get(key) == y)
                || (minesSweeper.containsKey(key) && minesSweeper.get(key).getY()[0] == y));
        if (canPlace) {
            minesSweeper.put(key, mineSweeper);
            for (int xi = mineSweeper.getAreaX()[0]; xi <= mineSweeper.getAreaX()[1]; xi++) {
                for (int yi = mineSweeper.getAreaY()[0]; yi <= mineSweeper.getAreaY()[1]; yi++) {
                    areaPoint.put(new KeyPoint(xi, yi, size), yi);
                }
            }
            return true;
        }
        return false;
    }

    public Attack attack(int x, int y) {
        KeyPoint key = new KeyPoint(x, y, size);
        if (areaAttack.containsKey(key) && areaAttack.get(key) == y) {
            return UNREAL;
        }
        if (areaShips.containsKey(key) && areaShips.get(key) == y) {
            for (int j = 1; j <= 4; j++) {
                for (Ship i : ships.get(j)) {
                    if (i.checkHit(x, y)) {
                        if (i.isSunk()) {
                            ships.get(j).remove(i);
                            countPlacedShip--;
                            currentKillArea[0] = i.getAreaX()[0];
                            currentKillArea[1] = i.getAreaX()[1];
                            currentKillArea[2] = i.getAreaY()[0];
                            currentKillArea[3] = i.getAreaY()[1];
                            for (int xi = i.getAreaX()[0]; xi <= i.getAreaX()[1]; xi++) {
                                for (int yi = i.getAreaY()[0]; yi <= i.getAreaY()[1]; yi++) {
                                    areaAttack.put(new KeyPoint(xi, yi, size), yi);
                                }
                            }
                            int[] shipX = i.getX();
                            int[] shipY = i.getY();
                            currentKill = new int[j];
                            for (int k = 0; k < j; k++) {
                                currentKill[k] = (shipX[k] * size) + shipY[k];
                            }
                            return KILL;
                        }
                        areaAttack.put(key, y);
                        return HIT;
                    }
                }
            }
        }
        if (mines.containsKey(key) && mines.get(key).getY()[0] == y) {
            currentKillArea[0] = mines.get(key).getAreaX()[0];
            currentKillArea[1] = mines.get(key).getAreaX()[1];
            currentKillArea[2] = mines.get(key).getAreaY()[0];
            currentKillArea[3] = mines.get(key).getAreaY()[1];
            for (int xi = mines.get(key).getAreaX()[0]; xi <= mines.get(key).getAreaX()[1]; xi++) {
                for (int yi = mines.get(key).getAreaY()[0]; yi <= mines.get(key).getAreaY()[1]; yi++) {
                    areaAttack.put(new KeyPoint(xi, yi, size), yi);
                }
            }
            return MINE;
        }
        if (minesSweeper.containsKey(key) && minesSweeper.get(key).getY()[0] == y) {
            currentKillArea[0] = minesSweeper.get(key).getAreaX()[0];
            currentKillArea[1] = minesSweeper.get(key).getAreaX()[1];
            currentKillArea[2] = minesSweeper.get(key).getAreaY()[0];
            currentKillArea[3] = minesSweeper.get(key).getAreaY()[1];
            for (int xi = minesSweeper.get(key).getAreaX()[0]; xi <= minesSweeper.get(key).getAreaX()[1]; xi++) {
                for (int yi = minesSweeper.get(key).getAreaY()[0]; yi <= minesSweeper.get(key).getAreaY()[1]; yi++) {
                    areaAttack.put(new KeyPoint(xi, yi, size), yi);
                }
            }
            return SWEEPER;
        }
        areaAttack.put(key, y);
        return NOTHING;
    }

    public boolean deleteMine(int x, int y) {
        KeyPoint key = new KeyPoint(x, y, size);
        if (mines.containsKey(key) && mines.get(key).getY()[0] == y) {
            attack(x, y);
            return true;
        }
        return false;
    }

    public Attack deleteShip(int x, int y) {
        KeyPoint key = new KeyPoint(x, y, size);
        if (areaShips.containsKey(key) && areaShips.get(key) == y) {
            return attack(x, y);
        }
        return UNREAL;
    }

    public boolean allShipsSunk() {
        return countPlacedShip == 0;
    }

    public int getCountPlacedShip() {
        return countPlacedShip;
    }

    public int getCountShip1() {
        return ships.get(1).size();
    }

    public int getCountShip2() {
        return ships.get(2).size();
    }

    public int getCountShip3() {
        return ships.get(3).size();
    }

    public int getCountShip4() {
        return ships.get(4).size();
    }

    public int getCountMines() {
        return mines.size();
    }

    public int getCountMinSweeper() {
        return minesSweeper.size();
    }

    /*
    Функция для генерации уникального ключа для определенных значений Х и У.
    Ключи будут одинаковые, только тогда когда Х1 == Х2 и У1 == У2.
    Создана для того чтобы для каждой точки создавать уникальное значение для ключа в словаре,
    но при этом, чтобы при таких же Х и У, эти значения совпадали.
     */
    private int getKey(int x, int y) {
        return (x * size) + y;
    }

}
