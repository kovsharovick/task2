package ru.vsu.cs.yesikov.seabattle;

import java.util.ArrayList;
import java.util.HashMap;

import static ru.vsu.cs.yesikov.seabattle.Attack.*;

public class Field {
    private int countPlacedShip = 0;
    private final int[] currentKillArea = new int[4];
    private int[] currentKill;
    private final HashMap<Integer, ArrayList<Ship>> ships = new HashMap<>();
    private final HashMap<Integer, Mine> mines = new HashMap<>();
    private final HashMap<Integer, MineSweeper> minesSweeper = new HashMap<>();
    private final HashMap<Integer, Integer> areaPoint = new HashMap<>();
    private final HashMap<Integer, Integer> areaShips = new HashMap<>();
    private final HashMap<Integer, Integer> areaAttack = new HashMap<>();

    public Field(int countMines, int countMinSweeper) {
        ships.put(1, new ArrayList<>());
        ships.put(2, new ArrayList<>());
        ships.put(3, new ArrayList<>());
        ships.put(4, new ArrayList<>());
    }

    public int[] getKillArea() {
        return currentKillArea;
    }

    public int[] getKillShip() {
        return currentKill;
    }

    public boolean placeShip(int size, int x, int y, boolean horizontal) {
        Ship ship = new Ship(size, x, y, horizontal);
        boolean canPlace = true;
        for (int xi : ship.getX()) {
            for (int yi : ship.getY()) {
                if ((areaPoint.containsKey((xi * 10) + yi) && areaPoint.get((xi * 10) + yi) == yi)
                        || (areaShips.containsKey((xi * 10) + yi) && areaShips.get((xi * 10) + yi) == yi)) {
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
                    areaPoint.put(((xi * 10) + yi), yi);
                }
            }

            for (int xi : ship.getX()) {
                for (int yi : ship.getY()) {
                    areaShips.put(((xi * 10) + yi), yi);
                }
            }
            return true;
        }

        return false;
    }

    public boolean placeMine(int x, int y) {
        Mine mine = new Mine(x, y);
        boolean canPlace = !((areaPoint.containsKey((x * 10) + y) && areaPoint.get((x * 10) + y) == y)
                || (mines.containsKey((x * 10) + y) && mines.get((x * 10) + y).getY()[0] == y));
        if (canPlace) {
            mines.put(((x * 10) + y), mine);
            for (int xi = mine.getAreaX()[0]; xi <= mine.getAreaX()[1]; xi++) {
                for (int yi = mine.getAreaY()[0]; yi <= mine.getAreaY()[1]; yi++) {
                    areaPoint.put(((xi * 10) + yi), yi);
                }
            }
            return true;
        }
        return false;
    }

    public boolean placeMineSweeper(int x, int y) {
        MineSweeper mineSweeper = new MineSweeper(x, y);
        boolean canPlace = !((areaPoint.containsKey((x * 10) + y) && areaPoint.get((x * 10) + y) == y)
                || (minesSweeper.containsKey((x * 10) + y) && minesSweeper.get((x * 10) + y).getY()[0] == y));
        if (canPlace) {
            minesSweeper.put(((x * 10) + y), mineSweeper);
            for (int xi = mineSweeper.getAreaX()[0]; xi <= mineSweeper.getAreaX()[1]; xi++) {
                for (int yi = mineSweeper.getAreaY()[0]; yi <= mineSweeper.getAreaY()[1]; yi++) {
                    areaPoint.put(((xi * 10) + yi), yi);
                }
            }
            return true;
        }
        return false;
    }

    public Attack attack(int x, int y, int size) {
        if (areaAttack.containsKey(((x * 10) + y)) && areaAttack.get(((x * 10) + y)) == y) {
            return UNREAL;
        }
        if (areaShips.containsKey(((x * 10) + y)) && areaShips.get(((x * 10) + y)) == y) {
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
                                    areaAttack.put(((xi * 10) + yi), yi);
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
                        areaAttack.put(((x * 10) + y), y);
                        return HIT;
                    }
                }
            }
        }
        if (mines.containsKey(((x * 10) + y)) && mines.get(((x * 10) + y)).getY()[0] == y) {
            currentKillArea[0] = mines.get(((x * 10) + y)).getAreaX()[0];
            currentKillArea[1] = mines.get(((x * 10) + y)).getAreaX()[1];
            currentKillArea[2] = mines.get(((x * 10) + y)).getAreaY()[0];
            currentKillArea[3] = mines.get(((x * 10) + y)).getAreaY()[1];
            for (int xi = mines.get(((x * 10) + y)).getAreaX()[0]; xi <= mines.get(((x * 10) + y)).getAreaX()[1]; xi++) {
                for (int yi = mines.get(((x * 10) + y)).getAreaY()[0]; yi <= mines.get(((x * 10) + y)).getAreaY()[1]; yi++) {
                    areaAttack.put(((xi * 10) + yi), yi);
                }
            }
            return MINE;
        }
        if (minesSweeper.containsKey(((x * 10) + y)) && minesSweeper.get(((x * 10) + y)).getY()[0] == y) {
            /*areaAttack.put(((x * 10) + y), y);
            areaAttack.put(((x * 10) + y), y + 1);
            areaAttack.put(((x * 10) + y), y - 1);
            areaAttack.put((((x - 1) * 10) + y + 1), y);
            areaAttack.put((((x - 1) * 10) + y - 1), y);
            areaAttack.put((((x - 1) * 10) + y), y);
            areaAttack.put((((x + 1) * 10) + y), y);
            areaAttack.put((((x + 1) * 10) + y + 1), y);
            areaAttack.put((((x + 1) * 10) + y - 1), y);*/
            currentKillArea[0] = minesSweeper.get(((x * 10) + y)).getAreaX()[0];
            currentKillArea[1] = minesSweeper.get(((x * 10) + y)).getAreaX()[1];
            currentKillArea[2] = minesSweeper.get(((x * 10) + y)).getAreaY()[0];
            currentKillArea[3] = minesSweeper.get(((x * 10) + y)).getAreaY()[1];
            for (int xi = minesSweeper.get(((x * 10) + y)).getAreaX()[0]; xi <= minesSweeper.get(((x * 10) + y)).getAreaX()[1]; xi++) {
                for (int yi = minesSweeper.get(((x * 10) + y)).getAreaY()[0]; yi <= minesSweeper.get(((x * 10) + y)).getAreaY()[1]; yi++) {
                    areaAttack.put(((xi * 10) + yi), yi);
                }
            }
            return SWEEPER;
        }
        areaAttack.put(((x * 10) + y), y);
        return NOTHING;
    }

    public boolean deleteMine(int x, int y, int size) {
        if (mines.containsKey((x * 10) + y) && mines.get((x * 10) + y).getY()[0] == y) {
            attack(x, y, size);
            return true;
        }
        return false;
    }

    public Attack deleteShip(int x, int y, int size) {
        if (areaShips.containsKey(((x * 10) + y)) && areaShips.get(((x * 10) + y)) == y) {
            return attack(x, y, size);
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

}
