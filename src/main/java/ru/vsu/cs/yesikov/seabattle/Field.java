package ru.vsu.cs.yesikov.seabattle;

import java.util.ArrayList;
import java.util.HashMap;

public class Field {
    private int countShip = 0;
    private final HashMap<Integer, ArrayList<Ship>> ships = new HashMap<>();
    private final HashMap<Integer, Integer> mines = new HashMap<>();
    private int countMines = 0;
    private int countMinTar = 0;
    private int countSub;
    private final HashMap<Integer, Integer> areaPoint = new HashMap<>();
    private final HashMap<Integer, Integer> areaShips = new HashMap<>();

    public Field(int countMines, int countSub, int countMinTar) {
        this.countMines = countMines;
        this.countSub = countSub;
        this.countMinTar = countMinTar;
        ships.put(1, new ArrayList<>());
        ships.put(2, new ArrayList<>());
        ships.put(3, new ArrayList<>());
        ships.put(4, new ArrayList<>());
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
            countShip++;
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
        if (mines.size() < countMines) {
            if (!areaShips.containsKey((x * 10) + y) || areaShips.get((x * 10) + y) != y) {
                mines.put(((x * 10) + y), y);
                areaShips.put(((x * 10) + y), y);
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public ArrayList<ArrayList<ArrayList<Integer>>> checkMineTarantila(int x, int y) {
        ArrayList<ArrayList<Integer>> resMines = new ArrayList<>();
        ArrayList<ArrayList<Integer>> resShips = new ArrayList<>();
        ArrayList<ArrayList<ArrayList<Integer>>> res = new ArrayList<>();

        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if (areaShips.get(i) != null && areaShips.get(i) == j) {
                    ArrayList<Integer> ship = new ArrayList<>();
                    ship.add(i);
                    ship.add(j);
                    resShips.add(ship);
                }
                if (mines.get(i) != null && mines.get(i) == j) {
                    ArrayList<Integer> mine = new ArrayList<>();
                    mine.add(i);
                    mine.add(j);
                    resMines.add(mine);
                }
            }
        }
        res.add(resMines);
        res.add(resShips);
        return res;
    }

    public boolean allShipsSunk() {
        return countShip > 0;
    }

    public int getCountShip() {
        return countShip;
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

    public int getCountSub() {
        return countSub;
    }

    public int getCountMinTar() {
        return countMinTar;
    }
}
