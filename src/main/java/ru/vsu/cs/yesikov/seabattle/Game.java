package ru.vsu.cs.yesikov.seabattle;

import java.util.Scanner;

import static ru.vsu.cs.yesikov.seabattle.Attack.NOTHING;
import static ru.vsu.cs.yesikov.seabattle.Attack.UNREAL;

public class Game {
    private final Field player1Field;
    private final Field player2Field;
    private int currentPlayer;
    private final int size;

    public Game(int size) {
        player1Field = new Field(size);
        player2Field = new Field(size);
        currentPlayer = 1;
        this.size = size;
    }

    public int countOfMines(int player) {
        if (player == 1) {
            return player1Field.getCountMines();
        }
        return player2Field.getCountMines();
    }

    public int countOfShipAll(int player) {
        if (player == 1) {
            return player1Field.getCountPlacedShip();
        }
        return player2Field.getCountPlacedShip();
    }

    public int countOfShip1(int player) {
        if (player == 1) {
            return player1Field.getCountShip1();
        }
        return player2Field.getCountShip1();
    }

    public int countOfShip2(int player) {
        if (player == 1) {
            return player1Field.getCountShip2();
        }
        return player2Field.getCountShip2();
    }

    public int countOfShip3(int player) {
        if (player == 1) {
            return player1Field.getCountShip3();
        }
        return player2Field.getCountShip3();
    }

    public int countOfShip4(int player) {
        if (player == 1) {
            return player1Field.getCountShip4();
        }
        return player2Field.getCountShip4();
    }

    public int countOfMineSweeper(int player) {
        if (player == 1) {
            return player1Field.getCountMinSweeper();
        }
        return player2Field.getCountMinSweeper();
    }

    public boolean placeShip(int size, int x, int y, boolean horizontal) {
        Field currentField = (currentPlayer == 1) ? player1Field : player2Field;
        if (horizontal) {
            if (x < 0 || y < 0 || x > this.size - 1 || y > this.size - size) {
                return false;
            }
        } else {
            if (x < 0 || y < 0 || x > this.size - size || y > this.size - 1) {
                return false;
            }
        }
        return currentField.placeShip(size, x, y, horizontal);
    }

    public boolean placeMine(int x, int y) {
        Field currentField = (currentPlayer == 1) ? player1Field : player2Field;
        if (x < 0 || y < 0 || x > this.size - 1 || y > this.size - 1) {
            return false;
        }
        return currentField.placeMine(x, y);
    }

    public boolean placeMineSweeper(int x, int y) {
        Field currentField = (currentPlayer == 1) ? player1Field : player2Field;
        if (x < 0 || y < 0 || x > this.size - 1 || y > this.size - 1) {
            return false;
        }
        return currentField.placeMineSweeper(x, y);
    }

    public boolean deleteMine(int x, int y) {
        Field currentField = (currentPlayer == 1) ? player1Field : player2Field;
        if (x < 0 || y < 0 || x > this.size - 1 || y > this.size - 1) {
            return false;
        }
        return currentField.deleteMine(x, y);
    }

    public Attack deleteShip(int x, int y, int currentPlayer) {
        Field currentField = (currentPlayer == 2) ? player1Field : player2Field;
        if (x < 0 || y < 0 || x > this.size - 1 || y > this.size - 1) {
            return UNREAL;
        }
        return currentField.deleteShip(x, y);
    }

    public Attack attack(int x, int y) {
        Field opponentField = (currentPlayer == 1) ? player2Field : player1Field;
        if (x < 0 || y < 0 || x > this.size - 1 || y > this.size - 1) {
            return NOTHING;
        }
        return opponentField.attack(x, y);
    }

    public int[] getKillArea(int currentPlayer) {
        Field opponentField = (currentPlayer == 1) ? player2Field : player1Field;
        return opponentField.getKillArea();
    }

    public int[] getKillShip() {
        Field opponentField = (currentPlayer == 1) ? player2Field : player1Field;
        return opponentField.getKillShip();
    }

    public void switchPlayer() {
        currentPlayer = (currentPlayer == 1) ? 2 : 1;
    }

    public boolean checkWin() {
        return player1Field.allShipsSunk() || player2Field.allShipsSunk();
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

}